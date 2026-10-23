package com.deltavisual.modules;

public class Module {
    private final String name;
    private final String category;
    private boolean enabled;
    public float anim = 0f;

    public Module(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isEnabled() { return enabled; }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}
}
