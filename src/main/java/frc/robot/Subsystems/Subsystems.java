package frc.robot.Subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Inputs.RunControls;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.Linear;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Utils.FieldConstants;
import frc.robot.Autonomous.Sequences.Grab;
import frc.robot.CommandsV2.CommandScheduler;

public class Subsystems {
    
    public static void init(){
        Intake.init();
		Shooter.init();
		Angular.init();
		Linear.init();
    }

    public static void update(){
        Linear.update();
		Intake.update();
		Shooter.update();
        Angular.update();


        if(RunControls.manipManualControl | Shooter.ampIntent | !Intake.hasNote | CommandScheduler.isActive(Grab.command())) return;

        //Auto arm angle + shooter rpm if in range.
        Pose2d pose = SwerveOdometry.getPose();

        if(DriverStation.getAlliance().get() == Alliance.Blue){
            if(pose.getX() < FieldConstants.BLUE_SHOOTING_LINE[0]){
                //Can shoot
                Angular.isShooting = true;
                Shooter.spinUp();
            }
        } else {
            if(pose.getX() > FieldConstants.RED_SHOOTING_LINE[0]){
                //Can shoot
                Angular.isShooting = true;
                Shooter.spinUp();
            }
        }   

    }
}
