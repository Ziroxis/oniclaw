package com.yuanno.oniclawaddon.events;

import com.yuanno.oniclawaddon.Main;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;

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
}
