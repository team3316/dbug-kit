package com.team3316.kit.motors;

import java.util.Objects;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class DBugTalon extends WPI_TalonSRX implements DBugMotorController {
  /*
   * Private members
   */
  private TalonType _type;
  private double _distPerPulse;

  /*
   * Constants
   */
  public static final int kTimeout = 30;
  private static final int kPIDSlot = 0;
  private static final int kDefaultSlot = 0;

  /**
   * Constructs a new DBugTalon and configures defaults.
   * @param deviceNumber The Talon's ID on the CAN chain
   * @param type The Talon's sensor type (from the {@link TalonType} enum)
   */
  public DBugTalon(int deviceNumber, TalonType type) {
    super(deviceNumber);

    this._type = type;
    this.configure();
  }

  /**
   * Constructs a new DBugTalon using the regular configuration.
   * @param deviceNumber The Talon's ID on the CAN chain
   */
  public DBugTalon(int deviceNumber) {
    this(deviceNumber, TalonType.REGULAR);
  }

  /**
   * Configures the talon using the parameters defined in the Config
   */
  @Override
  public void configure () {
    // Do MotorBase's configuration first
    this.configFactoryDefault();

    // Set motor voltage to 0 at start
    this.set(ControlMode.PercentOutput, 0);

    String configLabel = this._type.getConfigLabel();

    // General configurations
    this.configNominalOutputForward(0, DBugTalon.kTimeout);
    this.configNominalOutputReverse(0, DBugTalon.kTimeout);
    this.configPeakOutputForward(+1, DBugTalon.kTimeout);
    this.configPeakOutputReverse(-1, DBugTalon.kTimeout);
    this.configNeutralDeadband(0.04); // Factory default

    // Closed loop configurations
    if (this._type.isClosedLoop()) {
      this.setStatusFramePeriod( // Sets the status frame duration to be 10ms
        StatusFrame.Status_2_Feedback0,
        10,
        DBugTalon.kTimeout
      );

      this.configSelectedFeedbackSensor( // Configs the feedback sensor connected to the talon
        Objects.requireNonNull(this._type.getFeedbackDevice()),
        DBugTalon.kPIDSlot,
        DBugTalon.kTimeout
      );

      if (this._type.isRelative()) { // Zero the encoder if using a relative encoder
        this.setSelectedSensorPosition(0.0, DBugTalon.kPIDSlot, DBugTalon.kTimeout);
      }
    }
  }

  /**
   * Sets the distance *per revolution* of the encoder connected to the Talon. This sets the Talon's inner
   * feedback coefficient to be the dpr / upp, where the upp is the native units per revolution of the
   * connected encoder (CTRE Mag Encoder or the Bourns one), resulting in a dpr result when the encoder
   * does one revolution.
   * @param dpr The wanted distance per rotation. For angular motion, this will be 360 degrees times
   *            the gear ratio between the encoder and the end effector. For linear motion, this will
   *            be 2 * pi * r / g, where g is the gear ratio between the encoder and the wheel or drum
   *            and r is the radius of the wheel or drum that's connected to the encoder.
   * @param upr The number of native units in one revolution of the end effector. This isn't done
   *            automatically using the encoder type because we found out that for some reason we
   *            can't calculate this theoretically using the gear ratios.
   */
  @Override
  public void setDistancePerRevolution(double dpr, int upr) {
    if (this._type.isClosedLoop()) {
      this._distPerPulse = dpr / upr;
    }
  }

  /**
   * Returns the raw relative encoder value from the Talon. Since this is a raw value, it's defined as
   * an *integer*, b/c the Talon uses native units and not user-defined ones. In order to get the distance
   * passed, use the {@link DBugTalon#getDistance()} method instead.
   * @return The raw relative encoder value from the Talon in native units
   */
  @Override
  public double getEncoderValue() {
    return (double) this.getSelectedSensorPosition(DBugTalon.kPIDSlot);
  }

  /**
   * Returns the raw relative encoder rate from the Talon for a period of 10ms. Since this is a raw
   * value, it's defined as an *integer*, b/c the Talon uses native units and not user-defined ones.
   * In order to get the distance passed, use the {@link DBugTalon#getVelocity()} method instead.
   * @return The raw relative encoder rate from the Talon in native units for a period of 10ms
   */
  @Override
  public double getEncoderRate() {
    return (double) this.getSelectedSensorVelocity(DBugTalon.kPIDSlot);
  }

  /**
   * Returns the distance passed by the encoder that's connected to the Talon. This returns the value
   * as a *double*, since distance isn't discrete like native units. In order to get the encoder's raw
   * value, use the {@link DBugTalon#getEncoderValue()} method instead.
   * @return The distance that has been passed by the encoder, calculated by the dpr * rawValue.
   */
  @Override
  public double getDistance() {
    return this._distPerPulse * this.getEncoderValue();
  }

  /**
   * Sets the distance stored in the Talon to a given number. The conversion is done using the previously
   * defined distPerPulse (using the {@link DBugTalon#setDistancePerRevolution(double, int)} method).
   * @param distance The amount of distance wanted to be set to the selected sensor position
   */
  @Override
  public void setDistance(double distance) {
    this.setSelectedSensorPosition(this.convertDistanceToPulses(distance), DBugTalon.kPIDSlot, DBugTalon.kTimeout);
  }

  /**
   * Returns the velocity of the encoder that's connected to the Talon. This returns the value
   * as a *double*, since velocity isn't discrete like native units. In order to get the encoder's
   * raw rate of change, use the {@link DBugTalon#getEncoderRate()} method instead.
   * @return The distance that has been passed by the encoder, calculated by the dpr * rawValue.
   */
  @Override
  public double getVelocity() {
    return 10.0 * this._distPerPulse * this.getEncoderRate();
  }

  /**
   * Returns the current closed loop error, scaled to the wanted input range (aka from native units to
   * user-defined units that were defined using {@link DBugTalon#setDistancePerRevolution(double, int)}).
   * @return The current closed loop error in native units, multiplied by the distPerPulse factor.
   */
  public double getError() {
    return this._distPerPulse * this.getClosedLoopError(DBugTalon.kPIDSlot);
  }

  /**
   * Zeros the encoder that's attached to the Talon.
   */
  @Override
  public void zeroEncoder() {
    this.setSelectedSensorPosition(0, DBugTalon.kPIDSlot, DBugTalon.kTimeout);
  }

  /**
   * Configures the Talon's PIDF coefficients for the given profile slot.
   * @param kP The proportional loop coefficient
   * @param kI The integral loop coefficient
   * @param kD The derivative loop coefficient
   * @param kF The feed-forward loop coefficient
   * @param slot The profile slot to set the coefficients to
   */
  public void setupPIDF(double kP, double kI, double kD, double kF, int slot) {
    this.selectProfileSlot(slot, DBugTalon.kPIDSlot);
    this.config_kP(slot, kP, DBugTalon.kTimeout);
    this.config_kI(slot, kI, DBugTalon.kTimeout);
    this.config_kD(slot, kD, DBugTalon.kTimeout);
    this.config_kF(slot, kF, DBugTalon.kTimeout);
  }

  /**
   * Configures the Talon's PIDF coefficients for the default profile slot.
   * @param kP The proportional loop coefficient
   * @param kI The integral loop coefficient
   * @param kD The derivative loop coefficient
   * @param kF The feed-forward loop coefficient
   */
  @Override
  public void setupPIDF(double kP, double kI, double kD, double kF) {
    this.setupPIDF(kP, kI, kD, kF, DBugTalon.kDefaultSlot);
  }

  /**
   * Configures the Talon's izone (integral zone) for the given profile slot.
   * @param izone The max izone
   * @param slot The profile slot to set the izone to
   */
  public void setupIZone(int izone, int slot) {
    this.selectProfileSlot(slot, DBugTalon.kPIDSlot);
    this.config_IntegralZone(slot, izone, DBugTalon.kTimeout);
  }

  /**
   * Configures the Talon's izone (integral zone) for the default profile slot.
   * @param izone The max izone
   */
  public void setupIZone(int izone) {
    this.setupIZone(izone, DBugTalon.kDefaultSlot);
  }

  /**
   * Calculates the amount of native units required to do the given amount of user-defined units
   * (defined using the {@link DBugTalon#setDistancePerRevolution(double, int)} method).
   * @param distance The amount of the user-defined units to do
   * @return The encoder pulses required in order to do it
   */
  public int convertDistanceToPulses(double distance) {
    return (int) Math.round(distance / this._distPerPulse);
  }

  /**
   * Calculates the native units per 100ms required to do the given amount of user-defined units per
   * second (defined using the {@link DBugTalon#setDistancePerRevolution(double, int)} method).
   * @param velocity The amount of the user-defined units to do in 1s
   * @return The encoder pulses per 100ms required in order to do it
   */
  public int convertVelocityToPulses(double velocity) {
    return (int) Math.round(velocity / (10 * this._distPerPulse));
  }

  /**
   * Configures the Talon's MotionMagic constants for the given profile slot.
   * @param cruiseVel The cruising velocity constant
   * @param cruiseAcc The max acceleration constant
   * @param slot The profile slot to set the MotionMagic constants to
   */
  public void setMotionMagic(double cruiseVel, double cruiseAcc, int slot) {
    this.selectProfileSlot(slot, DBugTalon.kTimeout);
    this.configMotionCruiseVelocity((int) Math.round(cruiseVel / (10 * this._distPerPulse)), DBugTalon.kTimeout);
    this.configMotionAcceleration((int) Math.round(cruiseAcc / (10 * this._distPerPulse)), DBugTalon.kTimeout);
  }

  /**
   * Overrides the {@link TalonSRX#set(ControlMode, double)} method for the ControlMode.Position mode.
   * @param mode See {@link TalonSRX#set(ControlMode, double)}
   * @param outputValue If mode == ControlMode.Position, this should be the value defined in distance units. Else, see {@link TalonSRX#set(ControlMode, double)}
   */
  @Override
  public void set (ControlMode mode, double outputValue) {
    switch (mode) {
      case Position:
        super.set(mode, this.convertDistanceToPulses(outputValue));
        break;
      default:
        super.set(mode, outputValue);
        break;
    }
  }
}
