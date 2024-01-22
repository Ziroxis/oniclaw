package com.yuanno.oniclawaddon.init;

import com.yuanno.oniclawaddon.packets.CFinishCCPacketOverwriteMixin;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

public class ModNetwork {
    public static void init()
    {
        WyNetwork.registerPacket(CFinishCCPacketOverwriteMixin.class, CFinishCCPacketOverwriteMixin::encode, CFinishCCPacketOverwriteMixin::decode, CFinishCCPacketOverwriteMixin::handle);

    }
}
