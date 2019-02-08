package com.team3316.kit.control;

public class FFGains {
  private double _kV, _kA;

  public FFGains(double kV, double kA) {
    this._kV = kV;
    this._kA = kA;
  }

  public double getV() {
    return this._kV;
  }

  public double getA() {
    return this._kA;
  }
}
