package com.yuanno.oniclawaddon.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.screens.CharacterCreatorScreen;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

@Mixin(value = CharacterCreatorScreen.class, priority = 999)
public class CharacterCreatorScreenMixin {

    @Shadow
    private int[] options;

    @Shadow
    public int getSelectedFactionId() {
        return this.options[0];
    };

    @Shadow
    public int getSelectedRaceId()
    {
        return this.options[1];
    }

    @Shadow
    public int getSelectedStyleId()
    {
        return this.options[2];
    }


    @Inject(method = "completeCreation", at = @At("HEAD"), remap = false, cancellable = true)
    private void onCompleteCreation(CallbackInfo ci) {
        System.out.println("CALLED HERE CREATION");
        Minecraft.getInstance().setScreen(null);
        WyNetwork.sendToServer(new CFinishCCPacketOverwriteMixin(getSelectedFactionId(), getSelectedRaceId(), getSelectedStyleId()));
        ci.cancel();
    }
}
