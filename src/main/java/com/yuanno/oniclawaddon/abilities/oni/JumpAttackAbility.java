package com.yuanno.oniclawaddon.abilities.oni;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.math.vector.Vector3d;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.particles.effects.CommonExplosionParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.HashSet;
import java.util.UUID;

public class JumpAttackAbility extends Ability implements IHitTrackerAbility {

    public static final AbilityCore INSTANCE = new AbilityCore.Builder("Jump Attack", AbilityCategory.RACIAL, JumpAttackAbility::new)
            .addDescriptionLine("Launch yourself upwards, descending dealing damage")
            .build();

    private HashSet<UUID> hits = new HashSet<>();

    public JumpAttackAbility(AbilityCore abilityCore)
    {
        super(abilityCore);
        this.setMaxCooldown(20);

        this.onUseEvent = this::onUseEvent;
        this.duringCooldownEvent = this::duringCooldown;

    }

    private boolean onUseEvent(PlayerEntity player)
    {
        this.clearHits();

        Vector3d speed = WyHelper.propulsion(player, 3, -8, 3);
        player.setDeltaMovement(speed.x, 0.8 + speed.y, speed.z);
        ((ServerPlayerEntity)player).connection.send(new SEntityVelocityPacket(player));
        return true;

    }

    private void duringCooldown(PlayerEntity player, int cooldownTimer)
    {

        if (!this.canDealDamage())
            return;

        System.out.println(cooldownTimer);
        if (cooldownTimer > 360 && cooldownTimer < 370) {
            Vector3d speed = WyHelper.propulsion(player, 3, 18, 3);
            player.setDeltaMovement(speed.x, speed.y, speed.z);
            ((ServerPlayerEntity) player).connection.send(new SEntityVelocityPacket(player));
        }

        if (cooldownTimer > 320 && cooldownTimer < 360 && player.isOnGround())
        {
            ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.level, player.getX(), player.getY(), player.getZ(), 4.0F);
            explosion.setStaticDamage(60.0F);
            explosion.setDamageOwner(false);
            explosion.setDestroyBlocks(false);
            explosion.setSmokeParticles(new CommonExplosionParticleEffect(4));
            explosion.setFireAfterExplosion(false);
            explosion.doExplosion();

        }
    }

    public boolean canDealDamage()
    {
        return this.cooldown > WyHelper.percentage(80, this.getMaxCooldown());
    }

    @Override
    public HashSet<UUID> getHits() {
        return this.hits;
    }
}
