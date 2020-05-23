package frc.robot.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugCommandGroup
 */
public abstract class DBugParallel extends DBugCommand {
    protected List<Supplier<CommandBase>> commands;
    protected Map<CommandBase, Boolean> parallelsDict;
    protected boolean isFinished = false;

    /**
     * Constructor - will be utilised in most sub-classes
     * 
     * @param args - commands to initialize in this parrallel group
     */
    public DBugParallel(List<Supplier<CommandBase>> cmds) {
        if (cmds.size() <= 0) {
            throw new IllegalArgumentException("Tried to initialize an empty parallel");
        } else {
            commands = List.copyOf(cmds);
            parallelsDict = new HashMap<>();
        }
    }

    /**
     * @return The command group's representation of the commands that shall run -
     *         in the hierarchy
     */
    public List<Supplier<CommandBase>> getStorage() {
        return this.commands;
    }

    /**
     * starts the sequence
     */
    @Override
    public synchronized void init() {
        this._start();
    }

    /**
     * runs the next parallel sequence of command that should run
     */
    protected void _start() {
        for (Supplier<CommandBase> sup : commands) {
            CommandBase cmd = sup.get();
            cmd.schedule();
            parallelsDict.put(cmd, false);
        }
    }

    /**
     * @return whether the command's end condition has been reached.
     */
    @Override
    public final boolean isFinished() {
        return isFinished;
    }

    /**
     * runs before the <code>end(boolean interrupted)</code> function.
     * 
     * @param interrupted - whether the command was interrupted or ended naturally.
     */
    @Override
    protected void fin(boolean interrupted) {
        parallelsDict.keySet().stream().filter(cmd -> cmd.isScheduled()).forEach(cmd -> cmd.cancel());
    }

    @Override
    public String toString() {
        String res = this.getClass().getName() + ":\n\t"
                + commands.stream().map(supp -> supp.get().toString()).collect(Collectors.joining("\n\t"));

        return res;
    }
}