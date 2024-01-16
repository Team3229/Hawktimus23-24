package frc.robot.Vision;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    NetworkTable limelight;

    //Overloaded constructor for optional "id" parameter
    
    public Limelight() {
        limelight = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public Limelight(String id) {
        limelight = NetworkTableInstance.getDefault().getTable(id);
    }

    public Pose2d getPose() {
        double[] array = limelight.getEntry("botpose").getDoubleArray(new double[6]);
        return new Pose2d(array[0], array[1], Rotation2d.fromDegrees(MathUtil.inputModulus(array[5], 0, 360)));
    }

    public double getLatency() {
        return (limelight.getEntry("tl").getDouble(0) / 1000) + (limelight.getEntry("cl").getDouble(0) / 1000);
    }

    public boolean isValid() {
        return limelight.getEntry("tv").getInteger(0) == 1;
    }

}