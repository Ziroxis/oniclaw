package com.yuanno.oniclawaddon.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.entities.projectiles.brawler.KingPunchProjectile;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.items.weapons.CoreSwordItem;

public class NapoleonItem extends CoreSwordItem {

    public NapoleonItem()
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
        ItemStack itemStack = player.getMainHandItem();



        AbilityProjectileEntity proj = new KingPunchProjectile(player.level, player, null);
        proj.setDamage(90);
        proj.onBlockImpactEvent = (pos) -> {
            ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.level, proj.getX(), proj.getY(), proj.getZ(), 5.0F);
            explosion.setStaticDamage(0.0F);
            explosion.setExplosionSound(false);
            explosion.setDamageOwner(false);
            explosion.setDestroyBlocks(true);
            explosion.setFireAfterExplosion(false);
            explosion.setDamageEntities(false);
            explosion.doExplosion();
        };
        player.level.addFreshEntity(proj);
        proj.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 2.0F, 1.0F);
        player.removeEffect(ModEffects.MOVEMENT_BLOCKED.get());

        itemStack.getTag().putInt("cooldown", 120);
        player.getCooldowns().addCooldown(this, 3600);

    }
}
