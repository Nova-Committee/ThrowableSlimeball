package committee.nova.throwableslimeball;

import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.dispenser.ProxiedProjectileDispenseBehavior;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import committee.nova.throwableslimeball.common.item.impl.MagmaCreamProxy;
import committee.nova.throwableslimeball.common.item.impl.SlimeballProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
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

    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(MODID);
    public static final TagKey<EntityType<?>> ENTITY_SLIME = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "slime"));
    public static final TagKey<EntityType<?>> ENTITY_MAGMA_CUBE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "magma_cube"));
    public static final TagKey<Block> BLOCK_ELASTIC = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MODID, "elastic"));
    public static final TagKey<Block> BLOCK_STICKY = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MODID, "sticky"));

    public ThrowableSlimeball(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.CFG);
        EntityTypeReference.init();
        ENTITIES.register(bus);
        DispenserBlock.registerBehavior(Items.SLIME_BALL, new ProxiedProjectileDispenseBehavior(SlimeballProxy.getInstance()));
        DispenserBlock.registerBehavior(Items.MAGMA_CREAM, new ProxiedProjectileDispenseBehavior(MagmaCreamProxy.getInstance()));
    }
}
