package com.team3316.kit.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

public class DBugSparkMax extends CANSparkMax implements DBugMotorController {
  private CANEncoder _encoder;
  private CANPIDController _pidController;
  private double _distPerRevolution;

  /**
   * Create a new SPARK MAX Controller
   *
   * @param deviceNumber The device ID.
   * @param type         The motor type connected to the controller. Brushless
   *                     motors must be connected to their matching color and the
   *                     hall sensor plugged in. Brushed motors must be connected
   *                     to the Red and
   */
  public DBugSparkMax(int deviceNumber, MotorType type) {
    super(deviceNumber, type);

    this._encoder = this.getEncoder();
    this._pidController = this.getPIDController();

    this.configure();
  }

  /**
   * Create a new Spark MAX controller. This constructor uses the brushless
   * configuration by default since we usually use CTRE controllers for brushed
   * motors
   * 
   * @param deviceNumber The CAN device ID.
   */
  public DBugSparkMax(int deviceNumber) {
    this(deviceNumber, MotorType.kBrushless);
  }

  @Override
  public void configure() {
    this.restoreFactoryDefaults();
    this.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void setDistancePerRevolution(double dpr, int upr) {
    // No need for UPR because the Spark Max gives values from -1 to 1 on his own
    this._distPerRevolution = dpr;
  }

  /**
   * @return the encoder's value in rotations
   */
  @Override
  public double getEncoderValue() {
    return this._encoder.getPosition();
  }

  /**
   * @return the encoder's velocity in RPM
   */
  @Override
  public double getEncoderRate() {
    return this._encoder.getVelocity();
  }

  @Override
  public double getDistance() {
    return this._distPerRevolution * this.getEncoderValue();
  }

  @Override
  public double getVelocity() {
    return this._distPerRevolution * this.getEncoderRate();
  }

  @Override
  public double getOutputCurrent() {
    return super.getOutputCurrent();
  }

  @Override
  public void setDistance(double distance) {
    this._encoder.setPosition(distance / this._distPerRevolution);
  }

  @Override
  public void setupPIDF(double kP, double kI, double kD, double kF) {
    this._pidController.setP(kP);
    this._pidController.setI(kI);
    this._pidController.setD(kD);
    this._pidController.setFF(kF);
    this._pidController.setOutputRange(-1.0, 1.0);
  }

  @Override
  public void zeroEncoder() {
    this._encoder.setPosition(0);
  }

  @Override
  public void setNeutralMode (NeutralMode mode) {
    IdleMode idleModeFromNeutralMode = mode == NeutralMode.Brake ? IdleMode.kBrake : IdleMode.kCoast;
    this.setIdleMode(idleModeFromNeutralMode);
  }

  public void set(ControlMode mode, double outputValue) {
    switch (mode) {
    case Position:
      this._pidController.setReference(outputValue, ControlType.kPosition);
      break;
    case Velocity:
      this._pidController.setReference(outputValue, ControlType.kVelocity);
      break;
    case Current:
      this._pidController.setReference(outputValue, ControlType.kCurrent);
      break;
    case PercentOutput:
      this.set(outputValue);
      break;
    default:
      throw new Error("Control Mode " + mode.toString() + " isn't supported by the Spark MAX at the moment.");
    }
  }
}
