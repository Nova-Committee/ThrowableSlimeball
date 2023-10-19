package committee.nova.throwableslimeball.common.entity.impl;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class Slimeball extends ProjectileItemEntity {
    protected int elasticity = getMaxBounceTimes();

    public static Slimeball from(World l, LivingEntity e, ItemStack stack) {
        final Slimeball ball = new Slimeball(l, e);
        ball.setItem(stack);
        ball.shootFromRotation(e, e.xRot, e.yRot, 0.0F, 1.5F, 1.0F);
        return ball;
    }

    public Slimeball(EntityType<? extends ProjectileItemEntity> t, World l) {
        super(t, l);
    }

    public Slimeball(World l, double x, double y, double z) {
        this(EntityTypeReference.SLIME_BALL.cast(), l);
        setPos(x, y, z);
    }

    protected Slimeball(EntityType<? extends ProjectileItemEntity> pEntityType, LivingEntity pShooter, World pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    private Slimeball(World l, LivingEntity e) {
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
                SoundCategory.BLOCKS, bounce ? .5F : .25F, bounce ? (.2F * elasticity + random.nextFloat() * .1F) : .8F, true);
        IParticleData particleoptions = this.getParticle(bounce);
        for (int i = 0; i < 20 - 4 * id; ++i) {
            level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    protected IParticleData getParticle(boolean bounce) {
        return ParticleTypes.ITEM_SLIME;
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        super.onHitEntity(result);
        if (level.isClientSide()) return;
        final Entity e = result.getEntity();
        if (!(e instanceof LivingEntity)) {
            bounce(e.getMotionDirection().getOpposite(), true);
            return;
        }
        final LivingEntity l = (LivingEntity) e;
        if (canHealOrStrengthen(l)) {
            if (l.getHealth() < l.getMaxHealth()) l.heal(1.0F);
            else if (l instanceof SlimeEntity) {
                final SlimeEntity slime = ((SlimeEntity) l);
                final int size = slime.getSize();
                if (random.nextInt(size + 9) == 0) slime.setSize(size + 1, true);
            }
        } else if (l.getArmorCoverPercentage() < 1.0F) penetrateLivingEntity(l);
        else if (elasticity-- <= 0) destroy();
        else bounce(l.getMotionDirection().getOpposite(), true);
        this.remove();
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        super.onHitBlock(result);
        final BlockState state = level.getBlockState(result.getBlockPos());
        if (state.is(ThrowableSlimeball.BLOCK_STICKY)) destroy();
        else if (state.is(ThrowableSlimeball.BLOCK_ELASTIC)) bounce(result.getDirection(), false);
        else if (elasticity-- <= 0) destroy();
        else bounce(result.getDirection(), true);
    }


    @Override
    public boolean shouldBlockExplode(Explosion pExplosion, IBlockReader pLevel, BlockPos pPos, BlockState pBlockState, float pExplosionPower) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("elasticity", elasticity);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
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
        this.remove();
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
        living.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, elasticity * 20, 0));
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
