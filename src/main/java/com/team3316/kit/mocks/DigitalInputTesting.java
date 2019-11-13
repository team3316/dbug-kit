package com.team3316.kit.mocks;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * DigitalInputTesting
 */
public class DigitalInputTesting extends DigitalInput {

  public boolean state = false;

  DigitalInputTesting(int id) {
    super(id);
  }

  public boolean get() {
    return this.state;
  }

}