package com.deltavisual.modules;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> MODULES = new ArrayList<>();

    private static double savedGamma = 1.0D;

    public static final Module FULLBRIGHT = new Module("Fullbright", "Render") {
        @Override protected void onEnable() { savedGamma = Minecraft.getInstance().options.gamma; }
        @Override protected void onDisable() { Minecraft.getInstance().options.gamma = savedGamma; }
    };
    public static final Module ZOOM = new Module("Zoom", "Render");
    public static final Module WATERMARK = new Module("Watermark", "HUD");
    public static final Module FPS = new Module("FPS", "HUD");
    public static final Module COORDS = new Module("Coordinates", "HUD");

    public static void init() {
        MODULES.clear();
        MODULES.add(FULLBRIGHT);
        MODULES.add(ZOOM);
        MODULES.add(WATERMARK);
        MODULES.add(FPS);
        MODULES.add(COORDS);
        WATERMARK.toggle();
    }
}
