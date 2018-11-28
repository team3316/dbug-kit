package com.team3316.kit;

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
    String userHome = System.getProperty("user.home");
    Path p = Paths.get(userHome, filename);
    return p.toAbsolutePath().toString();  }
}
