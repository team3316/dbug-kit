package com.team3316.kit.path;

import com.team3316.kit.Util;
import com.team3316.kit.control.ControlLoop;
import edu.wpi.first.wpilibj.Encoder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public abstract class TrajectoryFollower extends ControlLoop {
  private Encoder _leftEnc, _rightEnc;
  private Trajectory _trajectory;
  private int _currentSegment = 0;
  private double _totalLeftError, _totalRightError, _totalAngleError;
  private double _lastLeftError, _lastRightError;

  public double baseWidth;

  public double kLeftP, kLeftI, kLeftD;
  public double kRightP, kRightI, kRightD;
  public double kV, kA;

  public double leftTolerance, rightTolerance, angleTolerance;

  public double dt = 0.01;

  public TimerTask task;

  public TrajectoryFollower (Encoder leftEnc, Encoder rightEnc, Trajectory trajectory, double baseWidth) {
    this._leftEnc = leftEnc;
    this._rightEnc = rightEnc;
    this._trajectory = trajectory;
    this.baseWidth = baseWidth;
  }

  public void setLeftPIDConstants (double kP, double kI, double kD) {
    this.kLeftP = kP;
    this.kLeftI = kI;
    this.kLeftD = kD;
  }

  public void setRightPIDConstants (double kP, double kI, double kD) {
    this.kRightP = kP;
    this.kRightI = kI;
    this.kRightD = kD;
  }

  public void setRobotConstraints (double maxVelocity, double maxAcceleration) {
    this.kV = 1 / maxVelocity;
    this.kA = 1 / maxAcceleration;
  }

  public void startInTimer (Timer timer) {
    if (this.task == null) {
      this.task = ControlLoop.createTimerTask(this);
    }
    long dtInMilis = (long) this.dt * (long) 1000.0;
    timer.schedule(this.task, 0, dtInMilis);
  }

  public void stopTask () {
    if (this.task != null) {
      this.task.cancel();
    }
  }

  @Override
  public Vector<Double> currentState () {
    double leftDist = this._leftEnc.getDistance();
    double rightDist = this._rightEnc.getDistance();

    // Encoder-based angle measurement gives positive angles in the counter-clockwise direction, hence the minus sign.
    double angle = -Math.toDegrees((rightDist - leftDist) / this.baseWidth);

    Vector<Double> state = new Vector<>(3);
    state.set(0, leftDist);
    state.set(1, rightDist);
    state.set(2, angle);

    return state;
  }

  @Override
  public Vector<Double> goalState () {
    Segment seg = this._trajectory.getSegment(this._currentSegment);

    Vector<Double> goal = new Vector<>(5);
    goal.set(0, seg.getLeftDist());
    goal.set(1, seg.getRightDist());
    goal.set(2, seg.getHeading());
    goal.set(3, seg.getVelocity());
    goal.set(4, seg.getAcceleration());

    return goal;
  }

  /*
   * Updated state definition:
   * [leftVoltage, rightVoltage]
   */
  @Override
  public Vector<Double> update (Vector<Double> state, Vector<Double> goalState) {
    // TODO - Implement angle PID loop
    double leftError = goalState.get(0) - state.get(0);
    double rightError = goalState.get(1) - state.get(1);
    double angleError = goalState.get(2) - state.get(2);
    double velocity = goalState.get(3), acceleration = goalState.get(4);

    /*
     * Integral calculations
     * PID Output interval is [-1/kI, 1/kI]
     */
    if (this.kLeftI > 0) {
      double low = -1 / this.kLeftI;
      double high = 1 / this.kLeftI;
      double newTotalLeftErr = this._totalLeftError + leftError;

      this._totalLeftError = Util.clampToBounds(newTotalLeftErr, low, high);
    }

    if (this.kRightI > 0) {
      double low = -1 / this.kRightI;
      double high = 1 / this.kRightI;
      double newTotalRightErr = this._totalRightError + rightError;

      this._totalRightError = Util.clampToBounds(newTotalRightErr, low, high);
    }

    /*
     * PIDVA Calculations
     * PID + feed-forward velocity and acceleration terms.
     */

    double leftValue = this.kLeftP * leftError +
                       this.kLeftI * this._totalLeftError +
                       this.kLeftD * (((leftError - this._lastLeftError) / this.dt) - velocity) +
                       this.kV * velocity + this.kA * acceleration;
    this._lastLeftError = leftError;

    double rightValue = this.kRightP * rightError +
                        this.kRightI * this._totalRightError +
                        this.kRightD * (((rightError - this._lastRightError) / this.dt) - velocity) +
                        this.kV * velocity + this.kA * acceleration;
    this._lastRightError = rightError;

    Vector<Double> updatedState = new Vector<>(2);
    updatedState.set(0, leftValue);
    updatedState.set(1, rightValue);

    this._currentSegment++; // Increment the current segment after all the calculations

    return updatedState;
  }

  @Override
  public boolean isOnGoal (Vector<Double> currentState, Vector<Double> goalState) {
    double leftError = goalState.get(0) - currentState.get(0),
           rightError = goalState.get(1) - currentState.get(1);
    return Util.isInNeighborhood(leftError, 0, this.leftTolerance) &&
           Util.isInNeighborhood(rightError, 0, this.rightTolerance);
  }
}
