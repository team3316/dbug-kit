package com.team3316.kit.motors;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team3316.kit.config.ConfigException;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public interface DBugMotorController {
  /**
   * Configures the motor controller using the parameters defined in the Config
   *
   * @throws ConfigException If something won't be found in the robot's config, a ConfigException will
   *                         be thrown.
   */
   void configure () throws ConfigException;

  /**
   * Sets the distance *per revolution* of the encoder connected to the motor controller. This sets the motor controller's inner
   * feedback coefficient to be the dpr / upp, where the upp is the native units per revolution of the
   * connected encoder (CTRE Mag Encoder or the Bourns one), resulting in a dpr result when the encoder
   * does one revolution.
   *
   * @param dpr The wanted distance per rotation. For angular motion, this will be 360 degrees times
   *            the gear ratio between the encoder and the end effector. For linear motion, this will
   *            be 2 * pi * r / g, where g is the gear ratio between the encoder and the wheel or drum
   *            and r is the radius of the wheel or drum that's connected to the encoder.
   * @param upr The number of native units in one revolution of the end effector. This isn't done
   *            automatically using the encoder type because we found out that for some reason we
   *            can't calculate this theoretically using the gear ratios.
   */
   void setDistancePerRevolution (double dpr, int upr);

  /**
   * Returns the raw relative encoder value from the Talon. Since this is a raw value, it's defined as
   * an *integer*, b/c the Talon uses native units and not user-defined ones. In order to get the distance
   * passed, use the {@link DBugMotorController#getDistance()} method instead.
   *
   * @return The raw relative encoder value from the Talon in native units
   */
  double getEncoderValue ();

  /**
   * Returns the raw relative encoder rate from the Talon for a period of 10ms. Since this is a raw
   * value, it's defined as an *integer*, b/c the Talon uses native units and not user-defined ones.
   * In order to get the distance passed, use the {@link DBugMotorController#getVelocity()} method instead.
   *
   * @return The raw relative encoder rate from the Talon in native units for a period of 10ms
   */
  double getEncoderRate ();

  /**
   * Returns the distance passed by the encoder that's connected to the Talon. This returns the value
   * as a *double*, since distance isn't discrete like native units. In order to get the encoder's raw
   * value, use the {@link DBugMotorController#getEncoderValue()} method instead.
   *
   * @return The distance that has been passed by the encoder, calculated by the dpr * rawValue.
   */
  double getDistance ();

  /**
   * Returns the velocity of the encoder that's connected to the motor controller. This returns the value
   * as a *double*, since velocity isn't discrete like native units. In order to get the encoder's
   * raw rate of change, use the {@link DBugMotorController#getEncoderRate()} method instead.
   *
   * @return The distance that has been passed by the encoder, calculated by the dpr * rawValue.
   */
  double getVelocity ();


  /**
   * Sets the distance stored in the Talon to a given number. The conversion is done using the previously
   * defined distPerPulse (using the {@link DBugMotorController#setDistancePerRevolution(double, int)} method).
   *
   * @param distance The amount of distance wanted to be set to the selected sensor position
   */
  void setDistance (double distance);

  /**
   * Configures the motor controller's PIDF coefficients for the default profile slot.
   *
   * @param kP The proportional loop coefficient
   * @param kI The integral loop coefficient
   * @param kD The derivative loop coefficient
   * @param kF The feed-forward loop coefficient
   */
  void setupPIDF (double kP, double kI, double kD, double kF);

  /**
   * Zeros the encoder that's attached to the motor controller.
   */
  void zeroEncoder();

  /**
   * Sets the motor controller's neutral mode to be the given one.
   * @param mode The given neutral mode - either coast or brake
   */
  void setNeutralMode(NeutralMode mode);

  /**
   * @return An instance of WPILib's PIDSource for use with regular PID loops for distance input.
   */
  default PIDSource getDistancePIDSource() {
    return new PIDSource() {
      @Override
      public void setPIDSourceType (PIDSourceType pidSource) {
        // TODO - Maybe implement? Maybe not? Need to check about this.
      }

      @Override
      public PIDSourceType getPIDSourceType () {
        return PIDSourceType.kDisplacement;
      }

      @Override
      public double pidGet () {
        return getDistance();
      }
    };
  }

  /**
   * @return An instance of WPILib's PIDSource for use with regular PID loops for velocity input.
   */
  default PIDSource getVelocityPIDSource() {
    return new PIDSource() {
      @Override
      public void setPIDSourceType (PIDSourceType pidSource) {
        // TODO - Maybe implement? Maybe not? Need to check about this.
      }

      @Override
      public PIDSourceType getPIDSourceType () {
        return PIDSourceType.kRate;
      }

      @Override
      public double pidGet () {
        return getVelocity();
      }
    };
  }

  /**
   * @return An instance of WPILib's PIDOutput for use with regular PID loops for percentage output.
   */
  PIDOutput getPercentPIDOutput();
}
