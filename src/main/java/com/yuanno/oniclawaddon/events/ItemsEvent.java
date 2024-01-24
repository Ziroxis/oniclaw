package com.yuanno.oniclawaddon.events;

import com.yuanno.oniclawaddon.Main;
import com.yuanno.oniclawaddon.init.ModItems;
import com.yuanno.oniclawaddon.items.GryphonItem;
import com.yuanno.oniclawaddon.items.NapoleonItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModValues;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class ItemsEvent {

    @SubscribeEvent
    public static void tickEventItems(TickEvent.PlayerTickEvent event)
    {
        PlayerEntity player = event.player;
        if (player.getMainHandItem().getItem().asItem().equals(ModItems.NAPOLEON.get())) {
            if (player.getCooldowns().isOnCooldown(ModItems.NAPOLEON.get()))
                return;
            ItemStack itemStack = player.getMainHandItem();
            if (!itemStack.getTag().contains("activate")) {
                itemStack.setTag(new CompoundNBT());
                itemStack.getTag().putInt("cooldown", 300);
                itemStack.getTag().putBoolean("activate", false);
            }
            if (!itemStack.getTag().getBoolean("activate"))
                return;
            if (itemStack.getTag().getInt("cooldown") > 0) {
                if (itemStack.getTag().getInt("cooldown") == 300)
                    player.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 300, 0));
                int cooldown = itemStack.getTag().getInt("cooldown");
                itemStack.getTag().putInt("cooldown", cooldown - 1);
            } else if (itemStack.getTag().getInt("cooldown") == 0) {
                NapoleonItem napoleonItem = (NapoleonItem) player.getMainHandItem().getItem();
                napoleonItem.onUse(player);
                itemStack.getTag().putBoolean("activate", false);
            }
        }

        if (player.getMainHandItem().getItem().asItem().equals(ModItems.GRYPHON.get())) {
            if (player.getCooldowns().isOnCooldown(ModItems.GRYPHON.get()))
                return;
            ItemStack itemStack = player.getMainHandItem();
            if (!itemStack.getTag().contains("activate")) {
                itemStack.setTag(new CompoundNBT());
                itemStack.getTag().putInt("cooldown", 50);
                itemStack.getTag().putBoolean("activate", false);
            }
            if (!itemStack.getTag().getBoolean("activate"))
                return;
            if (itemStack.getTag().getInt("cooldown") > 0) {
                int cooldown = itemStack.getTag().getInt("cooldown");
                itemStack.getTag().putInt("cooldown", cooldown - 1);
            } else if (itemStack.getTag().getInt("cooldown") == 0) {
                GryphonItem gryphonItem = (GryphonItem) player.getMainHandItem().getItem();
                gryphonItem.onUse(player);
                itemStack.getTag().putBoolean("activate", false);
            }
        }
    }
}
