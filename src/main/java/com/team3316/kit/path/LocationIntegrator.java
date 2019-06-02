package com.team3316.kit.path;

import com.team3316.kit.motors.DBugMotorController;
import edu.wpi.first.wpilibj.Timer;

/**
 * A location integrator for doing pure pursuit.
 * Estimation is done using Eq. 1: https://www.researchgate.net/publication/255610927_Positioning_System_for_4Wheel_Mobile_Robot_Encoder_Gyro_and_Accelerometer_Data_Fusion_with_Error_Model_Method
 */
public class LocationIntegrator {
  private DBugMotorController _leftMaster, _rightMaster;
  private double _baseWidth;
  private double _lastX = 0, _lastY = 0, _lastTheta = 0, _lastTime = 0;
  private double _currentX = 0, _currentY = 0, _currentTheta = 0;

  public LocationIntegrator(DBugMotorController leftMaster, DBugMotorController rightMaster, double baseWidth) {
    this._leftMaster = leftMaster;
    this._rightMaster = rightMaster;
    this._baseWidth = baseWidth;
    this._lastTime = Timer.getFPGATimestamp();
  }

  public void populateData() {
    double currentTime = Timer.getFPGATimestamp();
    double dt = currentTime - this._lastTime;
    this._lastTime = currentTime;

    double leftVel = this._leftMaster.getVelocity();
    double rightVel = this._rightMaster.getVelocity();
    double velocity = (leftVel + rightVel) / 2.0;

    this._currentX = this._lastX + velocity * Math.cos(this._lastTheta) * dt;
    this._currentY = this._lastY + velocity * Math.sin(this._lastTheta) * dt;
    this._currentTheta = this._lastTheta + (leftVel - rightVel) * dt / this._baseWidth;
  }

  public void resetData() {
    this._lastX = 0;
    this._lastY = 0;
    this._lastTheta = 0;
    this._currentX = 0;
    this._currentY = 0;
    this._currentTheta = 0;
    this._lastTime = Timer.getFPGATimestamp();
  }

  public double getX() {
    return this._currentX;
  }

  public double getY() {
    return this._currentY;
  }

  public double getHeading() {
    return Math.toDegrees(this._currentTheta);
  }
}
