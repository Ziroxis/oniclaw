package com.yuanno.oniclawaddon.entities.projectiles.oni;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.events.SetOnFireEvent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.particles.effects.CommonExplosionParticleEffect;

public class DominanceProjectile extends AbilityProjectileEntity {
    public DominanceProjectile(EntityType type, World world)
    {
        super(type, world);
    }

    public DominanceProjectile(World world, LivingEntity thrower, Ability ability)
    {
        super(OniProjectiles.DOMINANCE_SLASH.get(), world, thrower, ability);

        this.setDamage(20);
        super.setMaxLife(40);
        super.setCanGetStuckInGround();
        super.setPassThroughEntities();

    }



}
