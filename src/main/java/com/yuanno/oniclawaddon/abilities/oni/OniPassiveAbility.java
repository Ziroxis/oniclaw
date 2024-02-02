package com.yuanno.oniclawaddon.abilities.oni;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.PassiveAbility;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SRecalculateEyeHeightPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;


public class OniPassiveAbility extends PassiveAbility {

    static boolean changedSize = false;
    public static final AbilityCore INSTANCE = new AbilityCore.Builder("Oni Passive", AbilityCategory.RACIAL, OniPassiveAbility::new)
            .addDescriptionLine("Changes size and stat of the player")
            .build();

    public OniPassiveAbility(AbilityCore abilityCore)
    {
        super(abilityCore);

        this.duringPassiveEvent = this::duringPassiveEvent;
    }

    private void duringPassiveEvent(PlayerEntity player)
    {
        if (player.level.isClientSide)
            return;
        if (!changedSize) {
            MinecraftForge.EVENT_BUS.post(new EntityEvent.Size(player, player.getPose(), player.getDimensions(player.getPose()), player.getBbHeight()));
            WyNetwork.sendToAllTrackingAndSelf(new SRecalculateEyeHeightPacket(player.getId()), player);
            player.refreshDimensions();
            changedSize = true;
        }
    }


}
