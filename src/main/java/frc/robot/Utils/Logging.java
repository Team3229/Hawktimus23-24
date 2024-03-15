package frc.robot.Utils;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;

public class Logging {

    private static Field2d field;

    public static void init() {
        field = new Field2d();
    }
    
    public static void log() {
        field.setRobotPose(SwerveOdometry.getPose());
        
        SmartDashboard.putData("Odometry", field);
    }

}
