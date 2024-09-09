package committee.nova.throwableslimeball;

import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.dispenser.ProxiedProjectileDispenseBehavior;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import committee.nova.throwableslimeball.common.item.impl.MagmaCreamProxy;
import committee.nova.throwableslimeball.common.item.impl.SlimeballProxy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ThrowableSlimeball.MODID)
public class ThrowableSlimeball {
    public static final String MODID = "throwable_slimeball";

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);
    // SB NeoForge removed Registry Replacement without providing any alternative except mixin
    //public static final DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(BuiltInRegistries.ITEM, "minecraft");
    public static final TagKey<EntityType<?>> ENTITY_SLIME = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "slime"));
    public static final TagKey<EntityType<?>> ENTITY_MAGMA_CUBE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "magma_cube"));
    public static final TagKey<Block> BLOCK_ELASTIC = BlockTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "elastic"));
    public static final TagKey<Block> BLOCK_STICKY = BlockTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "sticky"));

    public ThrowableSlimeball(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.CFG);
        EntityTypeReference.init();
        //ITEMS_VANILLA.register(bus);
        ENTITIES.register(bus);
        DispenserBlock.registerBehavior(Items.SLIME_BALL, new ProxiedProjectileDispenseBehavior(SlimeballProxy.getInstance()));
        DispenserBlock.registerBehavior(Items.MAGMA_CREAM, new ProxiedProjectileDispenseBehavior(MagmaCreamProxy.getInstance()));
    }
}
