package com.team3316.kit.commands;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import com.team3316.kit.DBugLogger;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugCommandGroup
 */
public class DBugDeadlineParallel extends DBugParallel {
    
    private CommandBase deadline;
    
    public DBugDeadlineParallel(List<Supplier<CommandBase>> cmds) {
        super(cmds);
    }
    
    /**
     * runs the next parallel sequence of command that should run
     */
    protected void _start() {
        DBugLogger.getInstance().info(this.getClass().getName() + " HAS STARTED");
        parallelsDict = new HashMap<>();

        for (int i = 0; i < commands.size(); i++) {
            CommandBase cmd = commands.get(i).get();
            if (i == 0) deadline = cmd;
            cmd.schedule();
            parallelsDict.put(cmd, false);
        }
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
        } else if (parallelsDict.get(deadline)) {
            DBugLogger.getInstance().info(this.getClass().getName() + " RACE PARALLEL ENDED");
            this.isFinished = true;
        }
    }
}