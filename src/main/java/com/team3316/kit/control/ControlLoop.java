package com.team3316.kit.control;

import java.util.TimerTask;
import java.util.Vector;

/**
 * An interface that aims to describe a control loop which receives a vector of real numbers and
 * outputs a vector of real numbers.
 */
public abstract class ControlLoop {
  /**
   * A control loop timer task
   */
  private static class ControlTask extends TimerTask {
    private ControlLoop _controlLoop;

    /**
     * Constructs a new control task
     * @param loop The control loop to control
     */
    public ControlTask (ControlLoop loop) {
      this._controlLoop = loop;
    }

    @Override
    public void run () {
      Vector<Double> currentState = this._controlLoop.currentState();
      Vector<Double> goalState = this._controlLoop.goalState();
      Vector<Double> updatedState = this._controlLoop.update(currentState, goalState);

      this._controlLoop.mapUpdateToActuators(updatedState);
    }
  }

  /**
   * Returns the current state of the loop. This usually includes collecting data from the sensors
   * that are used in the loop. This is separated from the update method in order to be able to
   * use it in different places, such as the SDB.
   */
  public abstract Vector<Double> currentState ();

  /**
   * Retursn the goal state of the loop.
   */
  public abstract Vector<Double> goalState ();

  /**
   * Calculates the next set of output values for the loop.
   * @param state The current state, retrieved from the {@link #currentState()} method.
   * @param goalState The goal state, retrieved using the {@link #goalState()} method.
   */
  public abstract Vector<Double> update (Vector<Double> state, Vector<Double> goalState);

  /**
   * Maps the loop's output values to the actuators controlled by the loop.
   * @param updatedState The updated state, retrieved using the {@link #update(Vector state, Vector goalState)} method.
   */
  public abstract void mapUpdateToActuators (Vector<Double> updatedState);

  /**
   * Checks whether the loop has reached its goal.
   * @param currentState The current state, retrieved from the {@link #currentState()} method.
   * @param goalState The goal state, retrieved using the {@link #goalState()} method.
   * @return A boolean indicating whether the loop has reached its goal.
   */
  public abstract boolean isOnGoal (Vector<Double> currentState, Vector<Double> goalState);

  /**
   * Creates a new timer task object for a given control loop.
   * @param loop The loop that's wanted to be executed
   * @return A new ControlTask object.
   */
  public static ControlTask createTimerTask (ControlLoop loop) {
    return new ControlTask(loop);
  }
}
