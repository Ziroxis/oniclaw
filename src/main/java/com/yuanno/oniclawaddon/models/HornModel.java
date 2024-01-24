package com.yuanno.oniclawaddon.models;// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;

public class HornModel<T extends LivingEntity> extends MorphModel<T> {
	private final ModelRenderer Head;
	private final ModelRenderer group2;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer group;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;

	public HornModel() {
		super(1);
		texWidth = 16;
		texHeight = 16;

		Head = new ModelRenderer(this);
		Head.setPos(0.0F, 0.0F, 0.0F);
		

		group2 = new ModelRenderer(this);
		group2.setPos(5.425F, -10.1462F, -3.4907F);
		Head.addChild(group2);
		setRotationAngle(group2, 0.0F, -0.0873F, 0.0F);
		group2.texOffs(0, 0).addBox(-1.025F, -3.925F, -2.875F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		group2.texOffs(0, 0).addBox(-0.925F, -6.025F, -2.675F, 1.0F, 2.0F, 1.0F, 0.0F, true);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(0.0F, 0.0F, 0.0F);
		group2.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.3927F, 0.0F, 0.0F);
		cube_r1.texOffs(0, 0).addBox(-1.125F, -2.2575F, -2.2967F, 2.0F, 5.0F, 2.0F, 0.0F, true);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(0.0F, 0.0F, 0.0F);
		group2.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.7854F, 0.0F, 0.0F);
		cube_r2.texOffs(0, 0).addBox(-1.325F, 1.979F, -3.5567F, 2.0F, 5.0F, 2.0F, 0.0F, false);

		group = new ModelRenderer(this);
		group.setPos(-5.3F, -5.3712F, -1.5657F);
		Head.addChild(group);
		setRotationAngle(group, 0.0F, 0.1396F, 0.0F);
		group.texOffs(0, 0).addBox(-0.8F, -11.0F, -4.6F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		group.texOffs(0, 0).addBox(-1.1F, -8.9F, -4.8F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(-0.075F, -4.975F, -1.925F);
		group.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.7854F, 0.0F, 0.0F);
		cube_r3.texOffs(1, 1).addBox(-1.325F, 1.979F, -3.5567F, 2.0F, 5.0F, 2.0F, 0.0F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(-0.075F, -4.975F, -1.925F);
		group.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.3927F, 0.0F, 0.0F);
		cube_r4.texOffs(0, 0).addBox(-1.125F, -2.2575F, -2.2967F, 2.0F, 5.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		this.Head.copyFrom(this.head);
		this.Head.render(matrixStack, buffer, packedLight, packedOverlay, 1, 1, 1, 1);

	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderFirstPersonArm(MatrixStack matrixStack, IVertexBuilder vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HandSide side) {}

	@Override
	public void renderFirstPersonLeg(MatrixStack matrixStack, IVertexBuilder vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HandSide side) {}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}