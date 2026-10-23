package com.deltavisual.gui;

import com.deltavisual.modules.Module;
import com.deltavisual.modules.ModuleManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
    private static final int PANEL_W = 130;
    private static final int GAP = 10;
    private static final int HEAD_H = 24;
    private static final int ROW_H = 22;

    private static final int BG = 0xE6101014;
    private static final int HEAD = 0xFF181820;
    private static final int ROW = 0xFF1C1C26;
    private static final int ACCENT = 0xFF6C7BFF;
    private static final int OFF = 0xFF3A3A48;

    private final List<String> categories = new ArrayList<>();

    public ClickGui() {
        super(new StringTextComponent("Delta Visual"));
        for (Module m : ModuleManager.MODULES) {
            if (!categories.contains(m.getCategory())) categories.add(m.getCategory());
        }
    }

    @Override
    public boolean isPauseScreen() { return false; }

    private int startX() {
        int total = categories.size() * PANEL_W + (categories.size() - 1) * GAP;
        return (this.width - total) / 2;
    }

    private static void rrect(MatrixStack ms, int x, int y, int w, int h, int r, int color) {
        for (int i = 0; i < r; i++) {
            double dy = r - i - 0.5;
            int inset = r - (int) Math.round(Math.sqrt(r * r - dy * dy));
            AbstractGui.fill(ms, x + inset, y + i, x + w - inset, y + i + 1, color);
            AbstractGui.fill(ms, x + inset, y + h - i - 1, x + w - inset, y + h - i, color);
        }
        AbstractGui.fill(ms, x, y + r, x + w, y + h - r, color);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pt) {
        this.renderBackground(ms);
        int x = startX();
        int y0 = 40;

        String title = "Delta Visual";
        this.font.drawShadow(ms, "Delta", (this.width - this.font.width(title)) / 2f, 14, ACCENT);
        this.font.drawShadow(ms, "Visual", (this.width - this.font.width(title)) / 2f + this.font.width("Delta "), 14, 0xFFFFFFFF);

        for (String cat : categories) {
            List<Module> list = modulesOf(cat);
            int h = HEAD_H + list.size() * ROW_H + 6;
            rrect(ms, x, y0, PANEL_W, h, 6, BG);
            rrect(ms, x, y0, PANEL_W, HEAD_H, 6, HEAD);
            this.font.drawShadow(ms, cat, x + 10, y0 + 8, 0xFFFFFFFF);

            int y = y0 + HEAD_H + 3;
            for (Module m : list) {
                boolean hover = mouseX >= x + 4 && mouseX <= x + PANEL_W - 4 && mouseY >= y && mouseY < y + ROW_H - 2;
                rrect(ms, x + 4, y, PANEL_W - 8, ROW_H - 2, 4, hover ? 0xFF24242F : ROW);
                this.font.drawShadow(ms, m.getName(), x + 10, y + 6, m.isEnabled() ? 0xFFFFFFFF : 0xFFAAAAAA);

                m.anim += ((m.isEnabled() ? 1f : 0f) - m.anim) * 0.25f;
                int sw = 22, sh = 10;
                int sx = x + PANEL_W - 10 - sw - 4, sy = y + 5;
                rrect(ms, sx, sy, sw, sh, 5, m.isEnabled() ? ACCENT : OFF);
                int kx = sx + 1 + Math.round((sw - sh) * m.anim);
                rrect(ms, kx, sy + 1, sh - 2, sh - 2, 4, 0xFFFFFFFF);
                y += ROW_H;
            }
            x += PANEL_W + GAP;
        }
        super.render(ms, mouseX, mouseY, pt);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0) {
            int x = startX();
            int y0 = 40;
            for (String cat : categories) {
                int y = y0 + HEAD_H + 3;
                for (Module m : modulesOf(cat)) {
                    if (mx >= x + 4 && mx <= x + PANEL_W - 4 && my >= y && my < y + ROW_H - 2) {
                        m.toggle();
                        return true;
                    }
                    y += ROW_H;
                }
                x += PANEL_W + GAP;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    private List<Module> modulesOf(String cat) {
        List<Module> out = new ArrayList<>();
        for (Module m : ModuleManager.MODULES) if (m.getCategory().equals(cat)) out.add(m);
        return out;
    }
}
