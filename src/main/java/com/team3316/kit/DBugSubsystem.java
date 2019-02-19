package com.team3316.kit;

import com.team3316.kit.config.ConfigException;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class DBugSubsystem extends Subsystem {
  DBugLogger logger = DBugLogger.getInstance();

  public abstract void initDefaultCommand();

  public abstract void displayTestData();
  public abstract void displayMatchData();
  public abstract void displayCommands();
}
