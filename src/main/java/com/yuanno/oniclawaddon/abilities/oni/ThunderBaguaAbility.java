package com.yuanno.oniclawaddon.abilities.oni;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SAnimateHandPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import xyz.pixelatedw.mineminenomi.abilities.swordsman.ShiShishiSonsonAbility;
import xyz.pixelatedw.mineminenomi.animations.swordsman.IttoryuPoseAnimation;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.animations.IAnimation;
import xyz.pixelatedw.mineminenomi.api.damagesource.AbilityDamageSource;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.particles.effects.CommonExplosionParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ThunderBaguaAbility extends ChargeableAbility implements IAnimatedAbility, IHitTrackerAbility
{
	public static final AbilityCore<ShiShishiSonsonAbility> INSTANCE = new AbilityCore.Builder("Thunder Bagua", AbilityCategory.RACIAL, ThunderBaguaAbility::new)
		.addDescriptionLine("The user dashes forward and rapidly smashes the opponent creating an explosion")
		.setSourceHakiNature(SourceHakiNature.IMBUING)
		.setSourceType(SourceType.BLUNT)
		.build();

	private static final float MAX_TELEPORT_DISTANCE = 30;

	private HashSet<UUID> hits = new HashSet<UUID>();

	public ThunderBaguaAbility(AbilityCore core)
	{
		super(core);
		this.setMaxCooldown(40);
		this.setMaxChargeTime(2);

		this.onStartChargingEvent = this::onStartChargingEvent;
		this.duringChargingEvent = this::duringChargingEvent;
		this.onEndChargingEvent = this::endChargeEvent;
	}

	private boolean onStartChargingEvent(PlayerEntity player)
	{
		if (!AbilityHelper.canUseMomentumAbility(player))
			return false;

		if (!player.getMainHandItem().getItem().equals(ModWeapons.MACE.get()))
		{
			player.sendMessage(new TranslationTextComponent(com.yuanno.oniclawaddon.init.ModI18n.ABILITY_MESSAGE_NEED_MACE), Util.NIL_UUID);
			return false;
		}

		this.clearHits();

		return true;
	}

	private void duringChargingEvent(PlayerEntity player, int timer)
	{
		player.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 5, 1, false, false));
		AbilityHelper.slowEntityFall(player, 15);
	}

	private boolean endChargeEvent(PlayerEntity player)
	{
		if (!player.getMainHandItem().getItem().equals(ModWeapons.MACE.get()))
		{
			player.sendMessage(new TranslationTextComponent(com.yuanno.oniclawaddon.init.ModI18n.ABILITY_MESSAGE_NEED_MACE), Util.NIL_UUID);
			return true;
		}
		
		BlockPos startPos = player.blockPosition();
		
		ItemStack stack = player.getMainHandItem();
		stack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlotType.MAINHAND));

		BlockRayTraceResult mop = WyHelper.rayTraceBlocks(player, MAX_TELEPORT_DISTANCE);
		BlockPos blockpos;

		if (mop == null || mop.getType() == Type.MISS)
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
				if (player.canSee(target) && this.canHit(target))
				{
					boolean flag = target.hurt(AbilityDamageSource.causeAbilityDamage(player, this, "player").setBlunt(), 90);
					ExplosionAbility explosion = AbilityHelper.newExplosion(player, player.level, target.getX(), target.getY(), target.getZ(), 4.0F);
					explosion.setStaticDamage(10.0F);
					explosion.setDamageOwner(false);
					explosion.setSmokeParticles(new CommonExplosionParticleEffect(4));
					explosion.setFireAfterExplosion(false);
					explosion.doExplosion();
					if(flag)
					{
						WyHelper.spawnParticles(ParticleTypes.SWEEP_ATTACK, (ServerWorld) player.level, target.getX(), target.getY() + target.getEyeHeight(), target.getZ());
					}
				}
			}
		}	

		player.stopRiding();
		player.teleportToWithTicket(blockpos.getX(), blockpos.getY(), blockpos.getZ());
		((ServerWorld) player.level).getChunkSource().broadcastAndSend(player, new SAnimateHandPacket(player, 0));
		player.level.playSound(null, player.blockPosition(), ModSounds.DASH_ABILITY_SWOOSH_SFX.get(), SoundCategory.PLAYERS, 0.5f, 1);
		
		return true;
	}

	@Override
	public IAnimation getAnimation()
	{
		return IttoryuPoseAnimation.INSTANCE;
	}

	@Override
	public boolean isAnimationActive(LivingEntity entity)
	{
		return this.isCharging();
	}

	@Override
	public HashSet<UUID> getHits() {
		return this.hits;
	}
}