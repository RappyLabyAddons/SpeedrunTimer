package com.rappytv.speedruntimer.util;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import java.util.TimerTask;
import java.util.function.Supplier;

public class Timer {

    private static final String displayFormat = "%s:%s:%s";
    private static final java.util.Timer timer = new java.util.Timer();
    private final Runnable onCountdownComplete;
    private TimerState state = TimerState.OFF;
    private TimerDirection direction = TimerDirection.COUNT_DOWN;
    private long seconds = 0;

    public Timer(Runnable onCountdownComplete) {
        this.onCountdownComplete = onCountdownComplete;
    }

    public void startCountUp() {
        if(state != TimerState.OFF) return;
        this.direction = TimerDirection.COUNT_UP;
        this.seconds = 0;
        start();
    }

    public void startCountDown(long seconds) {
        if(state != TimerState.OFF) return;
        this.direction = TimerDirection.COUNT_DOWN;
        this.seconds = seconds;
        start();
    }

    public void reset() {
        if(state == TimerState.OFF) return;
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

        if(state == TimerState.PAUSED) component.decorate(TextDecoration.ITALIC).color(NamedTextColor.RED);
        else component.color(NamedTextColor.GREEN);
        return component.decorate(TextDecoration.BOLD);
    }

    private void start() {
        this.state = TimerState.RUNNING;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(state == TimerState.OFF) cancel();
                if(state == TimerState.PAUSED) return;

                if(direction == TimerDirection.COUNT_UP) seconds++;
                else if(direction == TimerDirection.COUNT_DOWN) {
                    seconds--;
                    if(seconds < 0) {
                        seconds = 0;
                        state = TimerState.PAUSED;
                        onCountdownComplete.run();
                    }
                }
            }
        }, 1000, 1000);
    }

    public long resolveSeconds(String timeValue) {
        String format = timeValue.substring(timeValue.length() - 1);
        long duration;
        try {
            duration = Integer.parseInt(timeValue.substring(0, timeValue.length() - 1));
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
                    yield resolveSeconds(Integer.parseInt(timeValue) + "s");
                } catch (NumberFormatException e) {
                    yield -1;
                }
            }
        };
    }

    public TimerDirection getDirection() {
        return direction;
    }

    public void setDirection(TimerDirection direction) {
        this.direction = direction;
    }

    public TimerState getState() {
        return state;
    }

    public void setState(TimerState state) {
        this.state = state;
    }

    public long getSeconds() {
        return seconds;
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
