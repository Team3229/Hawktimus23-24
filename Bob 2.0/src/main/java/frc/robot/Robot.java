// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.DriveSystem.Swerve.SwerveKinematics;
import frc.robot.DriveSystem.Swerve.SwerveOdometry;
import frc.robot.DriveSystem.Inputs.Controller;
import frc.robot.DriveSystem.Inputs.Controller.ControllerType;
import frc.robot.DriveSystem.Inputs.Controller.Controls;
	
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	Controller flightStick;
	Controller xboxController;

	Command autoCommand;
	PathPlanner autoManager;

	Limelight limelight;

	double[] desiredSwerveState = {0,0,0,0,0,0,0,0};
	double[] measuredSwerveState = {0,0,0,0,0,0,0,0};

	/**
	 * This function is run when the robot is first started up and should be used for any
	 * initialization code.
	 */
	@Override
	public void robotInit() {

		flightStick = new Controller(ControllerType.FlightStick, 0);
		xboxController = new Controller(ControllerType.XboxController, 1);

		autoManager = new PathPlanner();

		limelight = new Limelight();

		SwerveKinematics.initialize();

	}

	/**
	 * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
	 * that you want ran during disabled, autonomous, teleoperated and test.
	 *
	 * <p>This runs after the mode specific periodic functions, but before LiveWindow and
	 * SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic() {}

	/** This function is called once when autonomous is enabled. */
	@Override
	public void autonomousInit() {

		NamedCommands.registerCommand("doshoot", new CommandBase() {});
		
		autoCommand = autoManager.getCommand("Example Auto");
		
	}

	/** This function is called periodically during autonomous. */
	@Override
	public void autonomousPeriodic() {

		SwerveOdometry.addSensorData(limelight.getPose().toPose2d());

		autoCommand.execute();
	}

	/** This function is called once when teleop is enabled. */
	@Override
	public void teleopInit() {

		flightStick.nullControls();
		xboxController.nullControls();

		SwerveKinematics.configureEncoders();
		SwerveKinematics.configureMotors();
		SwerveKinematics.configurePID();
		if (SmartDashboard.getBoolean("resetAngleOffsets", false)) {
			SwerveKinematics.configOffsets(SwerveKinematics.offsets
			.calculateOffsets(
				SwerveKinematics.frontLeftModule.getAbsolutePosition(), 
				SwerveKinematics.frontRightModule.getAbsolutePosition(), 
				SwerveKinematics.backLeftModule.getAbsolutePosition(), 
				SwerveKinematics.backRightModule.getAbsolutePosition()
			));			
			System.out.println("Reset Offsets");
			SmartDashboard.putBoolean("resetAngleOffsets", false);
		}

		SwerveKinematics.configOffsets();
	}

	/** This function is called periodically during operator control. */
	@Override
	public void teleopPeriodic() {

		flightStick.update();
		xboxController.update();

		SwerveKinematics.drive(
						(double) flightStick.get(Controls.FlightStick.AxisX),
						(double) flightStick.get(Controls.FlightStick.AxisY),
						(double) flightStick.get(Controls.FlightStick.AxisZ)
					);

		//logging (move to seperate method once confirmed works)

		SmartDashboard.putNumber("setAngle", SwerveKinematics.backLeftModule.currentState.angle.getDegrees());
		SmartDashboard.putNumber("measuredAngle", SwerveKinematics.backLeftModule.getPosition().getDegrees());
		SmartDashboard.putNumber("setSpeed", SwerveKinematics.backLeftModule.getVelocity());
		SmartDashboard.putNumber("measuredSpeed", SwerveKinematics.backLeftModule.currentState.speedMetersPerSecond);

		desiredSwerveState[0] = SwerveKinematics.frontLeftModule.currentState.angle.getDegrees();
		desiredSwerveState[1] = SwerveKinematics.frontLeftModule.currentState.speedMetersPerSecond;
		desiredSwerveState[2] = SwerveKinematics.frontRightModule.currentState.angle.getDegrees();
		desiredSwerveState[3] = SwerveKinematics.frontRightModule.currentState.speedMetersPerSecond;
		desiredSwerveState[4] = SwerveKinematics.backLeftModule.currentState.angle.getDegrees();
		desiredSwerveState[5] = SwerveKinematics.backLeftModule.currentState.speedMetersPerSecond;
		desiredSwerveState[6] = SwerveKinematics.backRightModule.currentState.angle.getDegrees();
		desiredSwerveState[7] = SwerveKinematics.backRightModule.currentState.speedMetersPerSecond;

		measuredSwerveState[0] = SwerveKinematics.frontLeftModule.getPosition().getDegrees();
		measuredSwerveState[1] = SwerveKinematics.frontLeftModule.getVelocity();
		measuredSwerveState[2] = SwerveKinematics.frontRightModule.getPosition().getDegrees();
		measuredSwerveState[3] = SwerveKinematics.frontRightModule.getVelocity();
		measuredSwerveState[4] = SwerveKinematics.backLeftModule.getPosition().getDegrees();
		measuredSwerveState[5] = SwerveKinematics.backLeftModule.getVelocity();
		measuredSwerveState[6] = SwerveKinematics.backRightModule.getPosition().getDegrees();
		measuredSwerveState[7] = SwerveKinematics.backRightModule.getVelocity();

		SmartDashboard.putNumberArray("desiredSwerveState", desiredSwerveState);
		SmartDashboard.putNumberArray("measuredSwerveState", measuredSwerveState);

	}

	/** This function is called once when the robot is disabled. */
	@Override
	public void disabledInit() {}

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
