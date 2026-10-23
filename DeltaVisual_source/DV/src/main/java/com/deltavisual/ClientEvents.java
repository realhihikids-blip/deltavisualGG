package com.deltavisual;

import com.deltavisual.gui.ClickGui;
import com.deltavisual.modules.ModuleManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {
    private int frames = 0;
    private int fps = 0;
    private long last = System.nanoTime();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        while (DeltaVisual.OPEN_GUI != null && DeltaVisual.OPEN_GUI.consumeClick()) {
            if (mc.screen == null) mc.setScreen(new ClickGui());
        }
        if (ModuleManager.FULLBRIGHT.isEnabled() && mc.player != null) {
            mc.options.gamma = 15.0D;
        }
    }

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier e) {
        if (ModuleManager.ZOOM.isEnabled()) {
            e.setFOV(e.getFOV() * 0.3D);
        }
    }

    @SubscribeEvent
    public void onOverlay(RenderGameOverlayEvent.Post e) {
        if (e.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.renderDebug) return;

        frames++;
        long now = System.nanoTime();
        if (now - last >= 1000000000L) {
            fps = frames;
            frames = 0;
            last = now;
        }

        MatrixStack ms = e.getMatrixStack();
        int y = 6;
        if (ModuleManager.WATERMARK.isEnabled()) {
            mc.font.drawShadow(ms, "Delta", 6, y, 0xFF6C7BFF);
            mc.font.drawShadow(ms, "Visual", 6 + mc.font.width("Delta "), y, 0xFFFFFFFF);
            y += 12;
        }
        if (ModuleManager.FPS.isEnabled()) {
            mc.font.drawShadow(ms, "FPS: " + fps, 6, y, 0xFFFFFFFF);
            y += 12;
        }
        if (ModuleManager.COORDS.isEnabled()) {
            String s = String.format("XYZ: %.1f / %.1f / %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
            mc.font.drawShadow(ms, s, 6, y, 0xFFFFFFFF);
        }
    }
}
