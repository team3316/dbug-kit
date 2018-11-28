package com.team3316.kit.tests;

import com.team3316.kit.path.Trajectory;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import junit.framework.TestCase;
import org.junit.Test;

public class TrajectoryTest extends TestCase {
  protected Trajectory trajectory;
  protected CsvContainer csv;

  @Override
  protected void setUp () throws Exception {
    String csvPath = this.getClass().getResource("/path.csv").getPath();
    File csvFile = new File(csvPath);
    this.trajectory = new Trajectory(csvFile);

    CsvReader reader = new CsvReader();
    reader.setContainsHeader(true);
    this.csv = reader.read(csvFile, StandardCharsets.UTF_8);
  }

  @Test
  public void testGetLeftTrajectory () {
    double[] leftTraj = this.trajectory.getLeftTrajectory();
    for (int i = 0; i < this.csv.getRowCount(); i++) {
      double leftDist = Double.parseDouble(this.csv.getRow(i).getField("leftdist"));
      assertEquals(leftDist, leftTraj[i]);
    }
  }
}
