package committee.nova.throwableslimeball;

import committee.nova.throwableslimeball.common.config.CommonConfig;
import committee.nova.throwableslimeball.common.entity.impl.MagmaCream;
import committee.nova.throwableslimeball.common.entity.impl.Slimeball;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import committee.nova.throwableslimeball.common.item.init.ItemReference;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.Util;
import net.minecraft.world.World;
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
    public static final ITag.INamedTag<EntityType<?>> ENTITY_SLIME = EntityTypeTags.bind(MODID + ":slime");
    public static final ITag.INamedTag<EntityType<?>> ENTITY_MAGMA_CUBE = EntityTypeTags.bind(MODID + ":magma_cube");
    public static final ITag.INamedTag<Block> BLOCK_ELASTIC = BlockTags.bind(MODID + ":elastic");
    public static final ITag.INamedTag<Block> BLOCK_STICKY = BlockTags.bind(MODID + ":sticky");

    public ThrowableSlimeball() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.CFG);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemReference.init();
        EntityTypeReference.init();
        ITEMS_VANILLA.register(bus);
        ENTITIES.register(bus);
        DispenserBlock.registerBehavior(Items.SLIME_BALL, new ProjectileDispenseBehavior() {
            @Override
            protected ProjectileEntity getProjectile(World level, IPosition position, ItemStack itemStack) {
                return Util.make(new Slimeball(level, position.x(), position.y(), position.z()), s -> s.setItem(itemStack));
            }
        });
        DispenserBlock.registerBehavior(Items.MAGMA_CREAM, new ProjectileDispenseBehavior() {
            @Override
            protected ProjectileEntity getProjectile(World level, IPosition position, ItemStack itemStack) {
                return Util.make(new MagmaCream(level, position.x(), position.y(), position.z()), s -> s.setItem(itemStack));
            }
        });
    }
}
