package com.yuanno.oniclawaddon.mixins.client;

import com.google.common.base.Strings;
import com.yuanno.oniclawaddon.models.OniMorphInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;

@Mixin(value = ActiveRenderInfo.class, priority = 950)
public class ActiveRenderInfoMixin {

    @Shadow
    public Vector3d position;


    @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
    public void getMaxZoom(double startingDistance, CallbackInfoReturnable<Double> callback)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        IEntityStats entityStats = EntityStatsCapability.get(player);
        if (!entityStats.getRace().equals("oni"))
            return;
        OniMorphInfo info = new OniMorphInfo();

        double zoom = info.getCameraZoom(player);
        if(zoom != 0)
            callback.setReturnValue(zoom);


    }

    @Inject(method = "setPosition", at = @At("TAIL"))
    public void setPosition(double x, double y, double z, CallbackInfo callback)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        OniMorphInfo info = new OniMorphInfo();
        IEntityStats entityStats = EntityStatsCapability.get(player);
        if (!entityStats.getRace().equals("oni"))
            return;

        double height = info.getCameraHeight(player);
        if(height != 0)
            this.position = new Vector3d(x, y + height, z);
    }
}
