package com.yuanno.oniclawaddon.entities.projectiles.oni;

import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.entities.projectiles.mera.HikenProjectile;
import xyz.pixelatedw.mineminenomi.models.abilities.CubeModel;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OniProjectiles {
    public static final RegistryObject<EntityType<DominanceProjectile>> DOMINANCE_SLASH = WyRegistry.registerEntityType("Dominance Slash", () -> WyRegistry.createEntityType(DominanceProjectile::new).fireImmune().sized(1.2f, 2f).build(ModMain.PROJECT_ID + ":dominance_slash"));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(DOMINANCE_SLASH.get(), new AbilityProjectileRenderer.Factory(new CubeModel()).setColor("800080").setScale(1.0, 4.0, 0.5));
    }
}
