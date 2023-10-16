package committee.nova.throwableslimeball.common.entity.impl;

import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

@MethodsReturnNonnullByDefault
public class Slimeball extends ThrowableItemProjectile {
    private int elasticity = 3;

    public Slimeball(EntityType<? extends ThrowableItemProjectile> t, Level l) {
        super(t, l);
    }

    public Slimeball(Level l, LivingEntity e) {
        super(EntityTypeReference.SLIME_BALL.cast(), e, l);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id < 3 || id > 5) return;
        final boolean bounce = id != 3;
        level().playLocalSound(getX(), getY(), getZ(), bounce ? SoundEvents.SLIME_SQUISH_SMALL : SoundEvents.SLIME_ATTACK,
                SoundSource.BLOCKS, bounce ? .5F : .25F, .2F * elasticity + random.nextFloat() * .1F, true);
        ParticleOptions particleoptions = this.getParticle();
        for (int i = 0; i < 20 - 4 * id; ++i) {
            this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    private ParticleOptions getParticle() {
        return ParticleTypes.ITEM_SLIME;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!(result.getEntity() instanceof LivingEntity e)) {
            bounce(this.getMotionDirection().getOpposite(), true);
            return;
        }
        final boolean isSlime = e instanceof Slime;
        if (isSlime) e.heal(1.0F);
        else {
            e.hurt(this.damageSources().thrown(this, this.getOwner()), 1.0F);
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, elasticity * 20, 0));
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        final BlockState state = level().getBlockState(result.getBlockPos());
        if (state.is(Blocks.SLIME_BLOCK)) bounce(result.getDirection(), false);
        else if (state.is(Blocks.HONEY_BLOCK) || elasticity-- <= 0) destroy();
        else bounce(result.getDirection(), true);
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

    private void bounce(Direction direction, boolean decay) {
        final Direction.Axis axis = direction.getAxis();
        this.setDeltaMovement(this.getDeltaMovement().scale(decay ? .7 : 1.0)
                .multiply(
                        axis.equals(Direction.Axis.X) ? -1.0 : 1.0,
                        axis.equals(Direction.Axis.Y) ? -1.0 : 1.0,
                        axis.equals(Direction.Axis.Z) ? -1.0 : 1.0
                ));
        this.level().broadcastEntityEvent(this, (byte) (decay ? 4 : 5));
    }

    private void destroy() {
        this.level().broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }
}
