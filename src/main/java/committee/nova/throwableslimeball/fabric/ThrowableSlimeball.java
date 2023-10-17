package committee.nova.throwableslimeball.fabric;

import committee.nova.throwableslimeball.fabric.common.config.CommonConfig;
import committee.nova.throwableslimeball.fabric.common.entity.init.EntityTypeReference;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.config.ModConfig;

public class ThrowableSlimeball implements ModInitializer {
    public static final String MODID = "throwable_slimeball";

    public static final TagKey<EntityType<?>> ENTITY_SLIME = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, "slime"));
    public static final TagKey<EntityType<?>> ENTITY_MAGMA_CUBE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, "magma_cube"));
    public static final TagKey<Block> BLOCK_ELASTIC = TagKey.create(Registries.BLOCK, new ResourceLocation(MODID, "elastic"));
    public static final TagKey<Block> BLOCK_STICKY = TagKey.create(Registries.BLOCK, new ResourceLocation(MODID, "sticky"));

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, CommonConfig.CFG);
        EntityTypeReference.init();
    }
}
