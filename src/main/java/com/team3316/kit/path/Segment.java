package com.team3316.kit.path;

import de.siegmar.fastcsv.reader.CsvRow;

public class Segment {
  private double _time, _heading, _centerDist, _leftDist, _rightDist, _velocity, _acceleration;

  public Segment (CsvRow csv) {
    this._time = Double.parseDouble(csv.getField("t"));
    this._heading = Double.parseDouble(csv.getField("theta"));
    this._centerDist = Double.parseDouble(csv.getField("s"));
    this._leftDist = Double.parseDouble(csv.getField("sl"));
    this._rightDist = Double.parseDouble(csv.getField("sr"));
    this._velocity = Double.parseDouble(csv.getField("v"));
    this._acceleration = Double.parseDouble(csv.getField("a"));
  }

  public double getHeading () {
    return this._heading;
  }

  public double getCenterDist () {
    return this._centerDist;
  }

  public double getLeftDist () {
    return this._leftDist;
  }

  public double getRightDist () {
    return this._rightDist;
  }

  public double getVelocity () {
    return this._velocity;
  }

  public double getAcceleration () {
    return this._acceleration;
  }
}
