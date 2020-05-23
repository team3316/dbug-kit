package frc.robot.commands;

import java.util.List;
import java.util.function.Supplier;

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
                        this.isFinished = true;
                    } else {
                        this.cancel();
                    }
                });
    }
}