package com.yuanno.oniclawaddon.init;

import com.yuanno.oniclawaddon.particles.OverdriveParticleEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import xyz.pixelatedw.mineminenomi.particles.effects.ParticleEffect;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

public class ModParticleEffects {
    public static final RegistryObject<ParticleEffect<?>> OVERDRIVE = WyRegistry.registerParticleEffect("Oni Overdrive", OverdriveParticleEffect::new);
    public static void register(IEventBus eventBus) {
    }
}
