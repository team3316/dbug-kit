package com.team3316.kit.control;

public class PIDGains {
  private double _kP, _kI, _kD, _kV, _kA;

  public PIDGains(double kP, double kI, double kD, double kV, double kA) {
    this._kP = kP;
    this._kI = kI;
    this._kD = kD;
    this._kV = kV;
    this._kA = kA;
  }

  public PIDGains(double kP, double kI, double kD) {
    this(kP, kI, kD, 0.0, 0.0);
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

  public double getV() {
    return this._kV;
  }

  public double getA() {
    return this._kA;
  }
}
