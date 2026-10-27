package com.onlysole.modest_magic.integration.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onlysole.modest_magic.config.ModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class RotatingLettersRenderer {

    private RotatingLettersRenderer() {
    }

    public static void render(GuiGraphics guiGraphics, ResourceLocation texture, int cx, int cy, int radius) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();

        int numLetters = 12;
        int letterSize = 8;

        double baseAngle = 0.0;

        if (!ModConfig.get().reducedEmiMotion) {
            baseAngle = -((System.currentTimeMillis() % 24000L) / 24000.0) * 360.0;
        }

        for (int i = 0; i < numLetters; i++) {
            double angle = baseAngle + (360.0 / numLetters) * i;

            double exactX = cx + Math.cos(Math.toRadians(angle)) * radius - (letterSize / 2.0);
            double exactY = cy + Math.sin(Math.toRadians(angle)) * radius - (letterSize / 2.0);

            int u = i * letterSize;
            int v = 0;

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(exactX, exactY, 0);

            guiGraphics.blit(texture, 0, 0, u, v, letterSize, letterSize, letterSize * numLetters, letterSize);

            guiGraphics.pose().popPose();
        }
    }
}