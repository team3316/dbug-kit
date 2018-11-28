package com.team3316.kit.config;

import com.team3316.kit.DBugLogger;
import com.team3316.kit.Util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

public class Config {
  /*
   * Singleton stuff
   */
  private static Config _config;
  public static Config getInstance () {
    return _config;
  }

  public boolean robotA; // true if it's robot A, false if it's robot B

  private Hashtable<String, Object> _variables;
  private Hashtable<String, Object> _constants;

  private Config () {
    determineRobotA();
    deserializationInit();
  }

  /*
   * Reads the file on the roborio that says whether it is robot A or B
   * Default is Robot B
   */
  private void determineRobotA () {
    String line;
    BufferedReader in;

    try {
      in = new BufferedReader(new FileReader(Util.getPathForFile("robot-name.txt")));
      line = in.readLine();
      this.robotA = line.equals("Robot A");
      DBugLogger.getInstance().info(" This is Robot " + (this.robotA ? "A" : "B"));
    } catch (FileNotFoundException e) {
      DBugLogger.getInstance().severe(e);
      System.exit(1);
    } catch (Exception e) {
      DBugLogger.getInstance().severe(e);
    }
  }

  public void add (String key, Object value) {
    addToVariables(key, value);
  }

  /**
   * Returns the value attached to a requested key
   *
   * @param key the key to look for
   * @return returns the corresponding value
   * @throws ConfigException if the key does not exist
   */
  public Object get (String key) throws ConfigException {
    if (this._constants.containsKey(key)) {
      return this._constants.get(key);
    } else if (this._variables.containsKey(key)) {
      return this._variables.get(key);
    } else {
      throw new ConfigException(key);
    }
  }

  private void addToVariables (String key, Object value) {
    if (this._variables.containsKey(key)) {
      this._variables.replace(key, value);
      DBugLogger.getInstance().info("replaced " + key + " in variables hashtable");
    } else {
      this._variables.put(key, value);
      DBugLogger.getInstance().info("added " + key + " in variables hashtable");
    }
  }

  @SuppressWarnings("unchecked")
  private void deserializationInit () {
    String filename = this.robotA ? "configFileA" : "configFileB";
    String configPath = Util.getPathForFile("config/" + filename + ".ser");

    try {
      FileInputStream in = new FileInputStream(configPath);
      ObjectInputStream input = new ObjectInputStream(in);

      this._constants = (Hashtable<String, Object>) input.readObject();
      this._variables = (Hashtable<String, Object>) input.readObject();

      Set<Entry<String, Object>> set;

      set = this._constants.entrySet();
      DBugLogger.getInstance().info(" Logging Constants");
      for (Entry<String, Object> entry : set) {
        DBugLogger.getInstance().info(" Key = " + entry.getKey() + " Value = " + entry.getValue());
      }

      set = this._variables.entrySet();
      DBugLogger.getInstance().info(" Logging Variables");
      for (Entry<String, Object> entry : set) {
        DBugLogger.getInstance().info(" Key = " + entry.getKey() + " Value = " + entry.getValue());
      }

      input.close();
    } catch (Exception e) {
      DBugLogger.getInstance().severe(e);
    }
  }
}
