package com.yuanno.oniclawaddon.entities.projectiles.items;

import com.yuanno.oniclawaddon.entities.projectiles.dragonclaw.FlamingSlashProjectile;
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
public class ItemProjectiles {
    public static final RegistryObject<EntityType<DivineDepartureProjectile>> DIVINE_DEPARTURE = WyRegistry.registerEntityType("Divine Departure", () -> WyRegistry.createEntityType(DivineDepartureProjectile::new).fireImmune().sized(20f, 1.2f).build(ModMain.PROJECT_ID + ":divine_departure"));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(DIVINE_DEPARTURE.get(), new AbilityProjectileRenderer.Factory(new CubeModel()).setColor("FFFFFF").setScale(20.0, 1.0, 0.5));
    }
}
