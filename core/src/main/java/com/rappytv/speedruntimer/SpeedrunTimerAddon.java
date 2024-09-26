package com.rappytv.speedruntimer;

import com.rappytv.speedruntimer.command.TimerCommand;
import com.rappytv.speedruntimer.core.generated.DefaultReferenceStorage;
import com.rappytv.speedruntimer.hudWidget.TimerHudWidget;
import com.rappytv.speedruntimer.sound.DefaultTimerSound;
import com.rappytv.speedruntimer.sound.ITimerSound;
import com.rappytv.speedruntimer.util.Timer;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.jetbrains.annotations.NotNull;

@AddonMain
public class SpeedrunTimerAddon extends LabyAddon<SpeedrunTimerConfig> {

    private Timer timer;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void enable() {
        ITimerSound timerSound = ((DefaultReferenceStorage) this.referenceStorageAccessor()).iTimerSound();
        if(timerSound == null)
            timerSound = new DefaultTimerSound();
        ResourceLocation sound = timerSound.getNotificationSound();
        timer = new Timer(() -> {
            if(configuration().countdownSound().get()) {
                Laby.references().minecraftSounds().playSound(sound, 1f, 1f);
            }
        });
        registerSettingCategory();
        registerCommand(new TimerCommand(this));
        Laby.labyAPI().hudWidgetRegistry().register(new TimerHudWidget(this));
    }

    @Override
    protected Class<? extends SpeedrunTimerConfig> configurationClass() {
        return SpeedrunTimerConfig.class;
    }

    @NotNull
    public Timer getTimer() {
        return timer;
    }
}
