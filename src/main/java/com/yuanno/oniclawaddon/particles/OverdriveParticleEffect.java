package com.yuanno.oniclawaddon.particles;

import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.init.ModParticleTypes;
import xyz.pixelatedw.mineminenomi.particles.data.SimpleParticleData;
import xyz.pixelatedw.mineminenomi.particles.effects.ParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class OverdriveParticleEffect extends ParticleEffect<ParticleEffect.NoDetails>
{
    @Override
    public void spawn(Entity entity, World world, double posX, double posY, double posZ, NoDetails details)
    {
        for (int i = 0; i < 8; i++)
        {
            double offsetX = WyHelper.randomDouble() / 1.25;
            double offsetY = WyHelper.randomDouble() / 1.25;
            double offsetZ = WyHelper.randomDouble() / 1.25;

            SimpleParticleData particleData = new SimpleParticleData(ModParticleTypes.GORO.get());
            particleData.setColor(128, 0, 128);

            world.addParticle(particleData, true, posX + offsetX, (posY + 1) + offsetY, posZ + offsetZ, 0, 0, 0);
        }
    }
}