package com.team3316.kit.commands;

import edu.wpi.first.wpilibj.command.Command;
import com.team3316.kit.DBugLogger;

/**
 * An abstract command class which logs info about the command to the logger.
 */
public abstract class DBugCommand extends Command {
  protected final void initialize () {
    DBugLogger.getInstance().fine(this.getName() + " initialize");
    init();
  }

  public abstract void init ();

  public abstract void execute ();

  public abstract boolean isFinished ();

  protected final void end () {
    DBugLogger.getInstance().fine(this.getName() + " end");
    fin();
  }

  public abstract void fin ();

  protected final void interrupted () {
    DBugLogger.getInstance().fine(this.getName() + " interrupted");
    interr();
  }

  public abstract void interr ();
}

