package committee.nova.throwableslimeball.common.entity.init;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.entity.impl.MagmaCream;
import committee.nova.throwableslimeball.common.entity.impl.Slimeball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

public enum EntityTypeReference implements Supplier<EntityType<?>> {
    SLIME_BALL(() -> EntityType.Builder.<Slimeball>of(Slimeball::new, EntityClassification.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
            .build("slime_ball")),
    MAGMA_CREAM(() -> EntityType.Builder.<MagmaCream>of(MagmaCream::new, EntityClassification.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).fireImmune()
            .build("magma_cream")),
    ;

    EntityTypeReference(Supplier<EntityType<?>> sup) {
        reg = ThrowableSlimeball.ENTITIES.register(this.name().toLowerCase(Locale.ROOT), sup);
    }

    private final RegistryObject<EntityType<?>> reg;

    @Override
    public EntityType<?> get() {
        return reg.get();
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> EntityType<E> cast() {
        return (EntityType<E>) get();
    }

    public static void init() {
    }
}
