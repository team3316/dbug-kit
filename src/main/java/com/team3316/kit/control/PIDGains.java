package com.team3316.kit.control;

public class PIDGains {
  private double _kP, _kI, _kD;

  public PIDGains(double kP, double kI, double kD) {
    this._kP = kP;
    this._kI = kI;
    this._kD = kD;
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
}
