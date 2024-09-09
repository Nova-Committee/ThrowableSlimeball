package committee.nova.throwableslimeball.common.item.impl;

import committee.nova.throwableslimeball.common.entity.impl.Slimeball;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class SlimeballProxy implements ProjectileItem {
    private static SlimeballProxy instance;

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            final Slimeball slimeball = Slimeball.from(level, player, itemstack);
            level.addFreshEntity(slimeball);
        }
        player.awardStat(Stats.ITEM_USED.get(Items.SLIME_BALL));
        if (!player.getAbilities().instabuild) itemstack.shrink(1);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return Util.make(new Slimeball(level, position.x(), position.y(), position.z()), s -> s.setItem(itemStack));
    }

    public static SlimeballProxy getInstance() {
        if (instance == null) {
            instance = new SlimeballProxy();
        }
        return instance;
    }
}
