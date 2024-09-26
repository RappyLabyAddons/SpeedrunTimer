package com.rappytv.speedruntimer.hudWidget;

import com.rappytv.speedruntimer.SpeedrunTimerAddon;
import com.rappytv.speedruntimer.util.Timer.TimerState;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.render.font.ComponentRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;

public class TimerHudWidget extends SimpleHudWidget<HudWidgetConfig> {

    private final SpeedrunTimerAddon addon;
    private final ComponentRenderer renderer;

    public TimerHudWidget(SpeedrunTimerAddon addon) {
        super("speedruntimer_display", HudWidgetConfig.class);
        this.addon = addon;
        this.renderer = Laby.references().renderPipeline().componentRenderer();
    }

    @Override
    public void render(Stack stack, MutableMouse mouse, float partialTicks, boolean isEditorContext, HudSize size) {
        RenderableComponent statusComponent = RenderableComponent.of(addon.getTimer().getDisplay());
        if (stack != null) {
            renderer.builder().text(statusComponent).pos(this.anchor.isLeft() ? 2 : (this.anchor.isCenter() ? statusComponent.getWidth() / 2.0f : 2.0f), 0).color(-1).shadow(true).centered(this.anchor.isCenter()).render(stack);
        }
        size.set(statusComponent.getWidth(), statusComponent.getHeight());
    }

    @Override
    public boolean isVisibleInGame() {
        return addon.getTimer().getState() != TimerState.OFF;
    }
}
