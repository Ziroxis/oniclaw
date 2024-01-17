package com.yuanno.oniclawaddon.abilities.dragonclaw;

import com.yuanno.oniclawaddon.init.ModI18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.suna.SunaHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.ChargeableAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IGrappleAbility;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.events.SetOnFireEvent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class GrabAbility extends ChargeableAbility implements IGrappleAbility {

    public static final AbilityCore<GrabAbility> INSTANCE = new AbilityCore.Builder("Grab", AbilityCategory.STYLE, GrabAbility::new)
            .addDescriptionLine("The user sets is hand in a specific form, coating it with fire")
            .setSourceElement(SourceElement.FIRE)
            .build();

    private LivingEntity grabbedEntity = null;

    public GrabAbility(AbilityCore abilityCore)
    {
        super(abilityCore);

        this.setMaxCooldown(30);
        this.setMaxChargeTime(3);

        this.onStartChargingEvent = this::onUseEvent;
        this.duringChargingEvent = this::duringChargingEvent;
        this.onEndChargingEvent = this::onEndChargingEvent;

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
        this.grabbedEntity = this.canGrab(player);

        if(this.grabbedEntity == null) {
            return false;
        }

        return true;
    }

    private void duringChargingEvent(PlayerEntity player, int chargeTimer)
    {
        TalonsAbility talonsAbility = AbilityDataCapability.get(player).getEquippedAbility(TalonsAbility.INSTANCE);
        boolean talonsEnabled = talonsAbility != null && talonsAbility.isContinuous();
        if (!talonsEnabled)
        {
            player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NEED_TALONS), Util.NIL_UUID);
            return;
        }
        if(!this.grabbedEntity.isAlive() || !AbilityHelper.canUseMomentumAbility(player) || AbilityHelper.isTargetBlocking(player, this.grabbedEntity))
        {
            this.endCharging(player);
            return;
        }

        this.grabbedEntity.xRot = this.grabbedEntity.xRotO;
        this.grabbedEntity.yRot = this.grabbedEntity.yRotO;

        this.grabbedEntity.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 5, 1));
        this.grabbedEntity.hurt(ModDamageSource.causeAbilityDamage(player, this).bypassArmor().bypassMagic(), 2);
        if(grabbedEntity instanceof PlayerEntity) {
            AbilityHelper.disableAbilities((PlayerEntity) player, 100, (a) -> a.getCategory() == AbilityCategory.ALL);
        }
        AbilityHelper.disableAbilities((PlayerEntity) player, 100, (a) -> a.getCategory() == AbilityCategory.ALL);
        float distance = 2;
        Vector3d lookVec = player.getLookAngle();
        Vector3d pos = new Vector3d(player.getX() + lookVec.x * distance, player.getY() + player.getEyeHeight() / 2 + lookVec.y * distance, player.getZ() + lookVec.z * distance);
        this.grabbedEntity.teleportTo(pos.x, pos.y, pos.z);
        SetOnFireEvent event = new SetOnFireEvent(player, grabbedEntity, 4);
        if (!MinecraftForge.EVENT_BUS.post(event))
            grabbedEntity.setSecondsOnFire(4);
    }

    private boolean onEndChargingEvent(PlayerEntity player)
    {
        TalonsAbility talonsAbility = AbilityDataCapability.get(player).getEquippedAbility(TalonsAbility.INSTANCE);
        boolean talonsEnabled = talonsAbility != null && talonsAbility.isContinuous();
        if (!talonsEnabled)
        {
            player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NEED_TALONS), Util.NIL_UUID);
            return false;
        }
        if (this.grabbedEntity == null)
            return false;

        if(this.grabbedEntity.hurt(ModDamageSource.causeAbilityDamage(player, this).bypassArmor().bypassMagic(), 30))
        {
            this.grabbedEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 300, 1, false, false));
            this.grabbedEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 300, 1, false, false));
            this.grabbedEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 300, 1, false, false));
        }
        Vector3d speed = WyHelper.propulsion(player, 10, 10);
        grabbedEntity.setDeltaMovement(speed.x, 0.5, speed.z);
        grabbedEntity.hurtMarked = true;

        this.setMaxCooldown(15);
        return true;
    }

    @Override
    public LivingEntity getGrabbedEntity() {
        return this.grabbedEntity;
    }
}
