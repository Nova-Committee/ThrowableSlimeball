package committee.nova.throwableslimeball.fabric.common.entity.init;

import committee.nova.throwableslimeball.fabric.ThrowableSlimeball;
import committee.nova.throwableslimeball.fabric.common.entity.impl.MagmaCream;
import committee.nova.throwableslimeball.fabric.common.entity.impl.Slimeball;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public enum EntityTypeReference implements Supplier<EntityType<?>> {
    SLIME_BALL(
            "slime_ball",
            EntityType.Builder.of(Slimeball::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build("slime_ball")
    ),
    MAGMA_CREAM("magma_cream", EntityType.Builder.of(MagmaCream::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).fireImmune()
            .build("magma_cream")),
    ;

    EntityTypeReference(String id, EntityType<?> obj) {
        this.reg = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                new ResourceLocation(ThrowableSlimeball.MODID, id),
                obj
        );
    }

    private final EntityType<?> reg;

    @Override
    public EntityType<?> get() {
        return reg;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> EntityType<E> cast() {
        return (EntityType<E>) get();
    }

    public static void init() {
    }
}
