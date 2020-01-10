package com.team3316.kit.commands;

import com.team3316.kit.DBugLogger;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An abstract command class which logs info about the command to the logger.
 */
public abstract class DBugCommand extends CommandBase {

  @Override
  public final void initialize() {
    DBugLogger.getInstance().fine(this.getName() + " initialize");
    init();
  }

  public abstract void init ();

  @Override
  public abstract void execute();

  @Override
  public abstract boolean isFinished();

  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      DBugLogger.getInstance().fine(this.getName() + " interrupted");
    } else {
      DBugLogger.getInstance().fine(this.getName() + " end");  
    }
    fin(interrupted);
  }

  protected abstract void fin (boolean interrupted);
}
