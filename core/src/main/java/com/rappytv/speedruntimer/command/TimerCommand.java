package com.rappytv.speedruntimer.command;

import com.rappytv.speedruntimer.SpeedrunTimerAddon;
import com.rappytv.speedruntimer.util.Timer.TimerDirection;
import com.rappytv.speedruntimer.util.Timer.TimerState;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;

public class TimerCommand extends Command {

    private final SpeedrunTimerAddon addon;

    public TimerCommand(SpeedrunTimerAddon addon) {
        super("timer");
        this.addon = addon;

        withSubCommand(new StartSubcommand(addon));
        withSubCommand(new PauseSubcommand(addon));
        withSubCommand(new ResumeSubcommand(addon));
        withSubCommand(new TimeSubcommand(addon));
        withSubCommand(new ResetSubcommand(addon));
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        return true;
    }

    private static class StartSubcommand extends SubCommand {

        private final SpeedrunTimerAddon addon;

        protected StartSubcommand(SpeedrunTimerAddon addon) {
            super("start");
            this.addon = addon;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(addon.getTimer().getState() == TimerState.RUNNING) {
                displayMessage("timer already running");
                return true;
            }
            if(addon.getTimer().getState() == TimerState.PAUSED) {
                Laby.references().chatExecutor().chat("/timer resume");
                return true;
            }
            if(arguments.length > 0 && arguments[0].equalsIgnoreCase("down")) {
                if(arguments.length < 2) {
                    displayMessage("enter number");
                    return true;
                }
                long seconds = addon.getTimer().resolveSeconds(arguments[1]);
                if(seconds < 0) {
                    displayMessage("invalid number");
                    return true;
                }

                addon.getTimer().startCountDown(seconds);
            } else {
                addon.getTimer().startCountUp();
            }
            displayMessage("timer started");
            return true;
        }
    }

    private static class PauseSubcommand extends SubCommand {

        private final SpeedrunTimerAddon addon;

        protected PauseSubcommand(SpeedrunTimerAddon addon) {
            super("pause", "stop");
            this.addon = addon;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(addon.getTimer().getState() == TimerState.OFF) {
                displayMessage("no timer running");
                return true;
            }
            if(addon.getTimer().getState() == TimerState.PAUSED) {
                displayMessage("timer already paused");
                return true;
            }
            addon.getTimer().setState(TimerState.PAUSED);
            displayMessage("timer successfully paused");
            return true;
        }
    }

    private static class ResumeSubcommand extends SubCommand {

        private final SpeedrunTimerAddon addon;

        protected ResumeSubcommand(SpeedrunTimerAddon addon) {
            super("resume");
            this.addon = addon;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(addon.getTimer().getState() != TimerState.PAUSED) {
                displayMessage("timer not paused");
                return true;
            }
            if(addon.getTimer().getDirection() == TimerDirection.COUNT_DOWN && addon.getTimer().getSeconds() == 0) {
                displayMessage("can't resume ended countdown");
                return true;
            }
            addon.getTimer().setState(TimerState.RUNNING);
            displayMessage("timer resumed");
            return true;
        }
    }

    private static class TimeSubcommand extends SubCommand {

        private final SpeedrunTimerAddon addon;

        protected TimeSubcommand(SpeedrunTimerAddon addon) {
            super("time");
            this.addon = addon;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(addon.getTimer().getState() == TimerState.OFF) {
                displayMessage("no timer running");
                return true;
            }
            long seconds = addon.getTimer().resolveSeconds(arguments[0]);
            if(seconds < 0) {
                displayMessage("invalid number");
                return true;
            }

            addon.getTimer().setSeconds(seconds);
            displayMessage("set time to " + seconds + " seconds");
            return true;
        }
    }

    private static class ResetSubcommand extends SubCommand {

        private final SpeedrunTimerAddon addon;

        protected ResetSubcommand(SpeedrunTimerAddon addon) {
            super("reset");
            this.addon = addon;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(addon.getTimer().getState() == TimerState.OFF) {
                displayMessage("no timer running");
                return true;
            }
            addon.getTimer().reset();
            displayMessage("successfully reset");
            return true;
        }
    }
}
