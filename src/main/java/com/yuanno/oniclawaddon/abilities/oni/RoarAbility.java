package com.yuanno.oniclawaddon.abilities.oni;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.SAnimateHandPacket;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;
import xyz.pixelatedw.mineminenomi.abilities.goro.SangoAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.ChargeableAbility;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.entities.projectiles.goro.LightningEntity;
import xyz.pixelatedw.mineminenomi.entities.zoan.VoltAmaruMorphInfo;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.awt.*;

public class RoarAbility extends ChargeableAbility
{
	public static final AbilityCore INSTANCE = new AbilityCore.Builder("Roar", AbilityCategory.RACIAL, RoarAbility::new)
		.addDescriptionLine("Launches a powerful roar")
		.setSourceHakiNature(SourceHakiNature.SPECIAL)
		.build();

	public RoarAbility(AbilityCore core)
	{
		super(core);
		this.setMaxCooldown(25);
		this.setMaxChargeTime(2.5);

		this.duringChargingEvent = this::duringChargingEvent;
		this.onEndChargingEvent = this::onEndChargingEvent;
	}

	private void duringChargingEvent(PlayerEntity player, int charge)	
	{
		if(charge % 10 == 0)
		{
			EntityRayTraceResult trace = WyHelper.rayTraceEntities(player, 0.8);
		}
	}
	
	private boolean onEndChargingEvent(PlayerEntity player)
	{
		RayTraceResult mop = WyHelper.rayTraceBlocks(player, 32);
		double beamDistance = Math.sqrt(player.distanceToSqr(mop.getLocation().x, mop.getLocation().y, mop.getLocation().z));



		float damage = 70;
		float size = 0.34F;
		float length = 50F;

		((ServerWorld) player.level).getChunkSource().broadcastAndSend(player, new SAnimateHandPacket(player, 0));
		LightningEntity bolt = new LightningEntity(player, length + (float) beamDistance, 20F, this.getCore());
		bolt.setColor(Color.MAGENTA);
		bolt.setAliveTicks(40);
		bolt.setDamage(damage);
		bolt.setExplosion((int) (3), true, 0.3F, 10);
		bolt.setSize(size);
		bolt.setBoxSizeDivision(0.225f);
		bolt.setAngle(100);
		bolt.setTargetTimeToReset(60);
		bolt.disableExplosionKnockback();

		bolt.setBranches((int) (5 + beamDistance / 100));
		int segments = (int) (beamDistance * 0.5);
		bolt.setSegments((int) (segments + WyHelper.randomWithRange(-segments / 4, segments / 4)));
		player.level.addFreshEntity(bolt);

		if(damage > 14)
		{
			long seed = bolt.seed;
			bolt = new LightningEntity(player, length + (float) beamDistance, 20, this.getCore());
			bolt.seed = seed;
			bolt.setAliveTicks(25);
			bolt.setDamage(0);
			bolt.setExplosion(0, false);
			bolt.setSize(size*0.9F);
			bolt.setBoxSizeDivision(0.225f);
			bolt.setColor(new Color(255, 255, 255, 100));
			bolt.setAngle(100);

			bolt.setBranches((int) (5 + beamDistance / 100));
			bolt.setSegments((int) (segments + WyHelper.randomWithRange(-segments / 4, segments / 4)));
			player.level.addFreshEntity(bolt);
		}
		return true;
	}
}
