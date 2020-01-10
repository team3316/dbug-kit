package com.team3316.kit.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.team3316.kit.DBugLogger;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * DBugCommandBaseGroup
 */
public abstract class DBugCommandGroup extends DBugCommand {
    private List<List<Supplier<CommandBase>>> queue = new LinkedList<>();
    private List<List<Supplier<CommandBase>>> queueStorage = new LinkedList<>();
    private List<CommandBase> parallelsList;

    boolean canAdd = true;
    boolean isFinished;

    public DBugCommandGroup() {};



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
        if (parallelsList == null) {
            this._runNextSequential();
        } else if (parallelsList.stream().allMatch(cmd -> !cmd.isScheduled())) {
            DBugLogger.getInstance().info("SEQUENTIAL ENDED");
            if (!queue.isEmpty()) {
                this._runNextSequential();
            } else {
                isFinished = true;
            }
        }
    }

    private void _runNextSequential() {
        DBugLogger.getInstance().info("SEQUENTIAL STARTED");
        List<Supplier<CommandBase>> list = queue.remove(0);
        parallelsList = new LinkedList<>();

        for (Supplier<CommandBase> sup : list) {
            CommandBase cmd = sup.get();
            cmd.schedule();
            parallelsList.add(cmd);
        }
    }


    /**
     * @param args - appends a sequence of CommandBases to run. Given as suppliers, each returning a new CommandBase instance. These will run simultaneously.
     *
     */
    @SafeVarargs
    public final synchronized void add(Supplier<CommandBase>... args){
        List<Supplier<CommandBase>> l = new LinkedList<>();

        for (int i = 0; i < args.length; i++) {
            l.add(args[i]);
        }

        if (canAdd) {
            queueStorage.add(l);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to add sequential after initalizing command group ");
            sb.append(this.getName());
            sb.append(" [");
            sb.append(l.stream().map(sup -> ((Supplier<CommandBase>) sup).get().getClass().getName())
                .collect(Collectors.joining(", ")));
            sb.append("]");
            DBugLogger.getInstance().severe(sb.toString());
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

}