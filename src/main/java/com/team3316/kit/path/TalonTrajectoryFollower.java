package com.team3316.kit.path;

import com.team3316.kit.control.PIDGains;
import com.team3316.kit.motors.DBugTalon;
import edu.wpi.first.wpilibj.Notifier;

/**
 * A Trajectory follower that uses two master DBugTalons and a NavX as inputs and as outputs.
 * Built on top of the foundations that we built in 2018 when we used FalconPathPlanner.
 */
public class TalonTrajectoryFollower {
  private Notifier _notifier = new Notifier(this::calculate);

  private DBugTalon _leftMaster, _rightMaster;
  private Trajectory _trajectory;

  private int _currentSegment = 0;
  private PIDGains _leftGains = null, _rightGains = null, _angleGains = null;
  private double _initialAngle;

  public TalonTrajectoryFollower(DBugTalon leftMaster, DBugTalon rightMaster, Trajectory trajectory) {
    this._leftMaster = leftMaster;
    this._rightMaster = rightMaster;
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

  private void calculate() {
    double leftError = this._leftMaster.getError();
  }
}
