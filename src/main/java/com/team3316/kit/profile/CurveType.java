package com.team3316.kit.profile;


public enum CurveType {
  POSITION(1),
  VELOCITY(2),
  ACCELERATION(3);

  private int mType;

  CurveType (int type) {
    this.mType = type;
  }

  public double[] vectorForTime (double t) {
    // TODO - Make this beautiful
    double[] v = new double[]{t, t, t, t, t, 1};

    if (this.mType == 1) { // Position curve
      v[0] *= t * t * t * t;
      v[1] *= t * t * t;
      v[2] *= t * t;
      v[3] *= t;
    }

    if (this.mType == 2) { // Velocity curve; 1st derivative
      v[0] *= 5 * t * t * t;
      v[1] *= 4 * t * t;
      v[2] *= 3 * t;
      v[3] *= 2 * t;
      v[4] = 1;
      v[5] = 0;
    }

    if (this.mType == 3) { // Acceleration curve; 2nd derivative
      v[0] *= 20 * t * t;
      v[1] *= 12 * t;
      v[2] *= 3;
      v[3] = 2;
      v[4] = 0;
      v[5] = 0;
    }

    return v;
  }

  public double[][] vectorsForTimes (double[] ts) {
    double[][] vs = new double[Curve.NUM_OF_SAMPLES][6];

    for (int i = 0; i < Curve.NUM_OF_SAMPLES; i++) {
      vs[i] = this.vectorForTime(ts[i]);
    }

    return vs;
  }
}

