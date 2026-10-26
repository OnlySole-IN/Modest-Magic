package com.onlysole.modest_magic.events;

import com.onlysole.modest_magic.ModestMagic;
import com.onlysole.modest_magic.block.entity.ModBlockEntities;
import com.onlysole.modest_magic.block.renderer.AltarRenderer;
import com.onlysole.modest_magic.block.renderer.PedestalRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModestMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModestMagicClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ALTAR_BLOCK_ENTITY.get(), AltarRenderer::new);
    }
}