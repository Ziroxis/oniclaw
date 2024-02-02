package com.yuanno.oniclawaddon.events;

import com.yuanno.oniclawaddon.Main;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SRecalculateEyeHeightPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class OniPassiveEvents {

    private static final AttributeModifier RESISTANCE_MODIFIER = new AttributeModifier(UUID.fromString("aa9e534c-bb0c-11ee-a506-0242ac120002"), "Oni Resistance Multiplier", 0.25, AttributeModifier.Operation.MULTIPLY_BASE);


    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (!(event.getEntityLiving() instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        IEntityStats entityStats = EntityStatsCapability.get(player);
        if (!(entityStats.getRace().equals("oni")))
            return;
        if (!player.getAttribute(Attributes.ARMOR).hasModifier(RESISTANCE_MODIFIER))
            player.getAttribute(Attributes.ARMOR).addTransientModifier(RESISTANCE_MODIFIER);

    }

    @SubscribeEvent
    public static void onPlayerChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        PlayerEntity player = event.getPlayer();
        MinecraftForge.EVENT_BUS.post(new EntityEvent.Size(player, player.getPose(), player.getDimensions(player.getPose()), player.getBbHeight()));
        WyNetwork.sendToAllTrackingAndSelf(new SRecalculateEyeHeightPacket(player.getId()), player);

    }
}
