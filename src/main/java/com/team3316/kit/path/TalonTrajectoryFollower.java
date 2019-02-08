package com.team3316.kit.path;

import com.kauailabs.navx.frc.AHRS;
import com.team3316.kit.Util;
import com.team3316.kit.control.FFGains;
import com.team3316.kit.control.PIDGains;
import com.team3316.kit.motors.DBugTalon;
import edu.wpi.first.wpilibj.Notifier;

/**
 * A Trajectory follower that uses two master DBugTalons and a NavX as inputs and as outputs.
 * Built on top of the foundations that we built in 2018 when we used FalconPathPlanner.
 */
public class TalonTrajectoryFollower {
  private Notifier _notifier = new Notifier(this::loop);

  private DBugTalon _leftMaster, _rightMaster;
  private AHRS _navx;
  private Trajectory _trajectory;

  private int _currentSegment = 0;
  private PIDGains _leftGains = null, _rightGains = null, _angleGains = null;
  private FFGains _feedForwardGains = null;
  private double _initialAngle;
  private double _leftTotalError = 0, _rightTotalError = 0, _angleTotalError = 0;
  private double _leftLastError = 0, _rightLastError = 0, _angleLastError = 0;

  public static final double LOOP_PERIOD = 0.02; // The loop period

  public TalonTrajectoryFollower(DBugTalon leftMaster, DBugTalon rightMaster, AHRS navx, Trajectory trajectory) {
    this._leftMaster = leftMaster;
    this._rightMaster = rightMaster;
    this._navx = navx;
    this._initialAngle = navx.getYaw(); // REMARK - Assuming that the NavX's yaw axis is aligned with the robot's heading
    this._trajectory = trajectory;
  }

  public void setLeftPIDGains(PIDGains gains) {
    this._leftGains = gains;
  }

  public void setLeftPIDGains(double kP, double kI, double kD) {
    this._leftGains = new PIDGains(kP, kI, kD);
  }

  public void setRightPIDGains(PIDGains gains) {
    this._rightGains = gains;
  }

  public void setRightPIDGains(double kP, double kI, double kD) {
    this._rightGains = new PIDGains(kP, kI, kD);
  }

  public void setAnglePIDGains(PIDGains gains) {
    this._angleGains = gains;
  }

  public void setAnglePIDGains(double kP, double kI, double kD) {
    this._angleGains = new PIDGains(kP, kI, kD);
  }

  public void setFFGains(FFGains gains) {
    this._feedForwardGains = gains;
  }

  public void setFFGains(double maxVelocity, double maxAcceleration) {
    this._feedForwardGains = new FFGains(1 / maxVelocity, 1 / maxAcceleration);
  }

  private double calculatePID(double error, double totalError, double lastError, PIDGains gains) {
    double P = gains.getP() * error;
    double I = gains.getI() * totalError;
    double D = gains.getD() * (error - lastError) / LOOP_PERIOD;
    return P + I + D;
  }

  private void calculate() {
    Segment currentSegment = this._trajectory.getSegment(this._currentSegment);

    double currentAngle = this._initialAngle - this._navx.getYaw();
    double leftError = currentSegment.getLeftDist() - this._leftMaster.getDistance(),
           rightError = currentSegment.getRightDist() - this._rightMaster.getDistance(),
           angleError = currentSegment.getHeading() - currentAngle;

    /*
     * Integral calculations
     * PID Output interval is [-1/kI, 1/kI]
     */
    if (this._leftGains.getI() > 0) {
      double kI = this._leftGains.getI();
      double low = -1 / kI, high = 1 / kI;
      double newTotalLeftError = this._leftTotalError + leftError;

      this._leftTotalError = Util.clampToBounds(newTotalLeftError, low, high);
    }

    if (this._rightGains.getI() > 0) {
      double kI = this._rightGains.getI();
      double low = -1 / kI, high = 1 / kI;
      double newTotalRightError = this._rightTotalError + rightError;

      this._rightTotalError = Util.clampToBounds(newTotalRightError, low, high);
    }

    if (this._angleGains.getI() > 0) {
      double kI = this._leftGains.getI();
      double low = -1 / kI, high = 1 / kI;
      double newTotalAngleError = this._angleTotalError + angleError;

      this._angleTotalError = Util.clampToBounds(newTotalAngleError, low, high);
    }

    /*
     * PIDVA calculations
     * PID + feed-forward velocity and acceleration terms.
     */
    double ffTerm = this._feedForwardGains.getV() * currentSegment.getVelocity() +
                    this._feedForwardGains.getA() * currentSegment.getAcceleration();
    double leftValue = this.calculatePID(leftError, _leftTotalError, _leftLastError, this._leftGains) + ffTerm;
    double rightValue = this.calculatePID(rightError, _rightTotalError, _rightLastError, this._rightGains) + ffTerm;
    double angleValue = this.calculatePID(angleError, _angleTotalError, _angleLastError, this._angleGains);
  }

  private void loop() {

  }
}
