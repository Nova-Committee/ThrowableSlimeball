package committee.nova.throwableslimeball.common.entity.impl;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MagmaCream extends Slimeball {
    public MagmaCream(EntityType<? extends ThrowableItemProjectile> t, Level l) {
        super(t, l);
    }

    private MagmaCream(Level l, LivingEntity e) {
        super(EntityTypeReference.MAGMA_CREAM.cast(), e, l);
    }

    public static MagmaCream from(Level l, LivingEntity e, ItemStack stack) {
        final MagmaCream ball = new MagmaCream(l, e);
        ball.setItem(stack);
        ball.shootFromRotation(e, e.getXRot(), e.getYRot(), 0.0F, 1.5F, 1.0F);
        return ball;
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
        return living.getType().is(ThrowableSlimeball.ENTITY_MAGMA_CUBE);
    }

    @Override
    protected SoundEvent getDestroySound() {
        return SoundEvents.GENERIC_EXTINGUISH_FIRE;
    }

    @Override
    protected void penetrateLivingEntity(LivingEntity living) {
        super.penetrateLivingEntity(living);
        living.setSecondsOnFire(living.getRemainingFireTicks() + elasticity * 2);
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
