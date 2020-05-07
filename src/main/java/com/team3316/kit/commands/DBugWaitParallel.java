package com.team3316.kit.commands;

import java.util.List;
import java.util.function.Supplier;

import com.team3316.kit.DBugLogger;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugCommandGroup
 */
public class DBugWaitParallel extends DBugParallel {

    public DBugWaitParallel(List<Supplier<CommandBase>> cmds) {
        super(cmds);
    }

    @Override
    public void execute() {
        for (CommandBase cmd : parallelsDict.keySet()) {
            if (cmd.isFinished()) {
                parallelsDict.put(cmd, true);
            }
        }

        if (parallelsDict.entrySet().stream()
                .anyMatch((entry) -> !entry.getKey().isScheduled() && !entry.getValue())) {
            this.cancel();
        } else if (parallelsDict.keySet().stream().allMatch(cmd -> !cmd.isScheduled())) {
            DBugLogger.getInstance().info(this.getClass().getName() + " WaitParallel ended");
            this.isFinished = true;
        }
    }
}