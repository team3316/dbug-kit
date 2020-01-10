package com.team3316.kit;

import com.team3316.kit.DBugLogger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public abstract class DBugSubsystem extends SubsystemBase {
  DBugLogger logger = DBugLogger.getInstance();

  public abstract void initDefaultCommand();

  public abstract void displayTestData();
  public abstract void displayMatchData();
  public abstract void displayCommands();
}
