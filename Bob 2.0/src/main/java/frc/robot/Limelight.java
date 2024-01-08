package frc.robot;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
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

    public Pose3d getPose() {
        double[] array = limelight.getEntry("botpose").getDoubleArray(new double[6]);
        return new Pose3d(array[0], array[1], array[2], new Rotation3d(array[3], array[4], array[5]));
    }

}