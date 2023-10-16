package committee.nova.throwableslimeball;

import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import committee.nova.throwableslimeball.common.item.init.ItemReference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(ThrowableSlimeball.MODID)
public class ThrowableSlimeball {
    public static final String MODID = "throwable_slimeball";

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");
    public static final TagKey<EntityType<?>> ENTITY_SLIME = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MODID, "slime"));
    public static final TagKey<EntityType<?>> ENTITY_MAGMA_CUBE = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MODID, "magma_cube"));
    public static final TagKey<Block> BLOCK_ELASTIC = BlockTags.create(new ResourceLocation(MODID, "elastic"));
    public static final TagKey<Block> BLOCK_STICKY = BlockTags.create(new ResourceLocation(MODID, "sticky"));

    public ThrowableSlimeball() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.CFG);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemReference.init();
        EntityTypeReference.init();
        ITEMS_VANILLA.register(bus);
        ENTITIES.register(bus);
    }
}
