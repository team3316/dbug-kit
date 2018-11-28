package com.team3316.kit.control;

import com.team3316.kit.Util;
import java.util.Vector;

/**
 * An implementation of a feed-forward controller.
 * This kind of controller is mainly used for speed control, and for doing that
 * the control constant - kF - should be 1 over the maximum speed you want to achieve.
 */
public abstract class FFControlLoop extends ControlLoop {
  /**
   * Feed-forward control constant
   */
  public double kF;

  @Override
  public Vector<Double> update (Vector<Double> state, Vector<Double> goalState) {
    double goalValue = goalState.get(0);
    double result = this.kF * goalValue;
    return Util.singleValueVector(result);
  }
}
