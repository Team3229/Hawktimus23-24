package frc.robot.Subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Inputs.RunControls;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.Linear;
import frc.robot.Subsystems.Drivetrain.ModuleOffsets;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Subsystems.Vision.Vision;
import frc.robot.Utils.RunCommand;

public class Subsystems {
    
    public static void init() {

        RunCommand.init();

        Vision.init();
        Linear.init();
        Angular.init();
        Intake.init();
        Shooter.init();
        RunControls.init();

        ModuleOffsets.init();

        SwerveKinematics.init();

        SwerveOdometry.init(new Pose2d(1, 3.5, SwerveKinematics.robotRotation));
    }

    public static void execute() {
        Vision.execute();

        RunControls.execute();
        RunCommand.execute();
    }

}