package com.team3316.kit.mocks;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.team3316.kit.config.ConfigException;
import com.team3316.kit.motors.DBugTalon;
import com.team3316.kit.motors.TalonType;

import edu.wpi.first.wpilibj.PIDOutput;

public class TalonSRXTesting extends DBugTalon implements IMotorController {

  private int _id;
  private IMotorController _following;
  private ControlMode _controlMode;
  private double _demand, _sensorValue, _distPerPulse;
  private NeutralMode _neutralMode;
	private boolean _phaseSensor, _invertad;

	public TalonSRXTesting(int id, TalonType type) throws ConfigException {
		super(id, type);
  }

  public TalonSRXTesting(int id) throws ConfigException {
		super(id);
	}
	
	@Override
	public void configure() { }

	@Override
  	public double getDistance() {
    	return this._demand;
  	}

	@Override
	public PIDOutput getPercentPIDOutput() {
		return null;
	}


	@Override
	public void follow(IMotorController masterToFollow) {
    this._following = masterToFollow;
	}

	@Override
	public void valueUpdated() {
		
	}

	@Override
	public void set(ControlMode Mode, double demand) {
    this._controlMode = Mode;
    this._demand = demand;
	}

	@Override
	public void set(ControlMode Mode, double demand0, double demand1) {

	}

	@Override
	public void set(ControlMode Mode, double demand0, DemandType demand1Type, double demand1) {

	}

	@Override
	public void neutralOutput() {
		
	}

	@Override
	public void setNeutralMode(NeutralMode neutralMode) {
  this._neutralMode = neutralMode;
	}

	@Override
	public void setSensorPhase(boolean PhaseSensor) {
		this._phaseSensor = PhaseSensor;
	}

	@Override
	public void setInverted(boolean invert) {
		this._invertad = invert;
	}

	@Override
	public boolean getInverted() {
		return this._invertad;
	}

