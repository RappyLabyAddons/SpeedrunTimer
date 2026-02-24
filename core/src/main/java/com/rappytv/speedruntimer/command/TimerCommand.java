package com.rappytv.speedruntimer.command;

import com.rappytv.speedruntimer.SpeedrunTimerAddon;
import com.rappytv.speedruntimer.util.Timer;
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
        Timer timer = addon.getTimer();

        this.withSubCommand(new StartSubcommand(timer));
        this.withSubCommand(new CountdownSubcommand(timer));
        this.withSubCommand(new PauseSubcommand(timer));
        this.withSubCommand(new ResumeSubcommand(timer));
        this.withSubCommand(new TimeSubcommand(timer));
        this.withSubCommand(new ResetSubcommand(timer));
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        this.displayMessage(
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

        private final Timer timer;

        protected StartSubcommand(Timer timer) {
            super("start");
            this.timer = timer;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(this.timer.getState() == TimerState.RUNNING) {
                this.displayMessage(
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
            if(this.timer.getState() == TimerState.PAUSED) {
                Laby.references().chatExecutor().chat("/timer resume", false);
                return true;
            }
            this.timer.startCountUp();

            this.displayMessage(
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

        private final Timer timer;

        protected CountdownSubcommand(Timer timer) {
            super("countdown", "down");
            this.timer = timer;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(this.timer.getState() == TimerState.RUNNING) {
                this.displayMessage(
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
            if(this.timer.getState() == TimerState.PAUSED) {
                Laby.references().chatExecutor().chat("/timer resume", false);
                return true;
            }

            if(arguments.length < 1) {
                this.displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.enterTimeValue",
                            NamedTextColor.GRAY
                        ))
                );
                return true;
            }
            long seconds = this.timer.resolveSeconds(arguments[0]);
            if(seconds < 0) {
                this.displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.enterTimeValue",
                            NamedTextColor.RED
                        ))
                );
                return true;
            }

            this.timer.startCountdown(seconds);
            this.displayMessage(
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

        private final Timer timer;

        protected PauseSubcommand(Timer timer) {
            super("pause", "stop");
            this.timer = timer;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(this.timer.getState() == TimerState.OFF) {
                this.displayMessage(
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
            if(this.timer.getState() == TimerState.PAUSED) {
                this.displayMessage(
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
            this.timer.setState(TimerState.PAUSED);
            this.displayMessage(
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

        private final Timer timer;

        protected ResumeSubcommand(Timer timer) {
            super("resume");
            this.timer = timer;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(this.timer.getState() == TimerState.OFF) {
                this.displayMessage(
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
            if(this.timer.getState() != TimerState.PAUSED) {
                this.displayMessage(
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
            if(this.timer.getDirection() == TimerDirection.COUNT_DOWN && this.timer.getSeconds() == 0) {
                this.displayMessage(
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
            this.timer.setState(TimerState.RUNNING);
            this.displayMessage(
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

        private final Timer timer;

        protected TimeSubcommand(Timer timer) {
            super("time");
            this.timer = timer;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(this.timer.getState() == TimerState.OFF) {
                this.displayMessage(
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
                this.displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.enterTimeValue",
                            NamedTextColor.GRAY
                        ))
                );
                return true;
            }
            long seconds = this.timer.resolveSeconds(arguments[0]);
            if(seconds < 0) {
                this.displayMessage(
                    Component.empty()
                        .append(SpeedrunTimerAddon.prefix())
                        .append(Component.translatable(
                            "speedruntimer.command.enterTimeValue",
                            NamedTextColor.RED
                        ))
                );
                return true;
            }

            this.timer.setSeconds(seconds);
            this.displayMessage(
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

        private final Timer timer;

        protected ResetSubcommand(Timer timer) {
            super("reset");
            this.timer = timer;
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(this.timer.getState() == TimerState.OFF) {
                this.displayMessage(
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
            this.timer.reset();
            this.displayMessage(
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
