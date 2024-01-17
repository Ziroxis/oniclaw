package com.yuanno.oniclawaddon;

import com.yuanno.oniclawaddon.init.ModAbilities;
import com.yuanno.oniclawaddon.renderers.TalonsRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.pixelatedw.mineminenomi.renderers.abilities.EleclawRenderer;
import xyz.pixelatedw.mineminenomi.renderers.layers.*;
import xyz.pixelatedw.mineminenomi.renderers.layers.abilities.HanaCalendulaLayer;
import xyz.pixelatedw.mineminenomi.renderers.layers.abilities.HanaHandsLayer;
import xyz.pixelatedw.mineminenomi.renderers.layers.abilities.HanaWingsLayer;
import xyz.pixelatedw.mineminenomi.renderers.layers.armor.CaptainCapeOverlayLayer;
import xyz.pixelatedw.mineminenomi.renderers.layers.armor.LowerHalfArmorLayer;
import xyz.pixelatedw.mineminenomi.renderers.layers.armor.UpperHalfArmorLayer;

import java.util.Map;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MOD_ID)
public class Main
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oniclawaddon";
    public Main() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModAbilities.register(eventBus);
        MinecraftForge.EVENT_BUS.register(this);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

    }


    private void doClientStuff(final FMLClientSetupEvent event) {
         event.enqueueWork(() -> {
             for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
                 EntityRenderer entityRenderer = entry.getValue();
                 if (entityRenderer instanceof LivingRenderer) {
                     LivingRenderer renderer = (LivingRenderer) entityRenderer;
                     renderer.addLayer(new TalonsRenderer(renderer));
                 }
             }

             for (Map.Entry<String, PlayerRenderer> entry : Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().entrySet()) {
                 PlayerRenderer renderer = entry.getValue();
                 renderer.addLayer(new TalonsRenderer(renderer));

             }
         });
    }

}
