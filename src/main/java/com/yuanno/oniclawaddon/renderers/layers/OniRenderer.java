package com.yuanno.oniclawaddon.renderers.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuanno.oniclawaddon.Main;
import com.yuanno.oniclawaddon.abilities.dragonclaw.TalonsAbility;
import com.yuanno.oniclawaddon.events.DragonClawPassiveEvents;
import com.yuanno.oniclawaddon.models.HornModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;
import xyz.pixelatedw.mineminenomi.init.ModValues;
import xyz.pixelatedw.mineminenomi.models.morphs.MinkBunnyPartialModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OniRenderer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M>
{
	private final HornModel HORN_MODEL = new HornModel();
	private final ResourceLocation HORN_TEXTURE = new ResourceLocation("mineminenomi", "textures/models/horns.png");


	public OniRenderer(IEntityRenderer renderer)
	{
		super(renderer);
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		IEntityStats entityProps = EntityStatsCapability.get(entity);

		boolean isOni = entityProps.getRace().equals("oni");
		boolean isInvisible = entity.hasEffect(Effects.INVISIBILITY);
		if (isOni && !isInvisible)
		{
			//this.getParentModel().copyPropertiesTo(HORN_MODEL);
			HORN_MODEL.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			HORN_MODEL.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucent(HORN_TEXTURE)), packedLight, 0, 1f, 1f, 1f, 1f);

		}
	}
}
