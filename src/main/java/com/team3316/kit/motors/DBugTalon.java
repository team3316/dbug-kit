package com.team3316.kit.motors;

import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team3316.kit.config.Config;
import com.team3316.kit.config.ConfigException;

public class DBugTalon extends TalonSRX {
  private TalonType _type;
  private double _distPerPulse;

  private static final int kTimeout = 30;
  private static final int kPIDSlot = 0;

  public DBugTalon(int deviceNumber, TalonType type) throws ConfigException {
    super(deviceNumber);

    this._type = type;
    this.configure();
  }

  public DBugTalon(int deviceNumber) throws ConfigException {
    this(deviceNumber, TalonType.REGULAR);
  }

  /**
   * Configures the talon using the parameters defined in the Config
   * @throws ConfigException If something won't be found in the robot's config, a ConfigException will
   *                         be thrown.
   */
  private void configure() throws ConfigException {
    String configLabel = this._type.getConfigLabel();

    // General configurations
    this.configNominalOutputForward(0, DBugTalon.kTimeout);
    this.configNominalOutputReverse(0, DBugTalon.kTimeout);
    this.configPeakOutputForward(+1, DBugTalon.kTimeout);
    this.configPeakOutputReverse(-1, DBugTalon.kTimeout);
    this.configNeutralDeadband(
      (double) Config.getInstance().get(configLabel + ".neutralDeadband"),
      DBugTalon.kTimeout
    );

    // Closed loop configurations
    if (this._type.isClosedLoop()) {
      this.setStatusFramePeriod( // Sets the status frame duration to be 10ms
        StatusFrame.Status_2_Feedback0,
        10,
        DBugTalon.kTimeout
      );

      this.configSelectedFeedbackSensor( // Configs the feedback sensor connected to the talon
        this._type.getFeedbackDevice(),
        DBugTalon.kPIDSlot,
        DBugTalon.kTimeout
      );

      if (this._type.isRelative()) { // Zero the encoder if using a relative encoder
        this.setSelectedSensorPosition(0, DBugTalon.kPIDSlot, DBugTalon.kTimeout);
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
  public int getEncoderValue() {
    return this.getSelectedSensorPosition(DBugTalon.kPIDSlot);
  }

  /**
   * Returns the raw relative encoder rate from the Talon for a period of 10ms. Since this is a raw
   * value, it's defined as an *integer*, b/c the Talon uses native units and not user-defined ones.
   * In order to get the distance passed, use the {@link DBugTalon#getVelocity()} method instead.
   * @return The raw relative encoder rate from the Talon in native units for a period of 10ms
   */
  public int getEncoderRate() {
    return this.getSelectedSensorVelocity(DBugTalon.kPIDSlot);
  }

  /**
   * Returns the distance passed by the encoder that's connected to the Talon. This returns the value
   * as a *double*, since distance isn't discrete like native units. In order to get the encoder's raw
   * value, use the {@link DBugTalon#getEncoderValue()} method instead.
   * @return The distance that has been passed by the encoder, calculated by the dpr * rawValue.
   */
  public double getDistance() {
    return this._distPerPulse * this.getEncoderValue();
  }

  /**
   * Returns the velocity of the encoder that's connected to the Talon. This returns the value
   * as a *double*, since velocity isn't discrete like native units. In order to get the encoder's
   * raw rate of change, use the {@link DBugTalon#getEncoderRate()} method instead.
   * @return The distance that has been passed by the encoder, calculated by the dpr * rawValue.
   */
  public double getVelocity() {
    return this._distPerPulse * this.getEncoderRate() / 10.0;
  }

  /**
   * Zeros the encoder that's attached to the Talon.
   */
  public void zeroEncoder() {
    this.setSelectedSensorPosition(0, DBugTalon.kPIDSlot, DBugTalon.kTimeout);
  }
}
