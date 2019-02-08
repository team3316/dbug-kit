package com.team3316.kit.control;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class PIDGains {
  private double _kP, _kI, _kD, _tolerance;

  public PIDGains(double kP, double kI, double kD, double tolerance) {
    this._kP = kP;
    this._kI = kI;
    this._kD = kD;
    this._tolerance = tolerance;
  }

  public double getP() {
    return this._kP;
  }

  public double getI() {
    return this._kI;
  }

  public double getD() {
    return this._kD;
  }

  public double getTolerance() {
    return this._tolerance;
  }

  public PIDController createController(PIDSource source, PIDOutput output, double period) {
    return new PIDController(this._kP, this._kI, this._kD, source, output, period);
  }

  public PIDController createController(PIDSource source, PIDOutput output) {
    return this.createController(source, output, 0.02);
  }
}
