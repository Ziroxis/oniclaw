package com.yuanno.oniclawaddon.entities.projectiles.dragonclaw;

import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.entities.projectiles.mera.*;
import xyz.pixelatedw.mineminenomi.models.abilities.CubeModel;
import xyz.pixelatedw.mineminenomi.models.abilities.SphereModel;
import xyz.pixelatedw.mineminenomi.models.entities.projectiles.FistModel;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;
import xyz.pixelatedw.mineminenomi.renderers.abilities.GlowingAbilityProjectileRenderer;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DragonClawProjectiles {
    public static final RegistryObject<EntityType<HikenProjectile>> FLAMING_SLASH = WyRegistry.registerEntityType("Flaming Slash", () -> WyRegistry.createEntityType(FlamingSlashProjectile::new).fireImmune().sized(1.2f, 40f).build(ModMain.PROJECT_ID + ":flaming_slash"));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(FLAMING_SLASH.get(), new AbilityProjectileRenderer.Factory(new CubeModel()).setColor("ED7F10").setScale(1.0, 24.0, 0.5));
    }
}
