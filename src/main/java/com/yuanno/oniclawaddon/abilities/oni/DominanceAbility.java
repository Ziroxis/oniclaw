package com.yuanno.oniclawaddon.abilities.oni;

import com.yuanno.oniclawaddon.entities.projectiles.oni.DominanceProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.ContinuousAbility;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

import java.util.Random;

public class DominanceAbility extends ContinuousAbility {

    public static final AbilityCore INSTANCE = new AbilityCore.Builder("Dominance", AbilityCategory.RACIAL, DominanceAbility::new)
            .addDescriptionLine("Throws a multitude of slashes around you rendering you immovable.")
            .build();
    public DominanceAbility(AbilityCore abilityCore)
    {
        super(abilityCore);
        this.setMaxCooldown(100);
        this.duringContinuityEvent = this::duringContinuityEvent;
    }

    private void duringContinuityEvent(PlayerEntity player, int timer) {
        if (!player.hasEffect(ModEffects.MOVEMENT_BLOCKED.get()))
            player.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 40, 0));
        int randX = random.nextInt(8);
        int randY = random.nextInt(2);
        int randZ = random.nextInt(8);

        boolean x =RNGboolean();
        boolean y = RNGboolean();

        for (int i = 0; i < 4; i++) {
            DominanceProjectile projectile = new DominanceProjectile(player.level, player, this);

            double posX = player.getX() + (x ? randX : -randX);
            double posY = player.getY() + randY;
            double posZ = player.getZ() + (y ? randZ : -randZ);

            projectile.setPos(posX, posY, posZ);
            projectile.push((projectile.getX() - player.getX()) * 0.3, (projectile.getY() - player.getY()) * 0.1, (projectile.getZ() - player.getZ()) * 0.3);

            player.level.addFreshEntity(projectile);
        }

        if (timer >= 480) {
            this.stopContinuity(player);
        }
    }

    public static boolean RNGboolean()
    {
        Random rd = new Random();
        return rd.nextBoolean();
    }
}
