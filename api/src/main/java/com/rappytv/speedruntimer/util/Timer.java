package com.rappytv.speedruntimer.util;

import com.rappytv.speedruntimer.event.CountdownCompleteEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import java.util.TimerTask;

public class Timer {

    private static final String displayFormat = "%s:%s:%s";
    private static final java.util.Timer timer = new java.util.Timer();
    private TimerState state = TimerState.OFF;
    private TimerDirection direction = TimerDirection.COUNT_DOWN;
    private long seconds = 0;

    public void startCountUp() {
        if(this.state != TimerState.OFF) return;
        this.direction = TimerDirection.COUNT_UP;
        this.seconds = 0;
        this.start();
    }

    public void startCountdown(long seconds) {
        if(this.state != TimerState.OFF) return;
        this.direction = TimerDirection.COUNT_DOWN;
        this.seconds = seconds;
        this.start();
    }

    public void reset() {
        if(this.state == TimerState.OFF) return;
        this.direction = TimerDirection.COUNT_UP;
        this.seconds = 0;
        this.state = TimerState.OFF;
    }

    public Component getDisplay() {
        long hours = this.seconds / 3600;
        long minutes = (this.seconds % 3600) / 60;
        long seconds = this.seconds % 60;

        Component component = Component.text(String.format(
            displayFormat,
            (String.valueOf(hours).length() > 1 ? "" : "0") + hours,
            (String.valueOf(minutes).length() > 1 ? "" : "0") + minutes,
            (String.valueOf(seconds).length() > 1 ? "" : "0") + seconds
        ));

        if(this.state == TimerState.PAUSED) component.decorate(TextDecoration.ITALIC).color(NamedTextColor.RED);
        else component.color(NamedTextColor.GREEN);
        return component.decorate(TextDecoration.BOLD);
    }

    private void start() {
        this.state = TimerState.RUNNING;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(Timer.this.state == TimerState.OFF) this.cancel();
                if(Timer.this.state == TimerState.PAUSED) return;

                if(Timer.this.direction == TimerDirection.COUNT_UP) Timer.this.seconds++;
                else if(Timer.this.direction == TimerDirection.COUNT_DOWN) {
                    Timer.this.seconds--;
                    if(Timer.this.seconds < 0) {
                        Timer.this.seconds = 0;
                        Timer.this.state = TimerState.PAUSED;
                        Laby.fireEvent(new CountdownCompleteEvent());
                    }
                }
            }
        }, 1000, 1000);
    }

    public long resolveSeconds(String timeValue) {
        String format = timeValue.substring(timeValue.length() - 1);
        long duration;
        try {
            duration = Integer.parseInt(
                timeValue.length() > 1
                    ? timeValue.substring(0, timeValue.length() - 1)
                    : timeValue
            );
        } catch(NumberFormatException e) {
            return -1;
        }

        return switch(format) {
            case "s" -> duration;
            case "m" -> duration * 60;
            case "h" -> duration * 60 * 60;
            case "d" -> duration * 60 * 60 * 24;
            case "w" -> duration * 60 * 60 * 24 * 7;
            case "y" -> duration * 60 * 60 * 24 * 7 * 52;
            default -> {
                try {
                    yield this.resolveSeconds(Integer.parseInt(timeValue) + "s");
                } catch (NumberFormatException e) {
                    yield -1;
                }
            }
        };
    }

    public TimerDirection getDirection() {
        return this.direction;
    }

    public void setDirection(TimerDirection direction) {
        this.direction = direction;
    }

    public TimerState getState() {
        return this.state;
    }

    public void setState(TimerState state) {
        this.state = state;
    }

    public long getSeconds() {
        return this.seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public enum TimerDirection {
        COUNT_UP,
        COUNT_DOWN
    }

    public enum TimerState {
        RUNNING,
        PAUSED,
        OFF
    }
}
