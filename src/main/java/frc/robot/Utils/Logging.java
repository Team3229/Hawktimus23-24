package frc.robot.Utils;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Autonomous.Sequences.ScoreAmp;
import frc.robot.Autonomous.Sequences.ScoreSpeaker;
import frc.robot.CommandsV2.CommandScheduler;
import frc.robot.Inputs.RunControls;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Intake.Intake.IntakeStates;
import frc.robot.Subsystems.Shooter.Shooter;

public class Logging {

    private static Field2d field;

    public static void init() {
        field = new Field2d();
    }
    
    public static void log() {
        field.setRobotPose(SwerveOdometry.getPose());
        
        SmartDashboard.putData("Odometry", field);

        SmartDashboard.putNumberArray("odometry", new double[] {
			SwerveOdometry.getPose().getX(),
			SwerveOdometry.getPose().getY(),
			SwerveOdometry.getPose().getRotation().getDegrees()
		});
		
		SmartDashboard.putNumber("ArmA", Angular.encoder.getPosition());
		SmartDashboard.putNumber("frontLeft", SwerveKinematics.frontLeftModule.getAbsolutePosition().getDegrees());
		SmartDashboard.putNumber("frontRight", SwerveKinematics.frontRightModule.getAbsolutePosition().getDegrees());
		SmartDashboard.putNumber("backLeft", SwerveKinematics.backLeftModule.getAbsolutePosition().getDegrees());
		SmartDashboard.putNumber("backRight", SwerveKinematics.backRightModule.getAbsolutePosition().getDegrees());

		SmartDashboard.putBoolean("Amp Intent", Shooter.ampIntent);
		SmartDashboard.putBoolean("Has Note", Intake.hasNote);
		SmartDashboard.putBoolean("Auto Control", CommandScheduler.isActive(ScoreSpeaker.command) | CommandScheduler.isActive(ScoreAmp.command));
		SmartDashboard.putBoolean("Manip Manual Control", RunControls.manipManualControl);
		SmartDashboard.putBoolean("Auto Override", CommandScheduler.terminated);
		SmartDashboard.putBoolean("Intake Active", Intake.state == IntakeStates.intaking);
    }

}