	@Override
	public ErrorCode configOpenloopRamp(double secondsFromNeutralToFull, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configClosedloopRamp(double secondsFromNeutralToFull, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configPeakOutputForward(double percentOut, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configPeakOutputReverse(double percentOut, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configNominalOutputForward(double percentOut, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configNominalOutputReverse(double percentOut, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configNeutralDeadband(double percentDeadband, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configVoltageCompSaturation(double voltage, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configVoltageMeasurementFilter(int filterWindowSamples, int timeoutMs) {
		return null;
	}

	@Override
	public void enableVoltageCompensation(boolean enable) {
		
	}

	@Override
	public double getBusVoltage() {
		return 0;
	}

	@Override
	public double getMotorOutputPercent() {
		if (this._following != null) {
			return this._following.getMotorOutputPercent();
		}
    return this._demand;
	}

	@Override
	public double getMotorOutputVoltage() {
		if (this._following != null) {
			return this._following.getMotorOutputVoltage();
		}
    return this._demand;
	}

	@Override
	public double getTemperature() {
		return 0;
	}

	@Override
	public ErrorCode configSelectedFeedbackSensor(RemoteFeedbackDevice feedbackDevice, int pidIdx, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configSelectedFeedbackCoefficient(double coefficient, int pidIdx, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configRemoteFeedbackFilter(int deviceID, RemoteSensorSource remoteSensorSource, int remoteOrdinal,
			int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configSensorTerm(SensorTerm sensorTerm, FeedbackDevice feedbackDevice, int timeoutMs) {
		return null;
	}

	@Override
	public int getSelectedSensorPosition(int pidIdx) {
		return (int) this._sensorValue;
	}

	@Override
	public int getSelectedSensorVelocity(int pidIdx) {
		return 0;
	}

	@Override
	public ErrorCode setSelectedSensorPosition(int sensorPos, int pidIdx, int timeoutMs) {
    this._sensorValue = sensorPos;
    return null;
	}

	@Override
	public ErrorCode setControlFramePeriod(ControlFrame frame, int periodMs) {
		return null;
	}

	@Override
	public ErrorCode setStatusFramePeriod(StatusFrame frame, int periodMs, int timeoutMs) {
		return null;
	}

	@Override
	public int getStatusFramePeriod(StatusFrame frame, int timeoutMs) {
		return 0;
	}

	@Override
	public ErrorCode configForwardLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose,
			int deviceID, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configReverseLimitSwitchSource(RemoteLimitSwitchSource type, LimitSwitchNormal normalOpenOrClose,
			int deviceID, int timeoutMs) {
		return null;
	}

	@Override
	public void overrideLimitSwitchesEnable(boolean enable) {
		
	}

	@Override
	public ErrorCode configForwardSoftLimitThreshold(int forwardSensorLimit, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configReverseSoftLimitThreshold(int reverseSensorLimit, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configForwardSoftLimitEnable(boolean enable, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configReverseSoftLimitEnable(boolean enable, int timeoutMs) {
		return null;
	}

	@Override
	public void overrideSoftLimitsEnable(boolean enable) {
		
	}

	@Override
	public ErrorCode config_kP(int slotIdx, double value, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode config_kI(int slotIdx, double value, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode config_kD(int slotIdx, double value, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode config_kF(int slotIdx, double value, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode config_IntegralZone(int slotIdx, int izone, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configAllowableClosedloopError(int slotIdx, int allowableCloseLoopError, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configMaxIntegralAccumulator(int slotIdx, double iaccum, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configClosedLoopPeakOutput(int slotIdx, double percentOut, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configClosedLoopPeriod(int slotIdx, int loopTimeMs, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configAuxPIDPolarity(boolean invert, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode setIntegralAccumulator(double iaccum, int pidIdx, int timeoutMs) {
		return null;
	}

	@Override
	public int getClosedLoopError(int pidIdx) {
		return 0;
	}

	@Override
	public double getIntegralAccumulator(int pidIdx) {
		return 0;
	}

	@Override
	public double getErrorDerivative(int pidIdx) {
		return 0;
	}

	@Override
	public void selectProfileSlot(int slotIdx, int pidIdx) {
		
	}

	@Override
	public double getClosedLoopTarget(int pidIdx) {
		return 0;
	}

	@Override
	public int getActiveTrajectoryPosition() {
		return 0;
	}

	@Override
	public int getActiveTrajectoryVelocity() {
		return 0;
	}

	@Override
	public double getActiveTrajectoryHeading() {
		return 0;
	}

	@Override
	public ErrorCode configMotionCruiseVelocity(int sensorUnitsPer100ms, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configMotionAcceleration(int sensorUnitsPer100msPerSec, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configMotionProfileTrajectoryPeriod(int baseTrajDurationMs, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode clearMotionProfileTrajectories() {
		return null;
	}

	@Override
	public int getMotionProfileTopLevelBufferCount() {
		return 0;
	}

	@Override
	public ErrorCode pushMotionProfileTrajectory(TrajectoryPoint trajPt) {
		return null;
	}

	@Override
	public boolean isMotionProfileTopLevelBufferFull() {
		return false;
	}

	@Override
	public void processMotionProfileBuffer() {
		
	}

	@Override
	public ErrorCode getMotionProfileStatus(MotionProfileStatus statusToFill) {
		return null;
	}

	@Override
	public ErrorCode clearMotionProfileHasUnderrun(int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode changeMotionControlFramePeriod(int periodMs) {
		return null;
	}

	@Override
	public ErrorCode getLastError() {
		return null;
	}

	@Override
	public ErrorCode getFaults(Faults toFill) {
		return null;
	}

	@Override
	public ErrorCode getStickyFaults(StickyFaults toFill) {
		return null;
	}

	@Override
	public ErrorCode clearStickyFaults(int timeoutMs) {
		return null;
	}

	@Override
	public int getFirmwareVersion() {
		return 0;
	}

	@Override
	public boolean hasResetOccurred() {
		return false;
	}

	@Override
	public ErrorCode configSetCustomParam(int newValue, int paramIndex, int timeoutMs) {
		return null;
	}

	@Override
	public int configGetCustomParam(int paramIndex, int timoutMs) {
		return 0;
	}

	@Override
	public ErrorCode configSetParameter(ParamEnum param, double value, int subValue, int ordinal, int timeoutMs) {
		return null;
	}

	@Override
	public ErrorCode configSetParameter(int param, double value, int subValue, int ordinal, int timeoutMs) {
		return null;
	}

	@Override
	public double configGetParameter(ParamEnum paramEnum, int ordinal, int timeoutMs) {
		return 0;
	}

	@Override
	public double configGetParameter(int paramEnum, int ordinal, int timeoutMs) {
		return 0;
	}

	@Override
	public int getBaseID() {
		return 0;
	}

	@Override
	public int getDeviceID() {
		return this._id;
	}

	@Override
	public ControlMode getControlMode() {
		return this._controlMode;
	}
} 