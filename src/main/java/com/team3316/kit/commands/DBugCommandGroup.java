package com.team3316.kit.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.team3316.kit.DBugLogger;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugCommandGroup
 */
public abstract class DBugCommandGroup extends DBugCommand {
    private List<List<Supplier<CommandBase>>> queue = new LinkedList<>();
    private List<List<Supplier<CommandBase>>> queueStorage = new LinkedList<>();
    private Map<CommandBase, Boolean> parallelsDict;

    boolean canAdd = true;
    boolean isFinished;

    /**
     * Constructor - shall be overridden when writing command groups
     */
    public DBugCommandGroup() {
    };

    /**
     * @return The command group's representation of the commands that shall run -
     *         in the hierarchy
     */
    public List<List<Supplier<CommandBase>>> getStorage() {
        return this.queueStorage;
    }

    /**
     * starts the sequence
     */
    @Override
    public synchronized void init() {
        canAdd = false;
        isFinished = false;
        queue = new LinkedList<>(queueStorage);
    }

    @Override
    public void execute() {

        if (parallelsDict == null) {
            this._runNextSequential();
        } else {
            for (CommandBase cmd : parallelsDict.keySet()) {
                if (cmd.isFinished()) {
                    parallelsDict.put(cmd, true);
                }
            }

            if (parallelsDict.entrySet().stream()
                    .anyMatch((entry) -> !entry.getKey().isScheduled() && !entry.getValue())) {
                this.cancel();
            } else if (parallelsDict.entrySet().stream().allMatch(entry -> !entry.getKey().isScheduled())) {
                DBugLogger.getInstance().info(this.getClass().getName() + " SEQUENTIAL ENDED");
                if (!queue.isEmpty()) {
                    this._runNextSequential();
                } else {
                    isFinished = true;
                }
            }
        }

    }

    /**
     * runs the next parallel sequence of command that should run 
     */
    private void _runNextSequential() {
        DBugLogger.getInstance().info(this.getClass().getName() + " SEQUENTIAL STARTED");
        List<Supplier<CommandBase>> list = queue.remove(0);
        parallelsDict = new HashMap<>();

        for (Supplier<CommandBase> sup : list) {
            CommandBase cmd = sup.get();
            cmd.schedule();
            parallelsDict.put(cmd, false);
        }
    }

    /**
     * @param args - appends a sequence of CommandBases to run. Given as suppliers,
     *             each returning a new CommandBase instance. These will run
     *             simultaneously.
     *
     */
    @SafeVarargs
    protected final synchronized void add(Supplier<CommandBase>... args) {

        List<Supplier<CommandBase>> l = List.of(args);

        if (canAdd) {
            queueStorage.add(l);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to add sequential after initializing command group ");
            sb.append(this.getName());
            sb.append(" [");
            sb.append(l.stream().map(sup -> ((Supplier<CommandBase>) sup).get().getClass().getName())
                    .collect(Collectors.joining(", ")));
            sb.append("]");
            DBugLogger.getInstance().severe(sb.toString());
        }
    }

    /**
     * runs before the <code>end(boolean interrupted)</code> function.
     * @param interrupted - whether the command was interrupted or ended naturally.
     */
    @Override
    public final void fin(boolean interrupted) {
        if (interrupted && !queue.isEmpty()) {
            DBugLogger.getInstance().info(this.getClass().getName() + " INTERRUPTED");
            parallelsDict.keySet().stream().forEach(cmd -> cmd.cancel());
        } else {
            DBugLogger.getInstance().info(this.getClass().getName() + " ENDED");
        }
        queue = new LinkedList<>();
        parallelsDict = new HashMap<>();
    }

    /**
     * @return whether the command's end condition has been reached.
     */
    @Override
    public final boolean isFinished() {
        return isFinished;
    }

}