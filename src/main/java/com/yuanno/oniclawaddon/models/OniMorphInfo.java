package com.yuanno.oniclawaddon.models;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuanno.oniclawaddon.renderers.morphs.OniRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import xyz.pixelatedw.mineminenomi.abilities.mega.MegaMegaAbility;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.renderers.morphs.MegaRenderer;
import xyz.pixelatedw.mineminenomi.renderers.morphs.ZoanMorphRenderer;

import java.util.Map;

public class OniMorphInfo extends MorphInfo
{
	public static final OniMorphInfo INSTANCE = new OniMorphInfo();

	private static final EntitySize STANDING_SIZE = EntitySize.scalable(1.7F, 2.4F);
	private static final EntitySize CROUCHING_SIZE = EntitySize.scalable(1.7F, 2.39F);

	@Override
	@OnlyIn(Dist.CLIENT)
	public IRenderFactory getRendererFactory(AbstractClientPlayerEntity entity)
	{
		boolean isSlim = entity.getModelName().equals("slim");
		return new OniRenderer.Factory(this, isSlim);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public MorphModel getModel()
	{
		/** To avoid further confusion and splits the model is manually set in the renderer's constructor {@link MiniRenderer}
		 *  this is because the slim parameter is sent to the renderer factory via getRendererFactory
		 *  but not for getModel. This might change in the future or make them more tightly integrated but for now this is just a workaround.
		 */
		return null;
	}
	
	@Override
	public void preRenderCallback(AbstractClientPlayerEntity player, MatrixStack matrixStack, float partialTickTime)
	{
		
	}
	
	@Override
	public AkumaNoMiItem getDevilFruit()
	{
		return null;
	}

	@Override
	public String getForm()
	{
		return "oni";
	}
	

	@Override
	public String getDisplayName()
	{
		return "oni_giant";
	}

	@Override
	public double getEyeHeight()
	{
		return 3;
	}
	
	@Override
	public float getShadowSize()
	{
		return 1.25F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public double getCameraZoom(PlayerEntity player)
	{
		return 3;
	}
	
	@Override
	public boolean canMount()
	{
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public double getCameraHeight(PlayerEntity player) {
		boolean isFirstPerson = Minecraft.getInstance().options.getCameraType() == PointOfView.FIRST_PERSON;
		boolean shouldSit = player.isPassenger() && (player.getVehicle() != null && player.getVehicle().shouldRiderSit());
		if(isFirstPerson && shouldSit)
		{
			return 0.5;
		}
		return 0;
	}

	
	@Override
	public Map<Pose, EntitySize> getSizes()
	{
		return ImmutableMap.<Pose, EntitySize>builder()
			.put(Pose.STANDING, EntitySize.scalable(1.6F, 3.2F))
			.put(Pose.CROUCHING, EntitySize.scalable(1.6F, 3F))
			.build();
	}
}
