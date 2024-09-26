package com.rappytv.speedruntimer;

import com.rappytv.speedruntimer.command.TimerCommand;
import com.rappytv.speedruntimer.hudWidget.TimerHudWidget;
import com.rappytv.speedruntimer.util.Timer;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class SpeedrunTimerAddon extends LabyAddon<SpeedrunTimerConfig> {

    private final Timer timer = new Timer();

    @Override
    protected void enable() {
        registerSettingCategory();
        registerCommand(new TimerCommand(this));
        Laby.labyAPI().hudWidgetRegistry().register(new TimerHudWidget(this));
    }

    @Override
    protected Class<? extends SpeedrunTimerConfig> configurationClass() {
        return SpeedrunTimerConfig.class;
    }

    public Timer getTimer() {
        return timer;
    }
}
