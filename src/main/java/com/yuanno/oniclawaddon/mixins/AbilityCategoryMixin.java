package com.yuanno.oniclawaddon.mixins;

import com.yuanno.oniclawaddon.packets.CFinishCCPacketOverwriteMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;
import xyz.pixelatedw.mineminenomi.screens.CharacterCreatorScreen;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import java.util.function.Function;

@Mixin(value = AbilityCategory.class, priority = 999)
public class AbilityCategoryMixin {

// not used
    @Shadow
    private Function<PlayerEntity, ResourceLocation> iconFunction = null;

    @Redirect(method = "getIcon", at = @At("HEAD"), remap = false)
    private ResourceLocation getIcon(PlayerEntity player, CallbackInfoReturnable<ResourceLocation> cir) {
        IEntityStats entityStats = EntityStatsCapability.get(player);
        if (this.iconFunction == null) {
            return null;
        }
        return this.iconFunction.apply(player);

    }
}
