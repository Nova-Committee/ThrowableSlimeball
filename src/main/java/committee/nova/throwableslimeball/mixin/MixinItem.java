package committee.nova.throwableslimeball.mixin;

import committee.nova.throwableslimeball.common.item.impl.MagmaCreamProxy;
import committee.nova.throwableslimeball.common.item.impl.SlimeballProxy;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void inject$use(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        final Item thisItem = (Item) (Object) this;
        if (thisItem.equals(Items.SLIME_BALL))
            cir.setReturnValue(SlimeballProxy.getInstance().use(level, player, interactionHand));
        else if (thisItem.equals(Items.MAGMA_CREAM))
            cir.setReturnValue(MagmaCreamProxy.getInstance().use(level, player, interactionHand));
    }
}