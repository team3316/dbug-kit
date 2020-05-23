package com.team3316.kit.commands;

import java.util.List;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * CommandTesting
 */
public class CommandTesting {

    /**
     * Simulate a command's flow without a scheduler for testing purposes
     * @param cmd the command to simulate
     * @return whether the command ended or not
     */
    public static boolean test(CommandBase cmd) {
        cmd.initialize();
        if (cmd.isFinished()) {
            cmd.end(false);
            return true;
        }
        cmd.execute();
        if (cmd.isFinished()) {
            cmd.end(false);
            return true;
        }
        return false;
    }

    /**
     * Simulate a DBugCommandGroup's flow without a scheduler for testing purposes
     * @param cmd the command group to simulate
     * @return whether the command group ended or not
     */
    public static boolean test(DBugCommandGroup cmdGroup) {
        boolean allFinished = true;
        for (List<Supplier<CommandBase>> parallel : cmdGroup.getStorage()) {
            for (Supplier<CommandBase> supplier : parallel) {
                allFinished = CommandTesting.test(supplier.get()) && allFinished;
            }
        }
        return allFinished;
    }

    /*
    public static boolean commandTesting(SequentialCommandGroup cmdGroup) {
        boolean allFinished = true;
        cmdGroup.initialize();
        for (int i = 0; i < 100; i++) {
            cmdGroup.execute();
        }
        cmdGroup.end();
    }
    */
}