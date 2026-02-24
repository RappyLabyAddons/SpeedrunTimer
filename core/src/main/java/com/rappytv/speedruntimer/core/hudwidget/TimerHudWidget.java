package com.rappytv.speedruntimer.core.hudwidget;

import com.rappytv.speedruntimer.core.SpeedrunTimerAddon;
import com.rappytv.speedruntimer.api.Timer.TimerState;
import net.labymod.api.client.gfx.pipeline.renderer.text.TextRenderingOptions;
import net.labymod.api.client.gui.hud.HudWidgetRendererAccessor;
import net.labymod.api.client.gui.hud.binding.dropzone.HudWidgetDropzone;
import net.labymod.api.client.gui.hud.binding.dropzone.NamedHudWidgetDropzones;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.hud.position.HudWidgetAnchor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.bounds.area.RectangleAreaPosition;

public class TimerHudWidget extends SimpleHudWidget<HudWidgetConfig> {

    private final SpeedrunTimerAddon addon;

    public TimerHudWidget(SpeedrunTimerAddon addon) {
        super("speedruntimer_display", HudWidgetConfig.class);
        this.addon = addon;

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
        RenderableComponent statusComponent = RenderableComponent.of(this.addon.getTimer().getDisplay());
        context.canvas().submitRenderableComponent(
            statusComponent,
            this.anchor.isCenter() ? statusComponent.getWidth() / 2 : 0,
            0,
            -1,
            TextRenderingOptions.SHADOW | (this.anchor.isCenter()
                ? TextRenderingOptions.CENTERED
                : TextRenderingOptions.NONE
            ));
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
