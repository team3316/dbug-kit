package com.team3316.kit.path;

import de.siegmar.fastcsv.reader.CsvRow;

public class Segment {
  private double mTime, mHeading, mLeftDist, mRightDist, mVelocity, mAcceleration;

  public Segment (CsvRow csv) {
    this.mTime = Double.parseDouble(csv.getField("time"));
    this.mHeading = Double.parseDouble(csv.getField("heading"));
    this.mLeftDist = Double.parseDouble(csv.getField("leftdist"));
    this.mRightDist = Double.parseDouble(csv.getField("rightdist"));

    // TODO - Implement velocity and acceleration feed-forward terms
    this.mVelocity = 0; this.mAcceleration = 0;
  }

  public double getHeading () {
    return this.mHeading;
  }

  public double getLeftDist () {
    return this.mLeftDist;
  }

  public double getRightDist () {
    return this.mRightDist;
  }

  public double getVelocity () {
    return this.mVelocity;
  }

  public double getAcceleration () {
    return this.mAcceleration;
  }
}
