package com.yuanno.oniclawaddon.renderers.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuanno.oniclawaddon.models.HornModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;

public class HornRenderer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M>
{
	private final HornModel HORN_MODEL = new HornModel();
	private final ResourceLocation HORN_TEXTURE = new ResourceLocation("mineminenomi", "textures/models/horns.png");


	public HornRenderer(IEntityRenderer renderer)
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
			HORN_MODEL.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			HORN_MODEL.renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucent(HORN_TEXTURE)), packedLight, 0, 1f, 1f, 1f, 1f);

		}
	}
}
