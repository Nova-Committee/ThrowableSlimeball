package committee.nova.throwableslimeball.fabric.common.item.proxy;

import committee.nova.throwableslimeball.fabric.common.entity.impl.Slimeball;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SlimeballItemProxy {
    public static InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            final Slimeball slimeball = Slimeball.from(level, player, itemstack);
            level.addFreshEntity(slimeball);
        }
        player.awardStat(Stats.ITEM_USED.get(item));
        if (!player.getAbilities().instabuild) itemstack.shrink(1);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
