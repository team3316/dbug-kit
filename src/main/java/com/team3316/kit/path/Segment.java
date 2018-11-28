package com.team3316.kit.path;

import de.siegmar.fastcsv.reader.CsvRow;

public class Segment {
  private double _time, _heading, _leftDist, _rightDist, _velocity, _acceleration;

  public Segment (CsvRow csv) {
    this._time = Double.parseDouble(csv.getField("time"));
    this._heading = Double.parseDouble(csv.getField("heading"));
    this._leftDist = Double.parseDouble(csv.getField("leftdist"));
    this._rightDist = Double.parseDouble(csv.getField("rightdist"));

    // TODO - Implement velocity and acceleration feed-forward terms
    this._velocity = 0; this._acceleration = 0;
  }

  public double getHeading () {
    return this._heading;
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
