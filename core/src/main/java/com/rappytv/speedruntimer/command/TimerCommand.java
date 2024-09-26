package com.rappytv.speedruntimer.command;

import com.rappytv.speedruntimer.SpeedrunTimerAddon;
import com.rappytv.speedruntimer.util.Timer.TimerDirection;
import com.rappytv.speedruntimer.util.Timer.TimerState;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class TimerCommand extends Command {

    public TimerCommand(SpeedrunTimerAddon addon) {
        super("timer");

        withSubCommand(new StartSubcommand(addon));
        withSubCommand(new CountdownSubcommand(addon));
        withSubCommand(new PauseSubcommand(addon));
        withSubCommand(new ResumeSubcommand(addon));
        withSubCommand(new TimeSubcommand(addon));
        withSubCommand(new ResetSubcommand(addon));
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        displayMessage(
            Component.empty()
                .append(SpeedrunTimerAddon.prefix())
                .append(Component.translatable(
                    "speedruntimer.command.usage",
                    NamedTextColor.RED,
                    Component.text(
                        "/" + prefix + " <start/countdown/pause/resume/time/reset>",
                        NamedTextColor.AQUA
                    )
                ))
        );
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
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.start.alreadyRunning",
                            NamedTextColor.RED,
                            Component.text("/timer reset", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            if(addon.getTimer().getState() == TimerState.PAUSED) {
                Laby.references().chatExecutor().chat("/timer resume", false);
                return true;
            }
            addon.getTimer().startCountUp();

            displayMessage(
                Component.empty()
                    .append(SpeedrunTimerAddon.prefix())
                    .append(Component.translatable(
                        "speedruntimer.command.start.success",
                        NamedTextColor.GRAY
                    ))
            );
            return true;
        }
    }

    private static class CountdownSubcommand extends SubCommand {

        private final SpeedrunTimerAddon addon;

        protected CountdownSubcommand(SpeedrunTimerAddon addon) {
            super("countdown", "down");
            this.addon = addon;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(addon.getTimer().getState() == TimerState.RUNNING) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.start.alreadyRunning",
                            NamedTextColor.RED,
                            Component.text("/timer reset", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            if(addon.getTimer().getState() == TimerState.PAUSED) {
                Laby.references().chatExecutor().chat("/timer resume", false);
                return true;
            }

            if(arguments.length < 1) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.enterTimeValue",
                            NamedTextColor.GRAY
                        ))
                );
                return true;
            }
            long seconds = addon.getTimer().resolveSeconds(arguments[0]);
            if(seconds < 0) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.enterTimeValue",
                            NamedTextColor.RED
                        ))
                );
                return true;
            }

            addon.getTimer().startCountDown(seconds);
            displayMessage(
                Component.empty()
                    .append(SpeedrunTimerAddon.prefix())
                    .append(Component.translatable(
                        "speedruntimer.command.start.success",
                        NamedTextColor.GRAY
                    ))
            );
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
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.noTimerRunning",
                            NamedTextColor.RED,
                            Component.text("/timer start", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            if(addon.getTimer().getState() == TimerState.PAUSED) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.pause.alreadyPaused",
                            NamedTextColor.RED,
                            Component.text("/timer resume", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            addon.getTimer().setState(TimerState.PAUSED);
            displayMessage(
                Component.empty()
                    .append(SpeedrunTimerAddon.prefix())
                    .append(Component.translatable(
                        "speedruntimer.command.pause.success",
                        NamedTextColor.GRAY
                    ))
            );
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
            if(addon.getTimer().getState() == TimerState.OFF) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.noTimerRunning",
                            NamedTextColor.RED,
                            Component.text("/timer start", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            if(addon.getTimer().getState() != TimerState.PAUSED) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.resume.notPaused",
                            NamedTextColor.RED,
                            Component.text("/timer pause", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            if(addon.getTimer().getDirection() == TimerDirection.COUNT_DOWN && addon.getTimer().getSeconds() == 0) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.resume.endedCountdown",
                            NamedTextColor.RED,
                            Component.text("/timer reset", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            addon.getTimer().setState(TimerState.RUNNING);
            displayMessage(
                Component.empty()
                    .append(SpeedrunTimerAddon.prefix())
                    .append(Component.translatable(
                        "speedruntimer.command.resume.success",
                        NamedTextColor.GRAY
                    ))
            );
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
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.noTimerRunning",
                            NamedTextColor.RED,
                            Component.text("/timer start", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            if(arguments.length < 1) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.enterTimeValue",
                            NamedTextColor.GRAY
                        ))
                );
                return true;
            }
            long seconds = addon.getTimer().resolveSeconds(arguments[0]);
            if(seconds < 0) {
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.enterTimeValue",
                            NamedTextColor.RED
                        ))
                );
                return true;
            }

            addon.getTimer().setSeconds(seconds);
            displayMessage(
                Component.empty()
                    .append(SpeedrunTimerAddon.prefix())
                    .append(Component.translatable(
                        "speedruntimer.command.time.success",
                        NamedTextColor.GRAY,
                        Component.text(arguments[0], NamedTextColor.AQUA)
                    ))
            );
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
                displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.noTimerRunning",
                            NamedTextColor.RED,
                            Component.text("/timer start", NamedTextColor.AQUA)
                        ))
                );
                return true;
            }
            addon.getTimer().reset();
            displayMessage(
                Component.empty()
                    .append(SpeedrunTimerAddon.prefix())
                    .append(Component.translatable(
                        "speedruntimer.command.reset.success",
                        NamedTextColor.GRAY
                    ))
            );
            return true;
        }
    }
}
