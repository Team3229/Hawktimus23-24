// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drive;

import java.util.function.Supplier;

import com.choreo.lib.Choreo;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IDConstants;
import frc.robot.constants.PIDConstants;
import frc.robot.constants.PreferenceKeys;

/** Represents a swerve drive style drivetrain. */
public class DriveSubsystem extends SubsystemBase {

	public static final double kMaxSpeed = 3.0; // 3 meters per second
	public static final double kMaxAngularSpeed = Math.PI; // 1/2 rotation per second

	private final Translation2d m_frontLeftLocation = new Translation2d(0.381, 0.381);
	private final Translation2d m_frontRightLocation = new Translation2d(0.381, -0.381);
	private final Translation2d m_backLeftLocation = new Translation2d(-0.381, 0.381);
	private final Translation2d m_backRightLocation = new Translation2d(-0.381, -0.381);

	private final SwerveModule m_frontLeft = new SwerveModule(IDConstants.FL_DRIVE, IDConstants.FL_ANGLE, IDConstants.FL_ABS);
	private final SwerveModule m_frontRight = new SwerveModule(IDConstants.FR_DRIVE, IDConstants.FR_ANGLE, IDConstants.FR_ABS);
	private final SwerveModule m_backLeft = new SwerveModule(IDConstants.BL_DRIVE, IDConstants.BL_ANGLE, IDConstants.BL_ABS);
	private final SwerveModule m_backRight = new SwerveModule(IDConstants.BR_DRIVE, IDConstants.BR_ANGLE, IDConstants.BR_ABS);

	private double pDrive = PIDConstants.moduleDrivekP;
	private double iDrive = PIDConstants.moduleDrivekI;
	private double dDrive = PIDConstants.moduleDrivekD;
	private double pAngle = PIDConstants.moduleAnglekP;
	private double iAngle = PIDConstants.moduleAnglekI;
	private double dAngle = PIDConstants.moduleAnglekD;
	private double maxAccelDrive = SwerveModule.kModuleMaxLinearAcceleration;
	private double maxVelDrive = SwerveModule.kModuleMaxLinearAcceleration;

	private final AHRS m_gyro = new AHRS(Port.kMXP);

	private final SwerveDriveKinematics m_kinematics =
		new SwerveDriveKinematics(
			m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation);

	private final SwerveDriveOdometry m_odometry =
		new SwerveDriveOdometry(
			m_kinematics,
			m_gyro.getRotation2d(),
			new SwerveModulePosition[] {
			m_frontLeft.getPosition(),
			m_frontRight.getPosition(),
			m_backLeft.getPosition(),
			m_backRight.getPosition()
			});
	
	private final PIDController xController = new PIDController(PIDConstants.transkP, PIDConstants.transkI, PIDConstants.transkD);
	private final PIDController yController = new PIDController(PIDConstants.transkP, PIDConstants.transkI, PIDConstants.transkD);
	private final PIDController rotController = new PIDController(PIDConstants.rotkP, PIDConstants.rotkI, PIDConstants.rotkD);
	private Alliance alliance;

	private Command autoCommand;

	public DriveSubsystem(Supplier<Double> x, Supplier<Double> y, Supplier<Double> z) {
		m_gyro.reset();
		this.setDefaultCommand(driveCommand(x, y, z));

		Preferences.initDouble(PreferenceKeys.P_DRIVE, PIDConstants.moduleDrivekP);
		Preferences.initDouble(PreferenceKeys.I_DRIVE, PIDConstants.moduleDrivekI);
		Preferences.initDouble(PreferenceKeys.D_DRIVE, PIDConstants.moduleDrivekD);

		Preferences.initDouble(PreferenceKeys.P_ANGLE, PIDConstants.moduleAnglekP);
		Preferences.initDouble(PreferenceKeys.I_ANGLE, PIDConstants.moduleAnglekI);
		Preferences.initDouble(PreferenceKeys.D_ANGLE, PIDConstants.moduleAnglekD);

		Preferences.initDouble(PreferenceKeys.MAX_ACCEL_DRIVE, SwerveModule.kModuleMaxLinearAcceleration);
		Preferences.initDouble(PreferenceKeys.MAX_VEL_DRIVE, SwerveModule.kModuleMaxLinearAcceleration);

	}

	public void generateAuto(String pathName) {
		var trajectory = Choreo.getTrajectory(pathName);

		autoCommand = Choreo.choreoSwerveCommand(
			trajectory, 
			m_odometry::getPoseMeters,
			xController,
			yController,
			rotController,
			this::drive,
			() -> {

				DriverStation.getAlliance().ifPresent((Alliance alliance) -> {
					this.alliance = alliance;
				});

				return (alliance == Alliance.Red);
			},
			this
		);
	}

