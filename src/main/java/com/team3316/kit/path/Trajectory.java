package com.team3316.kit.path;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class Trajectory {
  private Segment[] _segments;
  private int _numOfSegments;

  public Trajectory (String csvString) throws IOException {
    StringReader reader = new StringReader(csvString);
    CsvReader csvReader = new CsvReader();
    csvReader.setContainsHeader(true);
    CsvContainer csv = csvReader.read(reader);

    this._numOfSegments = csv.getRowCount();

    this._segments = new Segment[csv.getRowCount()];
    for (int i = 0; i < this._numOfSegments; i++) {
      this._segments[i] = new Segment(csv.getRow(i));
    }
  }

  public Trajectory (File csvFile) throws IOException {
    CsvReader csvReader = new CsvReader();
    csvReader.setContainsHeader(true);
    CsvContainer csv = csvReader.read(csvFile, StandardCharsets.UTF_8);

    this._numOfSegments = csv.getRowCount();

    this._segments = new Segment[csv.getRowCount()];
    for (int i = 0; i < this._numOfSegments; i++) {
      this._segments[i] = new Segment(csv.getRow(i));
    }
  }

  public double[] getLeftTrajectory () {
    double[] leftTraj = new double[this._numOfSegments];
    for (int i = 0; i < this._numOfSegments; i++) {
      leftTraj[i] = this._segments[i].getLeftDist();
    }
    return leftTraj;
  }

  public double[] getRightTrajectory () {
    double[] rightTraj = new double[this._numOfSegments];
    for (int i = 0; i < this._numOfSegments; i++) {
      rightTraj[i] = this._segments[i].getRightDist();
    }
    return rightTraj;
  }

  public Segment getSegment (int index) {
    return this._segments[index];
  }

  public int size () {
    return this._numOfSegments;
  }
}
