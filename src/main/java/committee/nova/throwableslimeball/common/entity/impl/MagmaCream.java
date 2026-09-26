package committee.nova.throwableslimeball.common.entity.impl;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MagmaCream extends Slimeball {
    public static MagmaCream create(ServerLevel level, LivingEntity shooter, ItemStack stack) {
        return new MagmaCream(EntityTypeReference.MAGMA_CREAM.get(), shooter, level, stack);
    }

    public MagmaCream(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public MagmaCream(Level level, double x, double y, double z, ItemStack stack) {
        super(EntityTypeReference.MAGMA_CREAM.get(), x, y, z, level, stack);
    }

    public MagmaCream(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level level, ItemStack stack) {
        super(type, shooter, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.MAGMA_CREAM;
    }

    @Override
    protected ParticleOptions getParticle(boolean bounce) {
        return bounce ? ParticleTypes.ITEM_SLIME : ParticleTypes.LAVA;
    }

    @Override
    protected boolean canHealOrStrengthen(LivingEntity living) {
        return living.is(ThrowableSlimeball.ENTITY_MAGMA_CUBE);
    }

    @Override
    protected SoundEvent getDestroySound() {
        return SoundEvents.GENERIC_EXTINGUISH_FIRE;
    }

    @Override
    protected void penetrateLivingEntity(LivingEntity living) {
        super.penetrateLivingEntity(living);
        living.setRemainingFireTicks(living.getRemainingFireTicks() + elasticity * 2);
    }

    @Override
    public int getMaxBounceTimes() {
        return CommonConfig.magmaCreamMaxBounceTimes.get();
    }

    @Override
    public double getSpeedFactorAfterBounce() {
        return CommonConfig.magmaCreamSpeedDecay.get();
    }
}
