package com.team3316.kit.mocks;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * DoubleSolenoidTesting
 */
public class DoubleSolenoidTesting extends DoubleSolenoid {
    //TODO change to extend subsytems
    private int _forwadChannel, _reversChannel, _moduleNumber;
    private Value _wantedState; 

    public DoubleSolenoidTesting(int forwardChannel, int reverseChannel) {
        super(forwardChannel, reverseChannel);
        this._forwadChannel = forwardChannel;
        this._reversChannel = reverseChannel;
        this._wantedState = Value.kOff;
    }

    public DoubleSolenoidTesting(int moduleNumber, int forwardChannel, int reverseChannel) {
        super(moduleNumber, forwardChannel, reverseChannel);
        this._moduleNumber = moduleNumber;
        this._forwadChannel = forwardChannel;
        this._reversChannel = reverseChannel; 
        this._wantedState = Value.kOff;
    }

    @Override
    public Value get() {
        return this._wantedState;
    }

    @Override
    public void set(Value val) {
        this._wantedState = val;
    }
    
}