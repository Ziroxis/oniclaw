package com.yuanno.oniclawaddon.mixins;

import com.google.common.base.Strings;
import com.yuanno.oniclawaddon.models.OniMorphInfo;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.api.helpers.MorphHelper;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;

import java.util.Map;

@Mixin(value = PlayerEntity.class, priority = 950)
public class PlayerEntityMixin {

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    public void getSize(Pose pose, CallbackInfoReturnable<EntitySize> callbackInfoReturnable)
    {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        IEntityStats entityStats = EntityStatsCapability.get(player);
        if (entityStats.getRace().equals("oni")) {
            OniMorphInfo info = new OniMorphInfo();
            Map<Pose, EntitySize> poses = info.getSizes();
            if (poses != null && poses.containsKey(player.getPose()) && poses.get(player.getPose()) != null)
                callbackInfoReturnable.setReturnValue(poses.get(player.getPose()));
        }
    }
}
