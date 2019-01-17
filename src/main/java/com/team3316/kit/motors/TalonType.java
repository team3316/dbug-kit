package com.team3316.kit.motors;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

public enum TalonType {
  REGULAR,
  CLOSED_LOOP_QUAD,
  CLOSED_LOOP_QUAD_ABS,
  CLOSED_LOOP_MAG,
  CLOSED_LOOP_MAG_ABS,
  FOLLOWER;

  public String getConfigLabel() {
    switch (this) {
      case FOLLOWER: return "talonConfig.follower";
      case CLOSED_LOOP_QUAD:
      case CLOSED_LOOP_QUAD_ABS:
        return "talonConfig.closedLoop.quad";
      case CLOSED_LOOP_MAG:
      case CLOSED_LOOP_MAG_ABS:
        return "talonConfig.closedLoop.mag";
      case REGULAR:
      default:
        return "talonConfig.regular";
    }
  }

  public FeedbackDevice getFeedbackDevice() {
    switch (this) {
      case CLOSED_LOOP_MAG:
        return FeedbackDevice.CTRE_MagEncoder_Relative;
      case CLOSED_LOOP_MAG_ABS:
        return FeedbackDevice.CTRE_MagEncoder_Absolute;
      case CLOSED_LOOP_QUAD:
      case CLOSED_LOOP_QUAD_ABS:
        return FeedbackDevice.QuadEncoder;
      default:
        return null;
    }
  }

  public boolean isClosedLoop() {
    return this == CLOSED_LOOP_MAG
        || this == CLOSED_LOOP_QUAD
        || this == CLOSED_LOOP_MAG_ABS
        || this == CLOSED_LOOP_QUAD_ABS;
  }

  public boolean isAbsolute() {
    return this == CLOSED_LOOP_MAG_ABS || this == CLOSED_LOOP_QUAD_ABS;
  }

  public boolean isRelative() {
    return this.isClosedLoop() && !this.isAbsolute();
  }

}
