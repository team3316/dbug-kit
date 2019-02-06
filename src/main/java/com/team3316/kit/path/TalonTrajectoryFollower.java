package com.team3316.kit.path;

import com.team3316.kit.control.PIDGains;
import com.team3316.kit.motors.DBugTalon;

/**
 * A Trajectory follower that uses two master DBugTalons and a NavX as inputs and as outputs.
 * Built on top of the foundations that we built in 2018 when we used FalconPathPlanner.
 */
public class TalonTrajectoryFollower {
  private DBugTalon _leftMaster, _rightMaster;
  private Trajectory _trajectory;

  private int _currentSegment = 0;
  private PIDGains _leftGains, _rightGains, _angleGains;
}
