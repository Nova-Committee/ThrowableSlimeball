package committee.nova.throwableslimeball.common.entity.impl;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class Slimeball extends ThrowableItemProjectile {
    protected int elasticity = getMaxBounceTimes();

    public static Slimeball create(ServerLevel level, LivingEntity shooter, ItemStack stack) {
        return new Slimeball(EntityTypeReference.SLIME_BALL.get(), shooter, level, stack);
    }

    public Slimeball(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public Slimeball(Level level, double x, double y, double z, ItemStack stack) {
        this(EntityTypeReference.SLIME_BALL.get(), x, y, z, level, stack);
    }

    public Slimeball(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level level, ItemStack stack) {
        super(type, x, y, z, level, stack);
    }

    public Slimeball(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level level, ItemStack stack) {
        super(type, shooter, level, stack);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id < 3 || id > 5) return;
        final boolean bounce = id != 3;
        level().playLocalSound(getX(), getY(), getZ(), bounce ? getBounceSound() : getDestroySound(),
                SoundSource.BLOCKS, bounce ? .5F : .25F, bounce ? (.2F * elasticity + random.nextFloat() * .1F) : .8F, true);
        ParticleOptions particle = this.getParticle(bounce);
        for (int i = 0; i < 20 - 4 * id; ++i) {
            this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    protected ParticleOptions getParticle(boolean bounce) {
        return ParticleTypes.ITEM_SLIME;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level().isClientSide()) return;
        final Entity entity = result.getEntity();
        if (!(entity instanceof LivingEntity living)) {
            bounce(entity.getMotionDirection().getOpposite(), true);
            return;
        }
        if (canHealOrStrengthen(living)) {
            if (living.getHealth() < living.getMaxHealth()) living.heal(1.0F);
            else if (living instanceof Slime slime) {
                final int size = slime.getSize();
                if (random.nextInt(size + 9) == 0) slime.setSize(size + 1, true);
            }
        } else if (living.getArmorCoverPercentage() < 1.0F) penetrateLivingEntity(living);
        else if (elasticity-- <= 0) destroy();
        else bounce(living.getMotionDirection().getOpposite(), true);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        final BlockState state = level().getBlockState(result.getBlockPos());
        if (state.is(ThrowableSlimeball.BLOCK_STICKY)) destroy();
        else if (state.is(ThrowableSlimeball.BLOCK_ELASTIC)) bounce(result.getDirection(), false);
        else if (elasticity-- <= 0) destroy();
        else bounce(result.getDirection(), true);
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float explosionPower) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("elasticity", elasticity);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        elasticity = input.getIntOr("elasticity", 0);
    }

    protected void bounce(Direction direction, boolean decay) {
        final Direction.Axis axis = direction.getAxis();
        this.setDeltaMovement(this.getDeltaMovement().scale(decay ? getSpeedFactorAfterBounce() : 1.0)
                .multiply(
                        axis.equals(Direction.Axis.X) ? -1.0 : 1.0,
                        axis.equals(Direction.Axis.Y) ? -1.0 : 1.0,
                        axis.equals(Direction.Axis.Z) ? -1.0 : 1.0
                ));
        this.level().broadcastEntityEvent(this, (byte) (decay ? 4 : 5));
    }

    protected void destroy() {
        this.level().broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }

    protected SoundEvent getBounceSound() {
        return SoundEvents.SLIME_BLOCK_HIT;
    }

    protected SoundEvent getDestroySound() {
        return SoundEvents.SLIME_ATTACK;
    }

    protected boolean canHealOrStrengthen(LivingEntity living) {
        return living.is(ThrowableSlimeball.ENTITY_SLIME);
    }

    protected void penetrateLivingEntity(LivingEntity living) {
        if (living.level() instanceof ServerLevel serverLevel) {
            living.hurtServer(serverLevel, getDamageSource(), 1.0F);
        }
        living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, elasticity * 20, 0));
    }

    protected DamageSource getDamageSource() {
        return this.damageSources().thrown(this, this.getOwner());
    }

    public int getMaxBounceTimes() {
        return CommonConfig.slimeBallMaxBounceTimes.get();
    }

    public double getSpeedFactorAfterBounce() {
        return 1.0 - CommonConfig.slimeBallSpeedDecay.get();
    }
}
