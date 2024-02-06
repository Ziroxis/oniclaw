package com.yuanno.oniclawaddon.abilities.dragonclaw;

import com.yuanno.oniclawaddon.entities.projectiles.dragonclaw.FlamingSlashProjectile;
import com.yuanno.oniclawaddon.init.ModI18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IHitTrackerAbility;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;

public class FlamingSlashAbility extends Ability {

    public static final AbilityCore INSTANCE = new AbilityCore.Builder("Flaming Slash", AbilityCategory.STYLE, FlamingSlashAbility::new)
            .addDescriptionLine("Send an enormous vertical slash in flames")
            .setSourceHakiNature(SourceHakiNature.HARDENING)
            .setSourceHakiNature(SourceHakiNature.IMBUING)
            .setSourceHakiNature(SourceHakiNature.SPECIAL)
            .setSourceType(SourceType.SLASH)
            .setSourceElement(SourceElement.FIRE)
            .build();

    public FlamingSlashAbility(AbilityCore core) {
        super(core);
        this.setMaxCooldown(40);
        this.onUseEvent = this::onUseEvent;

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

        FlamingSlashProjectile flamingSlashProjectile = new FlamingSlashProjectile(player.level, player, this);
        player.level.addFreshEntity(flamingSlashProjectile);
        flamingSlashProjectile.shootFromRotation(player, player.xRot, player.yRot, 0, 2f, 1);
        return true;
    }
}