	public void executeAuto() {
		autoCommand.schedule();
	}

	/**
	 * Method to drive the robot using joystick info.
	 *
	 * @param xSpeed Speed of the robot in the x direction (forward).
	 * @param ySpeed Speed of the robot in the y direction (sideways).
	 * @param rot Angular rate of the robot.
	 * @param fieldRelative Whether the provided x and y speeds are relative to the field.
	 */
	public void drive(
			double xSpeed, double ySpeed, double rot, boolean fieldRelative, double periodSeconds) {
		var swerveModuleStates =
			m_kinematics.toSwerveModuleStates(
				ChassisSpeeds.discretize(
					fieldRelative
						? ChassisSpeeds.fromFieldRelativeSpeeds(
							xSpeed, ySpeed, rot, m_gyro.getRotation2d())
						: new ChassisSpeeds(xSpeed, ySpeed, rot),
					periodSeconds));
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);
		m_frontLeft.setDesiredState(swerveModuleStates[0]);
		m_frontRight.setDesiredState(swerveModuleStates[1]);
		m_backLeft.setDesiredState(swerveModuleStates[2]);
		m_backRight.setDesiredState(swerveModuleStates[3]);
	}

	public void drive(ChassisSpeeds speeds) {
		var swerveModuleStates =
			m_kinematics.toSwerveModuleStates(
				ChassisSpeeds.discretize(
					speeds, 1/50
				));
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);
		m_frontLeft.setDesiredState(swerveModuleStates[0]);
		m_frontRight.setDesiredState(swerveModuleStates[1]);
		m_backLeft.setDesiredState(swerveModuleStates[2]);
		m_backRight.setDesiredState(swerveModuleStates[3]);
	}

	/** Updates the field relative position of the robot. */
	public void updateOdometry() {
		m_odometry.update(
			m_gyro.getRotation2d(),
			new SwerveModulePosition[] {
				m_frontLeft.getPosition(),
				m_frontRight.getPosition(),
				m_backLeft.getPosition(),
				m_backRight.getPosition()
			});
	}

	public Command driveCommand(Supplier<Double> x, Supplier<Double> y, Supplier<Double> z) {

		Command out = new Command() {
			@Override public void execute() {
			drive(x.get(), y.get(), z.get(), true, 1/50);
			}

			@Override public boolean isFinished() {return false;}
		};

		out.addRequirements(this);

		return out;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("fL", () -> m_frontLeft.getState().angle.getDegrees(), null);
		builder.addDoubleProperty("fR", () -> m_frontRight.getState().angle.getDegrees(), null);
		builder.addDoubleProperty("bL", () -> m_backLeft.getState().angle.getDegrees(), null);
		builder.addDoubleProperty("bR", () -> m_backRight.getState().angle.getDegrees(), null);
	}

	public void loadPreferences() {
		// Load all PID constants once
		pDrive = Preferences.getDouble(PreferenceKeys.P_DRIVE, PIDConstants.moduleDrivekP);
		iDrive = Preferences.getDouble(PreferenceKeys.I_DRIVE, PIDConstants.moduleDrivekI);
		dDrive = Preferences.getDouble(PreferenceKeys.D_DRIVE, PIDConstants.moduleDrivekD);
		pAngle = Preferences.getDouble(PreferenceKeys.P_ANGLE, PIDConstants.moduleAnglekP);
		iAngle = Preferences.getDouble(PreferenceKeys.I_ANGLE, PIDConstants.moduleAnglekI);
		dAngle = Preferences.getDouble(PreferenceKeys.D_ANGLE, PIDConstants.moduleAnglekD);
		maxAccelDrive = Preferences.getDouble(PreferenceKeys.MAX_ACCEL_DRIVE, SwerveModule.kModuleMaxLinearAcceleration);
		maxVelDrive = Preferences.getDouble(PreferenceKeys.MAX_VEL_DRIVE, SwerveModule.kModuleMaxLinearAcceleration);
	
		// Configure all four swerve modules with the same values
		m_frontLeft.configurePID(pDrive, iDrive, dDrive, pAngle, iAngle, dAngle, maxAccelDrive, maxVelDrive);
		m_frontRight.configurePID(pDrive, iDrive, dDrive, pAngle, iAngle, dAngle, maxAccelDrive, maxVelDrive);
		m_backLeft.configurePID(pDrive, iDrive, dDrive, pAngle, iAngle, dAngle, maxAccelDrive, maxVelDrive);
		m_backRight.configurePID(pDrive, iDrive, dDrive, pAngle, iAngle, dAngle, maxAccelDrive, maxVelDrive);
	}	

}
