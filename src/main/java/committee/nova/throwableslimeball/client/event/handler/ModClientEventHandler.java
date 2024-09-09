package committee.nova.throwableslimeball.client.event.handler;

import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeReference.SLIME_BALL.cast(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityTypeReference.MAGMA_CREAM.cast(), ThrownItemRenderer::new);
    }
}
