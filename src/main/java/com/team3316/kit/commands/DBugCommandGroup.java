package com.team3316.kit.commands;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import com.team3316.kit.DBugLogger;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugCommandGroup
 */
public abstract class DBugCommandGroup extends DBugCommand {
    private Queue<Supplier<CommandBase>> queue = new ArrayDeque<>();
    private Queue<Supplier<CommandBase>> queueStorage = new ArrayDeque<>();
    private CommandBase head;

    private boolean canAdd = true;
    private boolean isFinished;

    /**
     * Constructor - shall be overridden when writing command groups
     */
    public DBugCommandGroup() {
    };

    /**
     * @return The command group's representation of the commands that shall run -
     *         in the hierarchy
     */
    public Queue<Supplier<CommandBase>> getStorage() {
        return this.queueStorage;
    }

    /**
     * starts the sequence
     */
    @Override
    public synchronized void init() {
        canAdd = false;
        isFinished = false;
        queue = new ArrayDeque<>(queueStorage);
    }

    @Override
    public void execute() {
        if (head == null) {
            isFinished = true;
        } else if (!head.isScheduled()) {
            if (head.isFinished()) {
                this._runNextSequential();
            } else {
                this.cancel();
            }
        }
    }

    /**
     * runs the next parallel sequence of command that should run 
     */
    private void _runNextSequential() {
        DBugLogger.getInstance().info(this.getClass().getName() + " SEQUENTIAL STARTED");
        head = queue.poll().get();

        if (head != null) {
            head.schedule();
        }
    }

    /**
     * Adds a sequential based on one CommandBase.
     * @param cmd - a new CommandBase to run sequentially - Passed as a supllier returning a CommandBase instant.
     *
     */
    protected final synchronized void add(Supplier<CommandBase> cmd) {

        if (canAdd) {
            queueStorage.add(cmd);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to add sequential after initializing command group ");
            sb.append(this.getName());
            sb.append(" [");
            sb.append(cmd.get().toString());
            sb.append("]");
            DBugLogger.getInstance().severe(sb.toString());
        }
    }

    /**
     * @param cmds - appends a sequence of CommandBases to run. Given as suppliers,
     *             each returning a new CommandBase instance. These will run
     *             in parallel until all are finished (or otherwise interrupted).
     *
     */
    @SafeVarargs
    protected final synchronized void addWaitParallel(Supplier<CommandBase>... cmds) {
        if (cmds.length <= 0) {
            DBugLogger.getInstance().severe("ERROR: Tried to initialize an empty Parallel");
            throw new IllegalArgumentException("Tried to initialize an empty parallel");
        }
        this.add(() -> new DBugWaitParallel(List.of(cmds)));
    }

    
    /**
     * @param cmds - appends a sequence of CommandBases to run. Given as suppliers,
     *             each returning a new CommandBase instance. These will run
     *             in parallel until one is finished (or otherwise interrupted).
     *
     */
    @SafeVarargs
    protected final synchronized void addRaceParallel(Supplier<CommandBase>... cmds) {
        if (cmds.length <= 0) {
            DBugLogger.getInstance().severe("ERROR: Tried to initialize an empty Parallel");
            throw new IllegalArgumentException("Tried to initialize an empty parallel");
        }
        this.add(() -> new DBugRaceParallel(List.of(cmds)));
    }

    /**
     * @param cmds - appends a sequence of CommandBases to run. Given as suppliers,
     *             each returning a new CommandBase instance. These will run
     *             in parallel until one is finished (or otherwise interrupted).
     *
     */
    @SafeVarargs
    protected final synchronized void addDeadlineParallel(Supplier<CommandBase> deadline, Supplier<CommandBase>... cmds) {
        List<Supplier<CommandBase>> list = Arrays.asList(cmds);
        list.add(0, deadline);
        this.add(() -> new DBugDeadlineParallel(list));
    }

    /**
     * runs before the <code>end(boolean interrupted)</code> function.
     * @param interrupted - whether the command was interrupted or ended naturally.
     */
    @Override
    public final void fin(boolean interrupted) {
        if (interrupted) {
            DBugLogger.getInstance().info(this.getClass().getName() + " INTERRUPTED");
            this.head.cancel();
        } else {
            DBugLogger.getInstance().info(this.getClass().getName() + " ENDED");
        }
        queue = new ArrayDeque<>();
    }

    /**
     * @return whether the command's end condition has been reached.
     */
    @Override
    public final boolean isFinished() {
        return isFinished;
    }

    public enum ParallelKind {
        Race,
        Deadline,
        Wait;
    }
}