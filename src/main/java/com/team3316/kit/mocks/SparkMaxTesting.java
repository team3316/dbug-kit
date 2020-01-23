package com.team3316.kit.mocks;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.team3316.kit.motors.DBugSparkMax;

/**
 * SparkMaxTesting
 */
public class SparkMaxTesting extends DBugSparkMax implements AutoCloseable{

    private int _id;
    private double _dist ,_demand;
    private ControlMode _mode;
    private NeutralMode _neutralMode;
    private IdleMode _idleMode;

    public SparkMaxTesting(final int deviceNumber, final MotorType type) {
        super(deviceNumber, type);
        this._dist = 0;
    }

    public SparkMaxTesting(final int deviceNumber) {
        this(deviceNumber, MotorType.kBrushed);
    }

    @Override
    public void configure() { }

    @Override
    public CANError restoreFactoryDefaults() {
        return null;
    }

    @Override
    public CANError restoreFactoryDefaults(boolean persist) {
        return null;
    }

    @Override
    public void setDistance(double distance) {
        this._dist = distance;
    }

    @Override
    public double getDistance() {
        return this._dist;
    }

    @Override
    public double getOutputCurrent() {
        return this._demand;
    }

    @Override
    public void setupPIDF(double kP, double kI, double kD, double kF) {
        
    }

    @Override
    public void set(ControlMode mode, double outputValue) {
        this._demand = outputValue;
        this._mode = mode;
        if (mode == ControlMode.Position) {
            this._dist = outputValue;
        }
    }

    @Override
    public CANPIDController getPIDController() {
        return new CANPIDController(null);
    }

    @Override
    public void zeroEncoder() {
        this._dist = 0;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        this._neutralMode = mode;
    }

    @Override
    public CANError setIdleMode(IdleMode mode) {
        this._idleMode = mode;
        return null;
    }

    @Override
    public double getVelocity() {
        return this._demand;
    }

    @Override
    public int getDeviceId() {
        return this._id;
    }

}