package committee.nova.throwableslimeball;

import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import committee.nova.throwableslimeball.common.item.init.ItemReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ThrowableSlimeball.MODID)
public class ThrowableSlimeball {
    public static final String MODID = "throwable_slimeball";

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public ThrowableSlimeball() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemReference.init();
        EntityTypeReference.init();
        ITEMS_VANILLA.register(bus);
        ENTITIES.register(bus);
    }
}
