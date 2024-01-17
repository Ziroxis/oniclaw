package com.yuanno.oniclawaddon.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuanno.oniclawaddon.abilities.dragonclaw.TalonsAbility;
import com.yuanno.oniclawaddon.events.DragonClawPassiveEvents;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.abilities.electro.EleclawAbility;
import xyz.pixelatedw.mineminenomi.abilities.electro.ElectricalShowerAbility;
import xyz.pixelatedw.mineminenomi.abilities.electro.ElectricalTempestaAbility;
import xyz.pixelatedw.mineminenomi.abilities.electro.SulongAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.events.passives.MinkPassiveEvents;

public class TalonsRenderer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M>
{
	public TalonsRenderer(IEntityRenderer renderer)
	{
		super(renderer);
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int pPackedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float partialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
	{
		if(!(this.getParentModel() instanceof IHasArm))
			return;
		
		IAbilityData data = AbilityDataCapability.get(entity);
		MatrixStack matrix = matrixStack;

		Ability talonsAbility = data.getEquippedAbility(TalonsAbility.INSTANCE);
		boolean talonsEnabled = talonsAbility != null && talonsAbility.isContinuous();

		if (talonsEnabled)
		{
			int lightningAmount = 5;
			matrix.pushPose();
			matrix.translate(-0.05, 0, 0);
			DragonClawPassiveEvents.ClientEvents.renderElectro(matrix, (IHasArm)this.getParentModel(), buffer, entity, partialTicks, 0.01f, lightningAmount);
			matrix.popPose();
		}		
	}
}
