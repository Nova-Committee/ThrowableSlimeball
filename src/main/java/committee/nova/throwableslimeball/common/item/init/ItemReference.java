package committee.nova.throwableslimeball.common.item.init;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.item.impl.MagmaCreamItem;
import committee.nova.throwableslimeball.common.item.impl.SlimeballItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

public enum ItemReference implements Supplier<Item> {
    SLIME_BALL(SlimeballItem::new),
    MAGMA_CREAM(MagmaCreamItem::new);

    ItemReference(Supplier<Item> sup) {
        reg = ThrowableSlimeball.ITEMS_VANILLA.register(this.name().toLowerCase(Locale.ROOT), sup);
    }

    private final RegistryObject<Item> reg;

    @Override
    public Item get() {
        return reg.get();
    }

    public static void init() {
    }
}
