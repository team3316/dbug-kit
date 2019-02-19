package com.team3316.kit;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class DBugSubsystem extends Subsystem {
  DBugLogger logger = DBugLogger.getInstance();

  public abstract void initDefaultCommand();

  public void displayTestData() {}
  public void displayMatchData() {}
  public void displayCommands() {}
}
