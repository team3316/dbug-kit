package frc.robot.commands;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

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
     * runs the next parallel sequence of commands that should run
     * Modified since we need to save the parallel
     */
    protected void _start() {
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
            this.isFinished = true;
        }
    }
}