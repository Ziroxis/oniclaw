package com.yuanno.oniclawaddon.entities.projectiles.items;

import com.yuanno.oniclawaddon.entities.projectiles.dragonclaw.DragonClawProjectiles;
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

public class DivineDepartureProjectile extends AbilityProjectileEntity {
    public DivineDepartureProjectile(EntityType type, World world)
    {
        super(type, world);
    }

    public DivineDepartureProjectile(World world, LivingEntity thrower, Ability ability)
    {
        super(ItemProjectiles.DIVINE_DEPARTURE.get(), world, thrower, ability);

        this.setDamage(80);
        super.setMaxLife(40);
        super.setPassThroughBlocks();
        super.setKnockbackStrength(5);

    }



}
