package com.team3316.kit.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import com.team3316.kit.DBugLogger;

/**
 * An abstract command group class which logs info about the command to the logger.
 */
public abstract class DBugCommandGroup extends CommandGroup {
  protected final void initialize () {
    DBugLogger.getInstance().fine(this.getName() + " initialize");
    init();
  }

  protected void init () {
  }

  protected final void end () {
    DBugLogger.getInstance().fine(this.getName() + " end");
    fin();
  }

  protected void fin () {
  }

  protected final void interrupted () {
    DBugLogger.getInstance().fine(this.getName() + " interrupted");
    interr();
  }

  protected void interr () {
  }
}

