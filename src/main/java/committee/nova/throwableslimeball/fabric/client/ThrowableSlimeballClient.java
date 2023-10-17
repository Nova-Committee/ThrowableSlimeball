package committee.nova.throwableslimeball.fabric.client;

import committee.nova.throwableslimeball.fabric.common.entity.init.EntityTypeReference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ThrowableSlimeballClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityTypeReference.SLIME_BALL.cast(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypeReference.MAGMA_CREAM.cast(), ThrownItemRenderer::new);
    }
}
