package com.team3316.kit.motors;

public enum TalonType {
  REGULAR, CLOSED_LOOP_QUAD, CLOSED_LOOP_MAG, FOLLOWER;

  public String getConfigLabel() {
    switch (this) {
      case FOLLOWER: return "talonConfig.follower";
      case CLOSED_LOOP_QUAD: return "talonConfig.closedLoop.quad";
      case CLOSED_LOOP_MAG: return "talonConfig.closedLoop.mag";
      case REGULAR:
      default:
        return "talonConfig.regular";
    }
  }

  public boolean isClosedLoop() {
    return this == CLOSED_LOOP_MAG || this == CLOSED_LOOP_QUAD;
  }
}
