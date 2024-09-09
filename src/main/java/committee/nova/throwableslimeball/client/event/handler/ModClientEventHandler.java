package committee.nova.throwableslimeball.client.event.handler;

import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeReference.SLIME_BALL.cast(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypeReference.MAGMA_CREAM.cast(), ThrownItemRenderer::new);
    }
}
