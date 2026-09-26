package committee.nova.throwableslimeball.common.item.impl;

import committee.nova.throwableslimeball.common.entity.impl.Slimeball;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class SlimeballProxy implements ProjectileItem {
    private static SlimeballProxy instance;

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (level instanceof ServerLevel serverLevel) {
            Projectile.spawnProjectileFromRotation(Slimeball::create, serverLevel, itemstack, player, 0.0F, 1.5F, 1.0F);
        }
        player.awardStat(Stats.ITEM_USED.get(Items.SLIME_BALL));
        itemstack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return new Slimeball(level, position.x(), position.y(), position.z(), itemStack);
    }

    public static SlimeballProxy getInstance() {
        if (instance == null) {
            instance = new SlimeballProxy();
        }
        return instance;
    }
}
