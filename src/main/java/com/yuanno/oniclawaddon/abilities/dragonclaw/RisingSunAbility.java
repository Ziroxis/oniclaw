package com.yuanno.oniclawaddon.abilities.dragonclaw;

import com.yuanno.oniclawaddon.init.ModI18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IHitTrackerAbility;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.events.SetOnFireEvent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class RisingSunAbility extends Ability implements IHitTrackerAbility {

    public static final AbilityCore INSTANCE = new AbilityCore.Builder("Rising Sun", AbilityCategory.STYLE, RisingSunAbility::new)
            .addDescriptionLine("Launch yourself forward at high speeds, uppercutting enemies in your way")
            .setSourceHakiNature(SourceHakiNature.HARDENING)
            .setSourceHakiNature(SourceHakiNature.IMBUING)
            .setSourceHakiNature(SourceHakiNature.SPECIAL)
            .setSourceType(SourceType.PHYSICAL)
            .setSourceElement(SourceElement.FIRE)
            .build();

    private HashSet<UUID> hits = new HashSet<>();

    public RisingSunAbility(AbilityCore abilityCore)
    {
        super(abilityCore);
        this.setMaxCooldown(40);

        this.onUseEvent = this::onUseEvent;
        this.duringCooldownEvent = this::duringCooldown;

    }

    private boolean onUseEvent(PlayerEntity player)
    {
        TalonsAbility talonsAbility = AbilityDataCapability.get(player).getEquippedAbility(TalonsAbility.INSTANCE);
        boolean talonsEnabled = talonsAbility != null && talonsAbility.isContinuous();
        if (!talonsEnabled)
        {
            player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NEED_TALONS), Util.NIL_UUID);
            return false;
        }
        if(!AbilityHelper.canUseMomentumAbility(player))
            return false;

        this.clearHits();

        player.level.playSound(null, player.blockPosition(), ModSounds.MERA_SFX.get(), SoundCategory.PLAYERS, 0.5f, 0.5f + (this.random.nextFloat() / 3));
        Vector3d speed = WyHelper.propulsion(player, 3, 3, 3);
        player.setDeltaMovement(speed.x, 0.5 + speed.y, speed.z);

        ((ServerPlayerEntity)player).connection.send(new SEntityVelocityPacket(player));
        return true;
    }

    private void duringCooldown(PlayerEntity player, int cooldownTimer)
    {
        if (this.canDealDamage())
        {
            if (cooldownTimer < 80)
            {
                Vector3d speed = WyHelper.propulsion(player, 3, 3, 3);
                player.setDeltaMovement(speed.x, speed.y, speed.z);
                ((ServerPlayerEntity) player).connection.send(new SEntityVelocityPacket(player));

                if (cooldownTimer % 2 == 0)
                    WyHelper.spawnParticleEffect(ModParticleEffects.HEAT_DASH.get(), player, player.getX(), player.getY(), player.getZ());
            }
            List<LivingEntity> list = WyHelper.getNearbyLiving(player.blockPosition(), player.level, 1.4, ModEntityPredicates.getEnemyFactions(player));

            for (LivingEntity target : list)
            {
                if(target.hurt(ModDamageSource.causeAbilityDamage(player, this, "player"), 60))
                {
                    Vector3d speedEntity = WyHelper.propulsion(target, 2, 2);
                    target.setDeltaMovement(speedEntity.x, 2, speedEntity.z);
                    target.hurtMarked = true;
                }
                if(this.canHit(target) && player.canSee(target))
                {
                    SetOnFireEvent event = new SetOnFireEvent(player, target, 2);
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        target.setSecondsOnFire(2);
                    }
                }
            }
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
