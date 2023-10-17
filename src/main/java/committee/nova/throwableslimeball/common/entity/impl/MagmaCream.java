package committee.nova.throwableslimeball.common.entity.impl;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class MagmaCream extends Slimeball {
    public MagmaCream(EntityType<? extends ProjectileItemEntity> t, World l) {
        super(t, l);
    }

    private MagmaCream(World l, LivingEntity e) {
        super(EntityTypeReference.MAGMA_CREAM.cast(), e, l);
    }

    public static MagmaCream from(World l, LivingEntity e, ItemStack stack) {
        final MagmaCream ball = new MagmaCream(l, e);
        ball.setItem(stack);
        ball.shootFromRotation(e, e.xRot, e.yRot, 0.0F, 1.5F, 1.0F);
        return ball;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.MAGMA_CREAM;
    }

    @Override
    protected IParticleData getParticle(boolean bounce) {
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
