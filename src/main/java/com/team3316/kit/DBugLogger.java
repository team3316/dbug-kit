package com.team3316.kit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A custom logging class for logging stuff on the RoboRIO.
 * Uses the singleton paradigm.
 * Example usage:
 * <code>
 *   DBugLogger logger = DBugLogger.getInstance();
 *   logger.info("Hey look I'm stupid!");
 * </code>
 */
public class DBugLogger extends Logger {
  /*
   * Singleton Stuff
   */
  private static DBugLogger _logger = new DBugLogger();

  public static DBugLogger getInstance () {
    return _logger;
  }

  /*
   * Class Members
   */
  public static String robotName = ""; // Change this if you want to
  private final static SimpleDateFormat _recordDateFormatter = new SimpleDateFormat("[dd/MM/yyyy][HH:mm:ss]");

  /**
   * Custom logging message formatter.
   */
  private class DBugFormatter extends Formatter {
    /**
     * Formats the current logged message in the following format:
     * [dd/MM/yyyy][hh:mm:ss][Class.method():LEVEL] {{ message }}
     * @param record The current record message
     * @return A formatted string, according to the aforementioned format
     */
    @Override
    public String format (LogRecord record) {
      long time = record.getMillis();
      String dateString = DBugLogger._recordDateFormatter.format(new Date(time));

      String methodString = "["
        + record.getSourceClassName()
        + "."
        + record.getSourceMethodName()
        + "():"
        + record.getLevel()
        + "]";

      return dateString + methodString + " " + record.getMessage() + "\n";
    }
  }

  /**
   * Constructs a new DBug logger instance.
   * This is only used in the singleton construction and shouldn't be used anywhere else.
   */
  private DBugLogger () {
    super(DBugLogger.robotName, null);

    // TODO - Need to understand why this is happening, this is taken from 2015
    Handler[] handlers = this.getHandlers();
    for (int i = 0; i < handlers.length; i++) {
      handlers[i].setLevel(Level.FINEST);
    }
    this.setLevel(Level.FINEST);
    this.setUseParentHandlers(true); // Disables console output if 'false' is given as a parameter

    try {
      Date currentDate = Calendar.getInstance().getTime();
      SimpleDateFormat fileDateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
      String timestamp = fileDateFormatter.format(currentDate);

      // TODO - Add writing to USB stick
      FileHandler fh = new FileHandler("%h/logs/logFile-" + timestamp + "-%u-%g.log", 1024 * 200, 5, true);
      this.addHandler(fh);

      DBugFormatter formatter = new DBugFormatter();
      fh.setFormatter(formatter);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Logs an exception to the SEVERE level log.
   * @param e The thrown exception to log
   */
  public void severe (Exception e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    String exceptionStackTrace = sw.toString();
    super.severe(e.getMessage() + ":" + exceptionStackTrace);
  }

  /**
   * Logs an object's toString() to the FINEST level log.
   * @param obj the object to log
   */
  public void finest (Object obj) {
    super.finest(obj.toString());
  }

  /**
   * Logs an object's toString() to the FINER level log.
   * @param obj the object to log
   */

  public void finer (Object obj) {
    super.finer(obj.toString());
  }

  /**
   * Logs an object's toString() to the FINE level log.
   * @param obj the object to log
   */
  public void fine (Object obj) {
    super.fine(obj.toString());
  }

  /**
   * Logs an object's toString() to the INFO level log.
   * @param obj the object to log
   */
  public void info (Object obj) {
    super.info(obj.toString());
  }

  /**
   * Logs an object's toString() to the CONFIG level log.
   * @param obj the object to log
   */
  public void config (Object obj) {
    super.config(obj.toString());
  }
}
