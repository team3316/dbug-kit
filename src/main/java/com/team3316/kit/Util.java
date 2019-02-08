package com.team3316.kit;

import edu.wpi.first.wpilibj.Filesystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * Utility functions
 */
public class Util {
  /**
   * Clamp a given value to a given interval [low, high].
   * @param value The vaue to clamp to the bound
   * @param lowerBound The lower bound of the interval
   * @param upperBound The upper bound of the interval
   * @return A clamped value
   */
  public static double clampToBounds (double value, double lowerBound, double upperBound) {
    return Math.max(lowerBound, Math.min(value, upperBound));
  }

  /**
   * Checks whether a given value, x, is in a closed epsilon-neighborhood of limit.
   * Essentially checking that |x - L| <= Ɛ.
   * @param x The value to check the existence of in the neighborhood
   * @param limit The center of the neighborhood
   * @param epsilon The tolerance of the neighborhood
   * @return A boolean indicat
   */
  public static boolean isInNeighborhood (double x, double limit, double epsilon) {
    return limit - epsilon <= x && x <= limit + epsilon;
  }

  /**
   * Create a single valued vector
   * @param val The value to hold in the vector
   * @return A vector holding the given value in index 0
   */
  public static Vector<Double> singleValueVector (double val) {
    Vector<Double> vec = new Vector<>(1);
    vec.set(0, val);
    return vec;
  }

  /**
   * Creates a vector containing a given array's contents
   * @param arr The given array
   * @return A vector holding the given array's values
   */
  public static Vector<Double> arrayToVector (double[] arr) {
    Vector<Double> vec = new Vector<>(arr.length);
    for (int i = 0; i < arr.length; i++) {
      vec.set(i, arr[i]);
    }
    return vec;
  }

  public static String getPathForFile (String filename) {
    String deployDir = Filesystem.getDeployDirectory().getAbsolutePath();
    Path p = Paths.get(deployDir, filename);
    return p.toAbsolutePath().toString();
  }

  public static boolean isOnTarget(double value, double target, double tolerance) {
    return Math.abs(value - target) <= tolerance;
  }

  public static double calculateLeftOutput(double yDirection, double xDirection) {
    double ySign = Math.signum(yDirection), xSign = Math.signum(xDirection);
    return xSign != ySign ? yDirection : yDirection * (1 - xSign * yDirection);
  }

  public static double calculateRightOutput(double yDirection, double xDirection) {
    double ySign = Math.signum(yDirection), xSign = Math.signum(xDirection);
    return xSign == ySign ? yDirection : yDirection * (1 + xSign * yDirection);
  }
}
