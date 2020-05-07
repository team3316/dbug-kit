package com.team3316.kit.commands;

import java.util.List;
import java.util.function.Supplier;

import com.team3316.kit.DBugLogger;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugCommandGroup
 */
public class DBugRaceParallel extends DBugParallel {

    public DBugRaceParallel(List<Supplier<CommandBase>> cmds) {
        super(cmds);
    }

    @Override
    public void execute() {
        parallelsDict.keySet().stream()
                .filter(cmd -> !cmd.isScheduled())
                .forEach((cmd) -> {
                    if (cmd.isFinished()) {
                        DBugLogger.getInstance().info(this.getClass().getName() + " RACE PARALLEL ENDED");
                        this.isFinished = true;
                    } else {
                        this.cancel();
                    }
                });
    }
}