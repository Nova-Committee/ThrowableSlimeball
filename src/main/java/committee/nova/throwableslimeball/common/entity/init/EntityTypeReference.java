package committee.nova.throwableslimeball.common.entity.init;

import committee.nova.throwableslimeball.ThrowableSlimeball;
import committee.nova.throwableslimeball.common.entity.impl.MagmaCream;
import committee.nova.throwableslimeball.common.entity.impl.Slimeball;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class EntityTypeReference {
    public static final DeferredHolder<EntityType<?>, EntityType<Slimeball>> SLIME_BALL = ThrowableSlimeball.ENTITIES.registerEntityType(
            "slime_ball",
            (type, level) -> new Slimeball(type, level),
            MobCategory.MISC,
            builder -> builder.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final DeferredHolder<EntityType<?>, EntityType<MagmaCream>> MAGMA_CREAM = ThrowableSlimeball.ENTITIES.registerEntityType(
            "magma_cream",
            (type, level) -> new MagmaCream(type, level),
            MobCategory.MISC,
            builder -> builder.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).fireImmune());

    private EntityTypeReference() {
    }

    public static void init() {
    }
}
