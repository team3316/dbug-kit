package com.team3316.kit.commands;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugWaitParallel
 * This class implements a structure that stores multiple commands and runs them in parallel
 * The run ends when all commands are done
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

        /*
         * Check if any of the commands isn't scheduled but hasn't finished, if this is the case, cancel the entire group
         * then check if all are done 
         */
        boolean isDone = true;
        for (Map.Entry<CommandBase,Boolean> entry : parallelsDict.entrySet()) {
            boolean isCommandDone = entry.getValue();
            if (entry.getKey().isScheduled()) {
                isDone = false;
            } else {
                if (!isCommandDone) {
                    this.cancel();
                }
            }
        }
        isFinished = isDone;
    }
}