package frc.robot.Utils;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Inputs.RunControls;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;

public class Logging {

    private static Field2d field;

    public static void init() {
        field = new Field2d();
    }
    
    public static void log() {
        field.setRobotPose(SwerveOdometry.getPose());
        
        SmartDashboard.putData("Odometry", field);
		
		SmartDashboard.putNumber("ArmA", Angular.encoder.getPosition());

		SmartDashboard.putBoolean("Has Note", Intake.hasNote);
		SmartDashboard.putBoolean("Manip Manual Control", RunControls.manipManualControl);
    }

}
