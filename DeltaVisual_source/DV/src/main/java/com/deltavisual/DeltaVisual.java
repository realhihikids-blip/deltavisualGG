package com.deltavisual;

import com.deltavisual.gui.ClickGui;
import com.deltavisual.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@Mod("deltavisual")
public class DeltaVisual {
    public static final String NAME = "Delta Visual";
    public static KeyBinding OPEN_GUI;

    public DeltaVisual() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        OPEN_GUI = new KeyBinding("Open Delta Visual", GLFW.GLFW_KEY_RIGHT_SHIFT, NAME);
        ClientRegistry.registerKeyBinding(OPEN_GUI);
        ModuleManager.init();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }
}
