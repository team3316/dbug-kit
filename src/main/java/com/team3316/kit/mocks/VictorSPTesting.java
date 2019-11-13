package com.team3316.kit.mocks;

import edu.wpi.first.wpilibj.VictorSP;

/**
 * VictorSPXTesting
 */
public class VictorSPTesting extends VictorSP {

  private int _id;
  private double _demand;

  public VictorSPTesting(int id) {
		super(id);
    this._id = id;
  }

	public void set(double val) {
		this._demand = val;
	}

	public double get() {
		return this._demand;
	}

	public int getID() {
		return this._id;
	}

} 