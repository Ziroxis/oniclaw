package com.yuanno.oniclawaddon.items;

import com.yuanno.oniclawaddon.abilities.oni.ThunderBaguaAbility;
import com.yuanno.oniclawaddon.init.ModAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SAnimateHandPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.damagesource.AbilityDamageSource;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.mera.HiganProjectile;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.items.weapons.CoreSwordItem;
import xyz.pixelatedw.mineminenomi.particles.effects.CommonExplosionParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.List;

public class KaidoWeaponItem extends CoreSwordItem {

    private int MAX_TELEPORT_DISTANCE = 50;

    public KaidoWeaponItem()
    {
        super(6, 5000);
    }



    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        player.getCooldowns().addCooldown(this, 1400);


        BlockPos startPos = player.blockPosition();

        ItemStack stack = player.getMainHandItem();
        stack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlotType.MAINHAND));

        BlockRayTraceResult mop = WyHelper.rayTraceBlocks(player, MAX_TELEPORT_DISTANCE);
        BlockPos blockpos;

        if (mop == null || mop.getType() == RayTraceResult.Type.MISS)
            blockpos = WyHelper.rayTraceBlockSafe(player, MAX_TELEPORT_DISTANCE);
        else
            blockpos = WyHelper.getClearPositionForPlayer(player, mop.getBlockPos());

        if (blockpos == null)
            blockpos = WyHelper.rayTraceBlockSafe(player, MAX_TELEPORT_DISTANCE);

        for(float f = 0; f < 1; f += 0.13)
        {
            int x = (int) MathHelper.lerp(f, startPos.getX(), blockpos.getX());
            int y = (int) MathHelper.lerp(f, startPos.getY(), blockpos.getY());
            int z = (int) MathHelper.lerp(f, startPos.getZ(), blockpos.getZ());
            BlockPos pos = new BlockPos(x, y, z);
            List<LivingEntity> targets = WyHelper.getNearbyLiving(pos, player.level, 3, ModEntityPredicates.getEnemyFactions(player));
            for(LivingEntity target : targets)
            {
                if (player.canSee(target))
                {
                    boolean flag = target.hurt(AbilityDamageSource.causeAbilityDamage(player, ThunderBaguaAbility.INSTANCE.createAbility(), "player").setBlunt(), 70);
                    if (!target.hasEffect(ModEffects.UNCONSCIOUS.get()))
                        target.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 20, 0));
                    if (!target.hasEffect(Effects.CONFUSION))
                        target.addEffect(new EffectInstance(Effects.CONFUSION, 100, 0));
                    if(flag)
                    {
                        WyHelper.spawnParticles(ParticleTypes.SWEEP_ATTACK, (ServerWorld) player.level, target.getX(), target.getY() + target.getEyeHeight(), target.getZ());
                    }
                }
            }
        }

        player.stopRiding();
        player.moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        player.level.playSound(null, player.blockPosition(), ModSounds.DASH_ABILITY_SWOOSH_SFX.get(), SoundCategory.PLAYERS, 0.5f, 1);
        return new ActionResult<>(ActionResultType.PASS, player.getItemInHand(hand));
    }
}