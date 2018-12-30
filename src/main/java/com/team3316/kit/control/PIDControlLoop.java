package com.team3316.kit.control;

import com.team3316.kit.Util;
import java.util.Vector;

/**
 * An implementation of a PID control loop.
 */
public abstract class PIDControlLoop extends ControlLoop {
  /**
   * PID control constants
   */
  public double kP, kI, kD;

  /**
   * Bounds of the PID output values
   */
  public double lowOutputBound, highOutputBound;

  /**
   * Period of the loop, in seconds
   */
  public double dt = 0.01;

  /**
   * Loop goal tolerance
   */
  public double tolerance;

  /**
   * Last and total error for the D and I components, respectively
   */
  private double _lastError = 0, _totalError = 0;

  /**
   * Sets the PID output bounds.
   * @param low The lower bound
   * @param high The upper bound
   */
  public void setOutputBounds (double low, double high) {
    this.lowOutputBound = low;
    this.highOutputBound = high;
  }

  /**
   * Sets the PID goal tolerance.
   * @param tolerance The goal tolerance of the PID loop
   */
  public void setTolerance (double tolerance) {
    this.tolerance = tolerance;
  }

  /**
   * Calculates the PID loop's output state
   * @param state The current state, retrieved from the {@link #currentState()} method.
   * @param goalState The goal state, retrieved using the {@link #goalState()} method.
   * @return A number between {@link #lowOutputBound} and {@link #highOutputBound} as an output of the PID controller.
   */
  @Override
  public Vector<Double> update (Vector<Double> state, Vector<Double> goalState) {
    // PID is only supported for single input - single output states.
    double currentValue = state.get(0), goalValue = goalState.get(0);

    double error = goalValue - currentValue;

    // Calculate the integral term only if it's actually needed
    if (this.kI > 0) {
      double lower = this.lowOutputBound / this.kI; // Min integral output
      double upper = this.highOutputBound / this.kI; // Max integral output
      double newTotalError = this._totalError + error;

      this._totalError = Util.clampToBounds(newTotalError, lower, upper);
    }

    // Calculating the PID value
    double outputValue = this.kP * error +
                         this.kI * this._totalError +
                         this.kD * ((error - this._lastError) / this.dt);
    this._lastError = error;

    double clampedValue = Util.clampToBounds(outputValue, this.lowOutputBound, this.highOutputBound);

    return Util.singleValueVector(clampedValue);
  }

  /**
   * Checkes whether the PID loop's result has met the goal requirements.
   * @param currentState The current state, retrieved from the {@link #currentState()} method.
   * @param goalState The goal state, retrieved using the {@link #goalState()} method.
   * @return A boolean indicating whether the current error is in a {@link #tolerance} neighborhood of zero.
   */
  @Override
  public boolean isOnGoal (Vector<Double> currentState, Vector<Double> goalState) {
    double currentError = goalState.get(0) - currentState.get(0);
    return Util.isInNeighborhood(currentError, 0, this.tolerance);
  }
}
