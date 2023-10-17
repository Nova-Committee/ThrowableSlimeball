package committee.nova.throwableslimeball.client.event.handler;

import committee.nova.throwableslimeball.common.entity.impl.MagmaCream;
import committee.nova.throwableslimeball.common.entity.impl.Slimeball;
import committee.nova.throwableslimeball.common.entity.init.EntityTypeReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void onRegisterRenderer(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeReference.SLIME_BALL.cast(),
                m -> new SpriteRenderer<Slimeball>(m, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeReference.MAGMA_CREAM.cast(),
                m -> new SpriteRenderer<MagmaCream>(m, Minecraft.getInstance().getItemRenderer()));
    }
}
