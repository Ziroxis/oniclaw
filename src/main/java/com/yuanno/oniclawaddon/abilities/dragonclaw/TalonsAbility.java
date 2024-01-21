package com.yuanno.oniclawaddon.abilities.dragonclaw;

import com.yuanno.oniclawaddon.init.ModI18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;


public class TalonsAbility extends PunchAbility implements IParallelContinuousAbility {

    public static final AbilityCore<TalonsAbility> INSTANCE = new AbilityCore.Builder("Talons", AbilityCategory.STYLE, TalonsAbility::new)
            .addDescriptionLine("The user sets is hand in a specific form, coating it with fire")
            .setSourceHakiNature(SourceHakiNature.HARDENING)
            .setSourceType(SourceType.FIST)
            .setSourceElement(SourceElement.FIRE)
            .build();


    public TalonsAbility(AbilityCore abilityCore)
    {
        super(abilityCore);
        this.setMaxCooldown(1);
        this.setStoppingAfterHit(false);

        this.onStartContinuityEvent = this::onStartContinuityEvent;
        this.duringContinuityEvent = this::duringContinuityEvent;
        this.onHitEntityEvent = this::onHitEntityEvent;

    }

    public float onHitEntityEvent(PlayerEntity player, LivingEntity target)
    {
        if (player.getMainHandItem().isEmpty())
            return 30;
        else
            return 0;
    }

    private boolean onStartContinuityEvent(PlayerEntity player)
    {
        BusoshokuHakiInternalDestructionAbility busoshokuHakiInternalDestructionAbility = AbilityDataCapability.get(player).getEquippedAbility(BusoshokuHakiInternalDestructionAbility.INSTANCE);
        boolean emissionEnabled = busoshokuHakiInternalDestructionAbility != null && busoshokuHakiInternalDestructionAbility.isContinuous();
        if (!emissionEnabled)
        {
            player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NEED_INTERNAL), Util.NIL_UUID);
            return false;
        }
        return true;
    }

    private void duringContinuityEvent(PlayerEntity player, int i)
    {
        BusoshokuHakiInternalDestructionAbility busoshokuHakiInternalDestructionAbility = AbilityDataCapability.get(player).getEquippedAbility(BusoshokuHakiInternalDestructionAbility.INSTANCE);
        boolean emissionEnabled = busoshokuHakiInternalDestructionAbility != null && busoshokuHakiInternalDestructionAbility.isContinuous();
        if (!emissionEnabled)
        {
            player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NEED_INTERNAL), Util.NIL_UUID);
            this.tryStoppingContinuity(player);
        }
    }


}
