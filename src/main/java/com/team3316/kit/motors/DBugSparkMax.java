package com.team3316.kit.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.team3316.kit.config.ConfigException;
import edu.wpi.first.wpilibj.PIDOutput;

public class DBugSparkMax extends CANSparkMax implements DBugMotorController {
  private final CANEncoder _encoder;
  private final CANPIDController _pidController;
  private double _distPerRevolution;

  /**
   * Create a new SPARK MAX Controller
   *
   * @param deviceNumber The device ID.
   * @param type     The motor type connected to the controller. Brushless motors
   *                 must be connected to their matching color and the hall sensor
   *                 plugged in. Brushed motors must be connected to the Red and
   */
  public DBugSparkMax(final int deviceNumber, final MotorType type) {
    super(deviceNumber, type);

    this._encoder = this.getEncoder();
    this._pidController = this.getPIDController();
    
    this.restoreFactoryDefaults();
  }

  /**
   * Create a new Spark MAX controller. This constructor uses the brushless configuration by default
   * since we usually use CTRE controllers for brushed motors
   * @param deviceNumber The CAN device ID.
   */
  public DBugSparkMax(final int deviceNumber) {
    this(deviceNumber, MotorType.kBrushless);
  }

  @Override
  public void configure () throws ConfigException {
    this.restoreFactoryDefaults();
  }

  @Override
  public void setDistancePerRevolution (final double dpr, final int upr) {
    this._distPerRevolution = dpr;
  }

  @Override
  public double getEncoderValue () {
    return this._encoder.getPosition(); // Units - NU
  }

  @Override
  public double getEncoderRate () {
    return this._encoder.getVelocity(); // Units - RPM
  }

  @Override
  public double getDistance () {
    return this._distPerRevolution * this.getEncoderValue();
  }

  @Override
  public double getVelocity () {
    return 60.0 * this._distPerRevolution * this.getEncoderRate();
  }

  @Override
  public void setDistance (final double distance) {
    this._encoder.setPosition(distance / this._distPerRevolution);
  }

  @Override
  public void setupPIDF (final double kP, final double kI, final double kD, final double kF) {
    this._pidController.setP(kP);
    this._pidController.setI(kI);
    this._pidController.setD(kD);
    this._pidController.setFF(kF);
    this._pidController.setFF(0.0);
    this._pidController.setOutputRange(-1.0, 1.0);
  }

  @Override
  public void zeroEncoder () {
    this._encoder.setPosition(0);
  }

  @Override
  public void setNeutralMode (final NeutralMode mode) {
    final IdleMode idleModeFromNeutralMode = mode == NeutralMode.Brake ? IdleMode.kBrake : IdleMode.kCoast;
    this.setIdleMode(idleModeFromNeutralMode);
  }

  @Override
  public PIDOutput getPercentPIDOutput () {
    return this; // REMARK - The Spark MAX implements the PIDOutput interface
  }

  public void set(final ControlMode mode, final double outputValue) {
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
