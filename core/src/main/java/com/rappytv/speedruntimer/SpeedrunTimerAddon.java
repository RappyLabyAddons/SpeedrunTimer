package com.rappytv.speedruntimer;

import com.rappytv.speedruntimer.command.TimerCommand;
import com.rappytv.speedruntimer.event.CountdownCompleteEvent;
import com.rappytv.speedruntimer.hudwidget.TimerHudWidget;
import com.rappytv.speedruntimer.util.Timer;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.jetbrains.annotations.NotNull;

@AddonMain
public class SpeedrunTimerAddon extends LabyAddon<SpeedrunTimerConfig> {

    private static final Component prefix = Component.empty()
        .append(Component.text("[", NamedTextColor.DARK_GRAY))
        .append(Component.text("Timer", NamedTextColor.GOLD).decorate(TextDecoration.BOLD))
        .append(Component.text("]", NamedTextColor.DARK_GRAY))
        .append(Component.space());

    private Timer timer;
    private ResourceLocation timerSound;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void enable() {
        this.timer = new Timer();
        this.initializeTimerSound();
        this.registerSettingCategory();
        this.registerCommand(new TimerCommand(this));
        Laby.labyAPI().hudWidgetRegistry().register(new TimerHudWidget(this));
    }

    @Subscribe
    public void onCountDownComplete(CountdownCompleteEvent event) {
        if(!this.configuration().countdownSound().get() || this.timerSound == null) {
            return;
        }
        Laby.references().minecraftSounds().playSound(
            this.timerSound,
            1f,
            1f
        );
    }

    @Override
    protected Class<? extends SpeedrunTimerConfig> configurationClass() {
        return SpeedrunTimerConfig.class;
    }

    @NotNull
    public Timer getTimer() {
        return this.timer;
    }

    public static Component prefix() {
        return prefix;
    }

    private void initializeTimerSound() {
        String path;
        if(MinecraftVersions.current().equals(MinecraftVersions.V1_8_9)) {
            path = "note.pling";
        } else if(MinecraftVersions.current().equals(MinecraftVersions.V1_12_2)) {
            path = "block.note.pling";
        } else {
            path = "block.note_block.pling";
        }
        this.timerSound = ResourceLocation.create("minecraft", path);
    }
}
