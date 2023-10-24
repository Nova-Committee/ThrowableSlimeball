package committee.nova.throwableslimeball.common.item.impl;

import committee.nova.throwableslimeball.common.entity.impl.MagmaCream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class MagmaCreamItem extends Item {
    public MagmaCreamItem() {
        super(new Properties().tab(ItemGroup.TAB_BREWING).stacksTo(64));
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            final MagmaCream slimeball = MagmaCream.from(level, player, itemstack);
            level.addFreshEntity(slimeball);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.instabuild) itemstack.shrink(1);
        return ActionResult.sidedSuccess(itemstack, level.isClientSide());
    }
}
