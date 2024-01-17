package com.yuanno.oniclawaddon.entities.projectiles.dragonclaw;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.events.SetOnFireEvent;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

public class FlamingSlashProjectile extends AbilityProjectileEntity {
    public FlamingSlashProjectile(EntityType type, World world)
    {
        super(type, world);
    }

    public FlamingSlashProjectile(World world, LivingEntity thrower, Ability ability)
    {
        super(DragonClawProjectiles.FLAMING_SLASH.get(), world, thrower, ability);

        this.setDamage(80);
        this.onEntityImpactEvent = this::onEntityImpactEvent;

    }

    private void onEntityImpactEvent(LivingEntity hitEntity)
    {
        SetOnFireEvent event = new SetOnFireEvent(this.getThrower(), hitEntity, 4);
        if (!MinecraftForge.EVENT_BUS.post(event))
            hitEntity.setSecondsOnFire(4);
    }
}
