package com.rappytv.speedruntimer;

import com.rappytv.speedruntimer.api.generated.ReferenceStorage;
import com.rappytv.speedruntimer.command.TimerCommand;
import com.rappytv.speedruntimer.hudwidget.TimerHudWidget;
import com.rappytv.speedruntimer.sound.DefaultTimerSound;
import com.rappytv.speedruntimer.sound.TimerSound;
import com.rappytv.speedruntimer.util.Timer;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.resources.ResourceLocation;
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

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void enable() {
        TimerSound timerSound = ((ReferenceStorage) this.referenceStorageAccessor()).getTimerSound();
        if(timerSound == null)
            timerSound = new DefaultTimerSound();
        ResourceLocation sound = timerSound.getNotificationSound();
        this.timer = new Timer(() -> {
            if(this.configuration().countdownSound().get()) {
                Laby.references().minecraftSounds().playSound(sound, 1f, 1f);
            }
        });
        this.registerSettingCategory();
        this.registerCommand(new TimerCommand(this));
        Laby.labyAPI().hudWidgetRegistry().register(new TimerHudWidget(this));
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
}
