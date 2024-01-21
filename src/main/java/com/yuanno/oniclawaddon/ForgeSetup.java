package com.yuanno.oniclawaddon;

import com.mojang.brigadier.CommandDispatcher;
import com.yuanno.oniclawaddon.commands.FightingStyleChangeCommand;
import com.yuanno.oniclawaddon.commands.RaceChangeCommand;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeSetup {

    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent event)
    {
        CommandDispatcher dispatcher = event.getServer().getCommands().getDispatcher();
        RaceChangeCommand.register(dispatcher);
        FightingStyleChangeCommand.register(dispatcher);

    }
}
