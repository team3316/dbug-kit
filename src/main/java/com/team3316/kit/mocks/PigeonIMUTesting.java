package com.team3316.kit.mocks;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

/**
 * PigeonIMUTesting
 */
public class PigeonIMUTesting extends PigeonIMU {

    private int _deviceNumber;
    private TalonSRX _talon;
    private double __yaw;

    public PigeonIMUTesting(int deviceNumber) {
        super(deviceNumber);
        this._deviceNumber = deviceNumber;
    }

    public PigeonIMUTesting(TalonSRX talon) {
        super(talon);
        this._talon = talon;
    }

    @Override
    public ErrorCode setYaw(double angleDeg) {
        this.__yaw = angleDeg;
        return null;
    }

    @Override
    public ErrorCode setYaw(double angleDeg, int timeoutMs) {
        this.__yaw = angleDeg;
        return null;
    }

    @Override
    public ErrorCode getYawPitchRoll(double[] ypr_deg) {
        ypr_deg[3] = this.__yaw;
        return null;
    }

}