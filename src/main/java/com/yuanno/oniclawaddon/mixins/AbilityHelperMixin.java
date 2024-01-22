package com.yuanno.oniclawaddon.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;

import java.lang.reflect.Field;
import java.util.function.Function;

@Mixin(AbilityHelper.class)
public class AbilityHelperMixin {

    @Shadow
    public static Function<PlayerEntity, ResourceLocation> RACE_CATEGORY_ICON;


    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void modifyStaticBlock(CallbackInfo info) {
        try {
            // Use reflection to get the field
            Field field = AbilityHelper.class.getDeclaredField("RACE_CATEGORY_ICON");

            // Make the field accessible, even if it is private or final
            field.setAccessible(true);

            // Your modifications here, for example:
            RACE_CATEGORY_ICON = (player) -> {
                String iconName = null;
                IEntityStats props = EntityStatsCapability.get(player);
                if (props.isHuman()) {
                    iconName = "human-rokushiki";
                    System.out.println("TEST");
                } else if (props.isFishman()) {
                    iconName = "fishman-karate";
                } else if (props.isCyborg()) {
                    iconName = "cyborg-abilities";
                } else if (props.isMink()) {
                    iconName = "mink-electro";
                } else if (props.getRace().equals("oni")) {
                    iconName = "oni-abilities";
                }
                System.out.println("MIXIN APPLIED");
                return iconName != null ? new ResourceLocation("mineminenomi", "textures/gui/icons/" + iconName + ".png") : null;
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}