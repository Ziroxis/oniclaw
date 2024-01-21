package com.yuanno.oniclawaddon.entities.projectiles.dragonclaw;

import net.minecraft.entity.Entity;
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

public class FlamingSlashProjectile extends AbilityProjectileEntity {
    public FlamingSlashProjectile(EntityType type, World world)
    {
        super(type, world);
    }

    public FlamingSlashProjectile(World world, LivingEntity thrower, Ability ability)
    {
        super(DragonClawProjectiles.FLAMING_SLASH.get(), world, thrower, ability);

        this.setDamage(80);
        super.setMaxLife(40);
        super.setCanGetStuckInGround();
        super.setPassThroughEntities();
        this.onEntityImpactEvent = this::onEntityImpactEvent;
        super.onBlockImpactEvent = this::onBlockImpactEvent;

    }

    private void onEntityImpactEvent(LivingEntity hitEntity)
    {
        SetOnFireEvent event = new SetOnFireEvent(this.getThrower(), hitEntity, 4);
        if (!MinecraftForge.EVENT_BUS.post(event))
            hitEntity.setSecondsOnFire(8);
    }
    private void onBlockImpactEvent(BlockPos hit) {
        ExplosionAbility explosion = AbilityHelper.newExplosion(super.getThrower(), super.level, super.getX(), super.getY(), super.getZ(), 4.0F);
        explosion.setStaticDamage(20.0F);
        explosion.setSmokeParticles(new CommonExplosionParticleEffect(4));
        explosion.setFireAfterExplosion(true);
        explosion.doExplosion();
    }
}
