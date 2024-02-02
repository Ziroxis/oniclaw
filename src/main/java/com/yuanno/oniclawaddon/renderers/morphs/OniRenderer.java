package com.yuanno.oniclawaddon.renderers.morphs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.models.morphs.NoMorphModel;
import xyz.pixelatedw.mineminenomi.renderers.morphs.MegaRenderer;
import xyz.pixelatedw.mineminenomi.renderers.morphs.ZoanMorphRenderer;

public class OniRenderer<T extends AbstractClientPlayerEntity, M extends MorphModel> extends ZoanMorphRenderer<T, M>
{
	public OniRenderer(EntityRendererManager rendererManager, MorphInfo info, boolean hasSmallHands)
	{
		super(rendererManager, info, hasSmallHands);
		this.model = new NoMorphModel(hasSmallHands);
		this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.0F)));
		this.addLayer(new com.yuanno.oniclawaddon.renderers.layers.OniRenderer<>(this));
	}

	@Override
	public void render(AbstractClientPlayerEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
	{
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	@Override
	protected void renderModel(AbstractClientPlayerEntity entity, MatrixStack matrixStack, int packedLight, IRenderTypeBuffer buffer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{		
		this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
		this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
		if(shouldSit)
			matrixStack.translate(0, -2.5, 0);
		
		boolean flag = this.isBodyVisible(entity);
		boolean flag1 = !flag && !entity.isInvisibleTo(Minecraft.getInstance().player);
		RenderType renderType = ModRenderTypes.getZoanRenderType(this.getTextureLocation(entity));
		if (renderType != null && flag)
		{
			IVertexBuilder ivertexbuilder = buffer.getBuffer(renderType);
			int i = this.getPackedLightCoords(entity, LivingRenderer.getOverlayCoords(entity, partialTicks));
			this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}
	}
	
	@Override
	protected void scale(AbstractClientPlayerEntity entitylivingbase, MatrixStack matrixStack, float partialTickTime)
	{
		matrixStack.scale(1.5f, 1.5f, 1.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractClientPlayerEntity entity)
	{
		return entity.getSkinTextureLocation();
	}

	public static class Factory<T extends PlayerEntity> implements IRenderFactory<T>
	{
		private MorphInfo info;
		private boolean hasSmallHands;
		
		public Factory(MorphInfo info, boolean hasSmallHands)
		{
			this.info = info;
			this.hasSmallHands = hasSmallHands;
		}

		@Override
		public EntityRenderer<? super T> createRenderFor(EntityRendererManager manager)
		{
			OniRenderer renderer = new OniRenderer(manager, this.info, this.hasSmallHands);
			return (EntityRenderer<? super T>) renderer;
		}
	}
}