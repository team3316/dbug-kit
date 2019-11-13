package com.team3316.robot.utils;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI.Port;

/**
 * AHRSTesting
 */
public class AHRSTesting extends AHRS {

  private float _yaw, _pitch;
  private float _velX, _velY, _velZ;
  private float _accX, _accY, _accZ;

  public AHRSTesting(Port port) {
    super(port);
    this._yaw = 0;
    this._pitch = 0;
  }

  public float getYaw() {
    return this._yaw;
  }

  public float getPitch() {
    return this._pitch;
  }

  public float getVelocityX() {
    return this._velX;
  }

  public float getVelocityY() {
    return this._velY;
  }

  public float getVelocityZ() {
    return this._velZ;
  }

  public float getRawAccelX() {
    return this._accX;
  }

  public float getRawAccelY() {
    return this._accY;
  }

  public float getRawAccelZ() {
    return this._accZ;
  }

}