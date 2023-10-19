package committee.nova.throwableslimeball.common.entity.impl;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class Slimeball extends ThrowableItemProjectile {
    protected int elasticity = getMaxBounceTimes();

    public static Slimeball from(Level l, LivingEntity e, ItemStack stack) {
        final Slimeball ball = new Slimeball(l, e);
        ball.setItem(stack);
        ball.shootFromRotation(e, e.getXRot(), e.getYRot(), 0.0F, 1.5F, 1.0F);
        return ball;
    }

    public Slimeball(EntityType<? extends ThrowableItemProjectile> t, Level l) {
        super(t, l);
    }

    public Slimeball(Level l, double x, double y, double z) {
        this(EntityTypeReference.SLIME_BALL.cast(), l);
        setPos(x, y, z);
    }

    protected Slimeball(EntityType<? extends ThrowableItemProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    private Slimeball(Level l, LivingEntity e) {
        this(EntityTypeReference.SLIME_BALL.cast(), e, l);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id < 3 || id > 5) return;
        final boolean bounce = id != 3;
        level.playLocalSound(getX(), getY(), getZ(), bounce ? getBounceSound() : getDestroySound(),
                SoundSource.BLOCKS, bounce ? .5F : .25F, bounce ? (.2F * elasticity + random.nextFloat() * .1F) : .8F, true);
        ParticleOptions particleoptions = this.getParticle(bounce);
        for (int i = 0; i < 20 - 4 * id; ++i) {
            level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    protected ParticleOptions getParticle(boolean bounce) {
        return ParticleTypes.ITEM_SLIME;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level.isClientSide()) return;
        final Entity e = result.getEntity();
        if (!(e instanceof LivingEntity l)) {
            bounce(e.getMotionDirection().getOpposite(), true);
            return;
        }
        if (canHealOrStrengthen(l)) {
            if (l.getHealth() < l.getMaxHealth()) l.heal(1.0F);
            else if (l instanceof Slime slime) {
                final int size = slime.getSize();
                if (random.nextInt(size + 9) == 0) slime.setSize(size + 1, true);
            }
        } else if (l.getArmorCoverPercentage() < 1.0F) penetrateLivingEntity(l);
        else if (elasticity-- <= 0) destroy();
        else bounce(l.getMotionDirection().getOpposite(), true);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        final BlockState state = level.getBlockState(result.getBlockPos());
        if (state.is(ThrowableSlimeball.BLOCK_STICKY)) destroy();
        else if (state.is(ThrowableSlimeball.BLOCK_ELASTIC)) bounce(result.getDirection(), false);
        else if (elasticity-- <= 0) destroy();
        else bounce(result.getDirection(), true);
    }

    @Override
    public boolean shouldBlockExplode(Explosion pExplosion, BlockGetter pLevel, BlockPos pPos, BlockState pBlockState, float pExplosionPower) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("elasticity", elasticity);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        elasticity = tag.getInt("elasticity");
    }

    protected void bounce(Direction direction, boolean decay) {
        final Direction.Axis axis = direction.getAxis();
        this.setDeltaMovement(this.getDeltaMovement().scale(decay ? getSpeedFactorAfterBounce() : 1.0)
                .multiply(
                        axis.equals(Direction.Axis.X) ? -1.0 : 1.0,
                        axis.equals(Direction.Axis.Y) ? -1.0 : 1.0,
                        axis.equals(Direction.Axis.Z) ? -1.0 : 1.0
                ));
        this.level.broadcastEntityEvent(this, (byte) (decay ? 4 : 5));
    }

    protected void destroy() {
        this.level.broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }

    protected SoundEvent getBounceSound() {
        return SoundEvents.SLIME_BLOCK_HIT;
    }

    protected SoundEvent getDestroySound() {
        return SoundEvents.SLIME_ATTACK;
    }

    protected boolean canHealOrStrengthen(LivingEntity living) {
        return living.getType().is(ThrowableSlimeball.ENTITY_SLIME);
    }

    protected void penetrateLivingEntity(LivingEntity living) {
        living.hurt(getDamageSource(), 1.0F);
        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, elasticity * 20, 0));
    }

    protected DamageSource getDamageSource() {
        return DamageSource.thrown(this, this.getOwner());
    }

    public int getMaxBounceTimes() {
        return CommonConfig.slimeBallMaxBounceTimes.get();
    }

    public double getSpeedFactorAfterBounce() {
        return 1.0 - CommonConfig.slimeBallSpeedDecay.get();
    }
}
