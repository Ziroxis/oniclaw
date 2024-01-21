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

        Vector3d speed = WyHelper.propulsion(player, 0, 6, 0);
        player.setDeltaMovement(speed.x, speed.y, speed.z);
        ((ServerPlayerEntity)player).connection.send(new SEntityVelocityPacket(player));
        return true;

    }

    private void duringCooldown(PlayerEntity player, int cooldownTimer)
    {
        if (!this.canDealDamage())
            return;

        if (cooldownTimer < 120 && cooldownTimer > 100)
        {
            Vector3d speed = WyHelper.propulsion(player, 0, -6, 0);
            player.setDeltaMovement(speed.x, speed.y, speed.z);
            ((ServerPlayerEntity) player).connection.send(new SEntityVelocityPacket(player));

        }
        if (player.isOnGround())
        {
            ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.level, player.getX(), player.getY(), player.getZ(), 4.0F);
            explosion.setStaticDamage(60.0F);
            explosion.setDamageOwner(false);
            explosion.setSmokeParticles(new CommonExplosionParticleEffect(4));
            explosion.setFireAfterExplosion(true);
            explosion.doExplosion();
        }
    }

    public boolean canDealDamage()
    {
        return this.cooldown > WyHelper.percentage(90, this.getMaxCooldown());
    }

    @Override
    public HashSet<UUID> getHits() {
        return this.hits;
    }
}
