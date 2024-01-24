package com.yuanno.oniclawaddon.items;

import com.yuanno.oniclawaddon.entities.projectiles.items.DivineDepartureProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import xyz.pixelatedw.mineminenomi.entities.projectiles.mera.HiganProjectile;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.items.weapons.CoreSwordItem;

public class GryphonItem extends CoreSwordItem {
    private int cooldownTimer = 0;
    private final int cooldownDuration = 1800; // Adjust the cooldown duration in ticks (20 ticks = 1 second)
    public GryphonItem()
    {
        super(6, 5000);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack itemStack = player.getMainHandItem();
        itemStack.getTag().putBoolean("activate", true);
        return new ActionResult<>(ActionResultType.PASS, player.getItemInHand(hand));
    }

    public void onUse(PlayerEntity player)
    {
        DivineDepartureProjectile proj = new DivineDepartureProjectile(player.level, player, null);
        player.level.addFreshEntity(proj);
        proj.shootFromRotation(player, player.xRot, player.yRot, 0, 2f, 1);
        player.getCooldowns().addCooldown(this, 1200);
    }
}
