package committee.nova.throwableslimeball.fabric.mixin;

import committee.nova.throwableslimeball.fabric.common.item.proxy.MagmaCreamItemProxy;
import committee.nova.throwableslimeball.fabric.common.item.proxy.SlimeballItemProxy;
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
            cir.setReturnValue(SlimeballItemProxy.use(thisItem, level, player, interactionHand));
        else if (thisItem.equals(Items.MAGMA_CREAM))
            cir.setReturnValue(MagmaCreamItemProxy.use(thisItem, level, player, interactionHand));
    }
}
