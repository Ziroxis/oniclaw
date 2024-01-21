package com.yuanno.oniclawaddon.abilities.oni;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.init.ModI18n;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.UUID;

public class OniOverdriveAbility extends ContinuousAbility implements IParallelContinuousAbility {

    public static final AbilityCore INSTANCE = new AbilityCore.Builder("Oni Overdrive", AbilityCategory.RACIAL, OniOverdriveAbility::new)
            .addDescriptionLine("Goes into overdrive, gaining strength, speed and mobility")
            .build();

    private static final AbilityAttributeModifier JUMP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("c8983d00-b863-11ee-a506-0242ac120002"), INSTANCE, "Oni Overdrive Jump Modifier", 5, AttributeModifier.Operation.ADDITION);
    private static final AbilityAttributeModifier STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("cdeefe24-b863-11ee-a506-0242ac120002"), INSTANCE, "Oni Overdrive Attack Damage Modifier", 4, AttributeModifier.Operation.ADDITION);
    private static final AbilityAttributeModifier STEP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("d07c4ab6-b863-11ee-a506-0242ac120002"), INSTANCE, "Oni Overdrive Step Height Modifier", 1, AttributeModifier.Operation.ADDITION);
    private static final AbilityAttributeModifier FALL_REDUCTION = new AbilityAttributeModifier(UUID.fromString("64016dd4-b86e-11ee-a506-0242ac120002"), INSTANCE, "Oni Overdrive Fall Reduction Modifier", 6, AttributeModifier.Operation.ADDITION);

    private boolean prevSprintValue = false;

    public OniOverdriveAbility(AbilityCore abilityCore)
    {
        super(abilityCore);
        this.setThreshold(40);
        this.setMaxCooldown(125);

        this.onStartContinuityEvent = this::onStartContinuity;
        this.duringContinuityEvent = this::duringContinuity;
        this.afterContinuityStopEvent = this::afterContinuityStopEvent;

    }

    private boolean onStartContinuity(PlayerEntity player)
    {

        player.getAttribute(ModAttributes.STEP_HEIGHT.get()).addTransientModifier(STEP_HEIGHT);
        player.getAttribute(ModAttributes.JUMP_HEIGHT.get()).addTransientModifier(JUMP_HEIGHT);
        player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(STRENGTH_MODIFIER);
        player.getAttribute(ModAttributes.PUNCH_DAMAGE.get()).addTransientModifier(STRENGTH_MODIFIER);
        player.getAttribute(ModAttributes.FALL_RESISTANCE.get()).addTransientModifier(FALL_REDUCTION);

        this.prevSprintValue = player.isSprinting();
        return true;
    }

    private void duringContinuity(PlayerEntity player, int passiveTimer)
    {
        if (passiveTimer % 10 == 0)
        {
            WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY(), player.getZ());
        }

        if (player.isSprinting())
        {
            if (!prevSprintValue)
                player.level.playSound(null, player.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5f, 1);
        }
        else
        {
            prevSprintValue = false;
            return;
        }

        float maxSpeed = 2.2F;

        Vector3d vec = player.getLookAngle();
        if (player.isOnGround())
        {
            player.setDeltaMovement((vec.x * maxSpeed), player.getDeltaMovement().y, (vec.z * maxSpeed));
        }
        else
        {
            player.setDeltaMovement((vec.x * maxSpeed * 0.5F), player.getDeltaMovement().y, (vec.z * maxSpeed * 0.5F));
        }

        this.prevSprintValue = player.isSprinting();
        player.hurtMarked = true;
    }

    private void afterContinuityStopEvent(PlayerEntity player)
    {
        int cooldown = (int) Math.round(this.continueTime / 30.0);
        this.setMaxCooldown(1 + cooldown);
        if (this.continueTime > this.getThreshold() / 1.425 && EntityStatsCapability.get(player).getDoriki() < 2000)
        {
            player.addEffect(new EffectInstance(Effects.HUNGER, 600, 3, true, true));
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 1, true, true));
        }

        player.getAttribute(ModAttributes.STEP_HEIGHT.get()).removeModifier(STEP_HEIGHT);
        player.getAttribute(ModAttributes.JUMP_HEIGHT.get()).removeModifier(JUMP_HEIGHT);
        player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(STRENGTH_MODIFIER);
        player.getAttribute(ModAttributes.PUNCH_DAMAGE.get()).removeModifier(STRENGTH_MODIFIER);
        player.getAttribute(ModAttributes.FALL_RESISTANCE.get()).removeModifier(FALL_REDUCTION);

    }
}
