package com.yuanno.oniclawaddon.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuanno.oniclawaddon.Main;
import com.yuanno.oniclawaddon.abilities.dragonclaw.TalonsAbility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.electro.EleclawAbility;
import xyz.pixelatedw.mineminenomi.abilities.electro.ElectricalShowerAbility;
import xyz.pixelatedw.mineminenomi.abilities.electro.ElectricalTempestaAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityOverlay;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.MorphHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class DragonClawPassiveEvents {

    @Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents
    {
        public static final Color LIGHTNING_COLOR = new Color(255, 180, 180, 0);
        private static ArrayList<Long> lightningValues1 = new ArrayList<>();
        private static ArrayList<Long> lightningValues2 = new ArrayList<>();

        public static void renderElectro(MatrixStack matrix, IHasArm model, IRenderTypeBuffer buffer, LivingEntity entity, float partialRenderTick, float scale, int lightningAmount)
        {
            for (int i = 0; i < lightningAmount; i++)
            {
                for (int z = 1; z < 5; z++)
                {
                    matrix.pushPose();
                    {
                        Color finalColor = (i == 1 && Math.random() > 0.6 && !Minecraft.getInstance().isPaused()) ? Color.white : LIGHTNING_COLOR;
                        model.translateToHand(entity.getMainArm(), matrix);
                        matrix.scale(scale * 0.8f, scale * 1.4f, scale * 0.75f);
                        matrix.translate((entity.getMainArm() == HandSide.LEFT ? 1 : -1), 0, 0);
                        drawMinkLightning(getRandomLightningLong(lightningValues1, entity, 2, i + z - 1), matrix, buffer, 3, 6, 8, finalColor, 255);
                    }
                    matrix.popPose();
                }
            }
        }

        public static long getRandomLightningLong(ArrayList<Long> oldValues, Entity entity, int ticks, int size)
        {
            boolean validValue = oldValues.size() > size;
            if (Minecraft.getInstance().isPaused() || entity == null)
                return validValue ? oldValues.get(size) : 0;

            if (entity.tickCount % ticks == 0)
            {
                long value = entity.level.random.nextLong();
                if (validValue)
                    oldValues.set(size, value);
                else
                    oldValues.add(value);
                return value;
            }

            return validValue ? oldValues.get(size) : 0;
        }

        @SubscribeEvent
        public static void onHandRendering(RenderHandEvent event)
        {
            if (event.getHand() != Hand.MAIN_HAND || !event.getItemStack().isEmpty())
                return;

            Minecraft mc = Minecraft.getInstance();
            ClientPlayerEntity player = mc.player;

            IAbilityData abilityProps = AbilityDataCapability.get(player);

            Ability talonsAbility = abilityProps.getEquippedAbility(TalonsAbility.INSTANCE);
            boolean talonsEnabled = talonsAbility != null && talonsAbility.isContinuous();


            if (talonsEnabled)
            {
                int lightningAmount = 5;
                // event.setCanceled(true);

                boolean flag = player.getMainArm() != HandSide.LEFT;

                AbilityOverlay overlay = AbilityHelper.getCurrentOverlay(player);
                MorphHelper.renderLimbFirstPerson(event.getMatrixStack(), event.getBuffers(), event.getLight(), event.getEquipProgress(), event.getSwingProgress(), player.getMainArm(), overlay, null);

                event.getMatrixStack().pushPose();
                {
                    float f = flag ? 1.0F : -1.0F;
                    float f1 = MathHelper.sqrt(event.getSwingProgress());
                    float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
                    float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
                    float f4 = -0.4F * MathHelper.sin(event.getSwingProgress() * (float) Math.PI);
                    event.getMatrixStack().translate(f * (f2 + 0.64000005F), f3 + -0.6F + event.getEquipProgress() * -0.6F, f4 + -0.71999997F);
                    event.getMatrixStack().mulPose(new Quaternion(Vector3f.YP, f * 45.0F, true));
                    float f5 = MathHelper.sin(event.getSwingProgress() * event.getSwingProgress() * (float) Math.PI);
                    float f6 = MathHelper.sin(f1 * (float) Math.PI);
                    event.getMatrixStack().mulPose(new Quaternion(Vector3f.YP, f * f6 * 70.0F, true));
                    event.getMatrixStack().mulPose(new Quaternion(Vector3f.ZP, f * f5 * -20.0F, true));

                    event.getMatrixStack().translate(f * -1.0F, 3.6F, 3.5F);
                    event.getMatrixStack().mulPose(new Quaternion(Vector3f.ZP, f * 120.0F, true));
                    event.getMatrixStack().mulPose(new Quaternion(Vector3f.XP, 200.0F, true));
                    event.getMatrixStack().mulPose(new Quaternion(Vector3f.YP, f * -135.0F, true));
                    event.getMatrixStack().translate(f * 5.6F, 0.0F, 0.0F);

                    for (int i = 0; i < lightningAmount; i++)
                    {
                        for (int z = 1; z < 5; z++)
                        {
                            Color finalColor = (i == 1 && Math.random() > 0.6 && !Minecraft.getInstance().isPaused()) ? Color.red : LIGHTNING_COLOR;
                            event.getMatrixStack().pushPose();
                            event.getMatrixStack().scale(0.01F, 0.035F, 0.01F);
                            event.getMatrixStack().translate(flag ? -40 : 40, -24, 0);
                            drawMinkLightning(getRandomLightningLong(lightningValues2, player, Math.random() > 0.5 ? 2 : 3, i + z - 1), event.getMatrixStack(), event.getBuffers(), 4, 6, 6, finalColor, 255);
                            event.getMatrixStack().popPose();
                        }
                    }

                }
                event.getMatrixStack().popPose();
            }
        }

        public static void drawMinkLightning(long seed, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, float length, int size, Color color, int offset)
        {
            drawMinkLightning(seed, matrixStackIn, bufferIn, packedLightIn, length, size, color, offset, 5);
        }

        public static void drawMinkLightning(long seed, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, float length, int size, Color color, int offset, int layers)
        {
            float[] afloat = new float[256], afloat1 = new float[256];
            float f = 0.0F, f1 = 0.0F;
            IVertexBuilder builder = bufferIn.getBuffer(ModRenderTypes.ENERGY);
            Matrix4f matrix = matrixStackIn.last().pose();
            Random randPrev = new Random(seed), rand = new Random(seed);
            offset = Math.min(offset, 255);

            // 7 - default offset
            for (int i = offset; i >= 0; --i)
            {
                afloat[i] = f;
                afloat1[i] = f1;
                f += randPrev.nextInt(11) - 5;
                f1 += randPrev.nextInt(11) - 5;
            }

            for (int i = 0; i < 3; ++i)
            {
                int l = 7;
                int i1 = 0;
                if (i > 0)
                {
                    l = 7 - i;
                    i1 = l - 2;
                }

                float f2 = afloat[l] - f;
                float f3 = afloat1[l] - f1;

                for (int j1 = l; j1 >= i1; --j1)
                {
                    float f4 = f2;
                    float f5 = f3;
                    f2 += rand.nextInt(11) - 5;
                    f3 += rand.nextInt(11) - 5;

                    Color color1 = color;
                    for (int j = 1; j <= layers; j++)
                    {
                        float f6 = 0.1F + size * (0.015F * j);
                        color = (Math.round(layers / 3f) > j) ? new Color(255, 255, 255, color1.getAlpha()) : color1;
                        drawLightningQuad(matrix, builder, f2, f3, j1, length, f4, f5, f6, false, false, true, false, packedLightIn, color);
                        drawLightningQuad(matrix, builder, f2, f3, j1, length, f4, f5, f6, true, false, true, true, packedLightIn, color);
                        drawLightningQuad(matrix, builder, f2, f3, j1, length, f4, f5, f6, true, true, false, true, packedLightIn, color);
                        drawLightningQuad(matrix, builder, f2, f3, j1, length, f4, f5, f6, false, true, false, false, packedLightIn, color);
                    }
                }
            }
        }

        private static void drawLightningQuad(Matrix4f matrix4f, IVertexBuilder builder, float x, float z, int y, float endY, float x2, float z2, float additional, boolean a, boolean c, boolean b, boolean d, int packedLight, Color color)
        {
            builder.vertex(matrix4f, x + (a ? additional : -additional), y * endY, z + (c ? additional : -additional)).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F).uv2(packedLight).endVertex();
            builder.vertex(matrix4f, x2 + (a ? additional : -additional), (y + 1) * endY, z2 + (c ? additional : -additional)).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F).uv2(packedLight).endVertex();
            builder.vertex(matrix4f, x2 + (b ? additional : -additional), (y + 1) * endY, z2 + (d ? additional : -additional)).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F).uv2(packedLight).endVertex();
            builder.vertex(matrix4f, x + (b ? additional : -additional), y * endY, z + (d ? additional : -additional)).color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F).uv2(packedLight).endVertex();
        }
    }
}
