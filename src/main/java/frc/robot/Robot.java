// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.awt.geom.Point2D;

import com.revrobotics.CANSparkBase.ControlType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Inputs.RunControls;
import frc.robot.Subsystems.Subsystems;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.Linear;
import frc.robot.Subsystems.Drivetrain.ModuleOffsets;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Intake.Intake.IntakeStates;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Subsystems.Vision.Vision;
import frc.robot.Utils.FieldConstants;
import frc.robot.Utils.Logging;
import frc.robot.Autonomous.PathPlanner;
import frc.robot.Autonomous.Sequences.ScoreAmp;
import frc.robot.Autonomous.Sequences.ScoreSpeaker;
import frc.robot.CommandsV2.CommandScheduler;
	
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	Command autoCommand;
	PathPlanner autoManager;
	SendableChooser<Command> autoChooser;

	double[] desiredSwerveState = {0,0,0,0,0,0,0,0};
	double[] measuredSwerveState = {0,0,0,0,0,0,0,0};

	/**
	 * This function is run when the robot is first started up and should be used for any
	 * initialization code.
	 */
	@Override
	public void robotInit() {

		ModuleOffsets.init();

		RunControls.init();

		Subsystems.init();

		CommandScheduler.init();

		autoManager = new PathPlanner();

		Vision.init();

		SwerveKinematics.initialize();

		SwerveOdometry.init(new Pose2d(1.35, 5.55, SwerveKinematics.robotRotation));

		Logging.init();

		autoChooser = autoManager.getDropdown();

		SmartDashboard.putData("Choose Auto", autoChooser);

	}

	/**
	 * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
	 * that you want ran during disabled, autonomous, teleoperated and test.
	 *
	 * <p>This runs after the mode specific periodic functions, but before LiveWindow and
	 * SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic() {
		Logging.log();
	}

	/** This function is called once when autonomous is enabled. */
	@Override
	public void autonomousInit() {

		SwerveKinematics.configureDrivetrain();
		SwerveKinematics.configOffsets(ModuleOffsets.read());
        SwerveKinematics.chassisState = new ChassisSpeeds();

		autoCommand = autoChooser.getSelected();

		SwerveOdometry.resetPose(new Pose2d(1.35, 5.55, SwerveKinematics.robotRotation));

		autoCommand.initialize();
		
	}

	/** This function is called periodically during autonomous. */
	@Override 
	public void autonomousPeriodic() {


		SwerveOdometry.update(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions);        
		
		if (!autoCommand.isFinished()) {
			autoCommand.execute();
		} else {
			SwerveKinematics.stop();
		}

		Subsystems.update();

	}

	/** This function is called once when teleop is enabled. */
	@Override
	public void teleopInit() {

		// Remove for Comp
		SwerveKinematics.zeroGyro();

		SwerveOdometry.init(new Pose2d(Vision.getPose().getX(), Vision.getPose().getY(), SwerveKinematics.robotRotation));

		RunControls.nullControls();

		SwerveKinematics.configureDrivetrain();

		ModuleOffsets.checkBoolean();

        SwerveKinematics.chassisState = new ChassisSpeeds();

	}

	/** This function is called periodically during operator control. */
	@Override
	public void teleopPeriodic() {

		RunControls.run();

		SwerveOdometry.update(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions);

		Subsystems.update();

		CommandScheduler.periodic();

	}

	/** This function is called once when the robot is disabled. */
	@Override
	public void disabledInit() {
		SwerveKinematics.stop();
		CommandScheduler.emptyTrashCan();
	}

	/** This function is called periodically when disabled. */
	@Override
	public void disabledPeriodic() {}

	/** This function is called once when test mode is enabled. */
	@Override
	public void testInit() {}

	/** This function is called periodically during test mode. */
	@Override
	public void testPeriodic() {}

	/** This function is called once when the robot is first started up. */
	@Override
	public void simulationInit() {}

	/** This function is called periodically whilst in simulation. */
	@Override
	public void simulationPeriodic() {}
}
