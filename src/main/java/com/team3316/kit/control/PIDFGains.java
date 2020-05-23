package com.team3316.kit.control;

public class PIDFGains {
  private double _kP, _kI, _kD, _kF, _tolerance;

  public PIDFGains(double kP, double kI, double kD, double kF, double tolerance) {
    this._kP = kP;
    this._kI = kI;
    this._kD = kD;
    this._kF = kF;
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

  public double getF() {
    return this._kF;
  }

  public double getTolerance() {
    return this._tolerance;
  }
}
