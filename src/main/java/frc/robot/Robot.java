// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Inputs.Controller;
import frc.robot.Inputs.Controller.ControllerType;
import frc.robot.Inputs.Controller.Controls;
import frc.robot.Vision.Vision;
import frc.robot.Autonomous.PathPlanner;
import frc.robot.Drivetrain.ModuleOffsets;
import frc.robot.Drivetrain.SwerveKinematics;
import frc.robot.Drivetrain.SwerveOdometry;
	
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	Controller flightStick;

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

		ModuleOffsets.initialize();

		flightStick = new Controller(ControllerType.FlightStick, 0);

		autoManager = new PathPlanner();

		Vision.initialize();

		SwerveKinematics.initialize();
		SwerveOdometry.initialize(new Pose2d(1, 3.5, SwerveKinematics.robotRotation));

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
		logging();
	}

	/** This function is called once when autonomous is enabled. */
	@Override
	public void autonomousInit() {

		SwerveKinematics.zeroGyro();

		SwerveKinematics.configureDrivetrain();
		SwerveKinematics.configOffsets(ModuleOffsets.read());
        SwerveKinematics.chassisState = new ChassisSpeeds();

		autoCommand = autoChooser.getSelected();

		autoCommand.initialize();
		
	}

	/** This function is called periodically during autonomous. */
	@Override
	public void autonomousPeriodic() {

		SwerveOdometry.addSensorData(Vision.getPose(), Vision.getLatency(), false);

		SwerveOdometry.update(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions);

		if (!autoCommand.isFinished()) {
			autoCommand.execute();
		} else {
			SwerveKinematics.stop();
		}

		SmartDashboard.putNumberArray("odometry", new double[] {
			SwerveOdometry.getPose().getX(),
			SwerveOdometry.getPose().getY(),
			SwerveOdometry.getPose().getRotation().getDegrees()
		});

	}

	/** This function is called once when teleop is enabled. */
	@Override
	public void teleopInit() {

		// Remove for Comp
		SwerveKinematics.zeroGyro();

		SwerveOdometry.initialize(new Pose2d(Vision.getPose().getX(), Vision.getPose().getY(), SwerveKinematics.robotRotation));

		flightStick.nullControls();

		SwerveKinematics.configureDrivetrain();

		if(SmartDashboard.getBoolean("resetAngleOffsets", false)){
			SwerveKinematics.configOffsets(ModuleOffsets.calculateOffsets(SwerveKinematics.frontLeftModule.getAbsolutePosition(), SwerveKinematics.frontRightModule.getAbsolutePosition(), SwerveKinematics.backLeftModule.getAbsolutePosition(), SwerveKinematics.backRightModule.getAbsolutePosition()));
			SmartDashboard.putBoolean("resetAngleOffsets", false);
		} else {
			SwerveKinematics.configOffsets(ModuleOffsets.read());
		}

        SwerveKinematics.chassisState = new ChassisSpeeds();

	}

	/** This function is called periodically during operator control. */
	@Override
	public void teleopPeriodic() {

		flightStick.update();

		SwerveKinematics.maxChassisRotationSpeed = 10 * 0.5 * ((-(double) flightStick.get(Controls.FlightStick.Throttle)) + 1);

		SwerveKinematics.drive(
						(double) flightStick.get(Controls.FlightStick.AxisX),
						(double) flightStick.get(Controls.FlightStick.AxisY),
						(double) flightStick.get(Controls.FlightStick.AxisZ)
					);

		if((boolean) flightStick.get(Controls.FlightStick.Button10)){
			SwerveKinematics.navxGyro.zeroYaw();
		}

		// Uncomment when limelight added
		// SwerveOdometry.addSensorData(limelight.getPose());
		SwerveOdometry.update(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions);
	
		SmartDashboard.putNumberArray("odometry", new double[] {
			SwerveOdometry.getPose().getX(),
			SwerveOdometry.getPose().getY(),
			SwerveOdometry.getPose().getRotation().getDegrees()
		});

		// logging();

	}

	/** This function is called once when the robot is disabled. */
	@Override
	public void disabledInit() {
		SwerveKinematics.stop();
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

	public void logging() {

		measuredSwerveState[0] = SwerveKinematics.frontLeftModule.getPosition().getDegrees();
		measuredSwerveState[1] = -SwerveKinematics.frontLeftModule.getVelocity();
		measuredSwerveState[2] = SwerveKinematics.frontRightModule.getPosition().getDegrees();
		measuredSwerveState[3] = -SwerveKinematics.frontRightModule.getVelocity();
		measuredSwerveState[4] = SwerveKinematics.backLeftModule.getPosition().getDegrees();
		measuredSwerveState[5] = -SwerveKinematics.backLeftModule.getVelocity();
		measuredSwerveState[6] = SwerveKinematics.backRightModule.getPosition().getDegrees();
		measuredSwerveState[7] = -SwerveKinematics.backRightModule.getVelocity();
		
		SmartDashboard.putNumberArray("measuredSwerveState", measuredSwerveState);

		SmartDashboard.putNumber("navX", SwerveKinematics.robotRotation.getDegrees());

		SmartDashboard.putNumber("frontLeft", SwerveKinematics.frontLeftModule.getAbsolutePosition().getDegrees());
		SmartDashboard.putNumber("frontRight", SwerveKinematics.frontRightModule.getAbsolutePosition().getDegrees());
		SmartDashboard.putNumber("backLeft", SwerveKinematics.backLeftModule.getAbsolutePosition().getDegrees());
		SmartDashboard.putNumber("backRight", SwerveKinematics.backRightModule.getAbsolutePosition().getDegrees());
		SmartDashboard.putNumber("frontLefte", SwerveKinematics.frontLeftModule.getPosition().getDegrees());
		SmartDashboard.putNumber("frontRighte", SwerveKinematics.frontRightModule.getPosition().getDegrees());
		SmartDashboard.putNumber("backLefte", SwerveKinematics.backLeftModule.getPosition().getDegrees());
		SmartDashboard.putNumber("backRighte", SwerveKinematics.backRightModule.getPosition().getDegrees());

	}
}
