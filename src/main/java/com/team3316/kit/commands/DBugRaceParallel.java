package com.team3316.kit.commands;

import java.util.List;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugRaceParallel
 * This class implements a structure that stores multiple commands and runs them in parallel
 * The run ends when the first command is done
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
                        this.isFinished = true;
                    } else {
                        this.cancel();
                    }
                });
    }
}