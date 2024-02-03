package com.yuanno.oniclawaddon.events;

import com.google.common.base.Strings;
import com.yuanno.oniclawaddon.Main;
import com.yuanno.oniclawaddon.models.OniMorphInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityOverlay;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.MorphHelper;
import xyz.pixelatedw.mineminenomi.config.ClientConfig;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;
import xyz.pixelatedw.mineminenomi.renderers.morphs.ZoanMorphRenderer;

public class OniClientPassiveEvents {

    @Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
    public static class Client
    {
        @SubscribeEvent
        public static void onEntityRendered(RenderLivingEvent.Pre event)
        {
            if (!(event.getEntity() instanceof AbstractClientPlayerEntity))
                return;
            IEntityStats entityStats = EntityStatsCapability.get(event.getEntity());
            if (!entityStats.getRace().equals("oni"))
                return;
            event.setCanceled(true);
            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) event.getEntity();

            OniMorphInfo morphInfo = new OniMorphInfo();
            ZoanMorphRenderer renderer = (ZoanMorphRenderer) morphInfo.getRendererFactory(player).createRenderFor(Minecraft.getInstance().getEntityRenderDispatcher());
            renderer.render(player, player.yRot, event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight());
        }

        @SubscribeEvent
        public static void onHandRendering(RenderHandEvent event)
        {
            if(event.getHand() != Hand.MAIN_HAND)
                return;

            PlayerEntity player = Minecraft.getInstance().player;
            if(player == null)
                return;

            IEntityStats props = EntityStatsCapability.get(player);
            if (!props.getRace().equals("oni"))
                return;
            boolean hasEmptyHand = player.getMainHandItem().isEmpty();

            OniMorphInfo morphInfo = new OniMorphInfo();
            AbilityOverlay overlay = AbilityHelper.getCurrentOverlay(player);


            boolean isBlackLeg = props.isBlackLeg() && hasEmptyHand && (props.isInCombatMode() || ClientConfig.INSTANCE.isBlackLegAlwaysUp());
            boolean isOverlay = hasEmptyHand;

            if (isOverlay || isBlackLeg)
            {
                event.setCanceled(true);
                MorphHelper.renderLimbFirstPerson(event.getMatrixStack(), event.getBuffers(), event.getLight(), event.getEquipProgress(), event.getSwingProgress(), player.getMainArm(), overlay, morphInfo);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Main.MOD_ID)
    public static class Common
    {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onZoanSizeChange(EntityEvent.Size event)
        {
            if (!(event.getEntity() instanceof PlayerEntity))
                return;

            PlayerEntity player = (PlayerEntity) event.getEntity();
            IEntityStats entityStats = EntityStatsCapability.get(player);
            if (!entityStats.getRace().equals("oni"))
                return;
            Pose pose = player.getPose();
            float eyeHeight = player.getDimensions(pose).height * 0.9F;

            OniMorphInfo morphInfo = new OniMorphInfo();


            if(morphInfo.getEyeHeight() <= 0)
                    return;


            eyeHeight = (float) (1.62 * (morphInfo.getEyeHeight() / 1.75));
            if(eyeHeight < 0.22F)
                eyeHeight = 0.22F;



            if(player.isCrouching())
            {
                eyeHeight = eyeHeight - 0.3F;
                eyeHeight = (float) Math.max(0.3, eyeHeight);
            }
            event.setNewEyeHeight(eyeHeight);
        }

    }


}
