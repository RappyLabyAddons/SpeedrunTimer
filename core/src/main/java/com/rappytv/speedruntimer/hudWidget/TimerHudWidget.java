package com.rappytv.speedruntimer.hudWidget;

import com.rappytv.speedruntimer.SpeedrunTimerAddon;
import com.rappytv.speedruntimer.util.Timer.TimerState;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.hud.HudWidgetRendererAccessor;
import net.labymod.api.client.gui.hud.binding.dropzone.HudWidgetDropzone;
import net.labymod.api.client.gui.hud.binding.dropzone.NamedHudWidgetDropzones;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.hud.position.HudWidgetAnchor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.render.font.ComponentRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.bounds.area.RectangleAreaPosition;

public class TimerHudWidget extends SimpleHudWidget<HudWidgetConfig> {

    private final SpeedrunTimerAddon addon;
    private final ComponentRenderer renderer;

    public TimerHudWidget(SpeedrunTimerAddon addon) {
        super("speedruntimer_display", HudWidgetConfig.class);
        this.addon = addon;
        this.renderer = Laby.references().renderPipeline().componentRenderer();

        this.bindDropzones(new TimerHudWidgetDropzone());
        this.setIcon(Icon.texture(ResourceLocation.create(
            "speedruntimer",
            "textures/timer.png"
        )));
    }

    public void initializePreConfigured(HudWidgetConfig config) {
        super.initializePreConfigured(config);
        config.setEnabled(true);
        config.setX(0.0f);
        config.setY(-50.0f);
        config.setAreaIdentifier(RectangleAreaPosition.BOTTOM_CENTER);
    }

    @Override
    public void render(RenderPhase phase, ScreenContext context, boolean isEditorContext, HudSize size) {
        Stack stack = context.stack();
        RenderableComponent statusComponent = RenderableComponent.of(this.addon.getTimer().getDisplay());
        if (stack != null) {
            this.renderer.builder().text(statusComponent).pos(this.anchor.isLeft() ? 2 : (this.anchor.isCenter() ? statusComponent.getWidth() / 2.0f : 2.0f), 0).color(-1).shadow(true).centered(this.anchor.isCenter()).render(stack);
        }
        size.set(statusComponent.getWidth(), statusComponent.getHeight());
    }

    @Override
    public boolean isVisibleInGame() {
        return this.addon.getTimer().getState() != TimerState.OFF;
    }

    public static class TimerHudWidgetDropzone extends HudWidgetDropzone {

        public TimerHudWidgetDropzone() {
            super("timer_display");
        }

        @Override
        public float getX(HudWidgetRendererAccessor renderer, HudSize hudWidgetSize) {
            return NamedHudWidgetDropzones.ACTION_BAR.getX(renderer, hudWidgetSize);
        }

        @Override
        public float getY(HudWidgetRendererAccessor renderer, HudSize hudWidgetSize) {
            return NamedHudWidgetDropzones.ACTION_BAR.getY(renderer, hudWidgetSize) - 15;
        }

        @Override
        public HudWidgetDropzone copy() {
            return new TimerHudWidgetDropzone();
        }

        @Override
        public HudWidgetAnchor getAnchor() {
            return HudWidgetAnchor.CENTER_BOTTOM;
        }
    }
}
