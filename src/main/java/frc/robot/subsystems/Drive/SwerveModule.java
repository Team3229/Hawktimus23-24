// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drive;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.constants.PIDConstants;

public class SwerveModule {
  private static final double kWheelRadius = 0.0508;

  private static final double kAngleGearRatio = 21.42857;
  private static final double kDriveGearRatio = 6.12;

  private static final double kModuleMaxAngularVelocity = DriveSubsystem.kMaxAngularSpeed;
  private static final double kModuleMaxAngularAcceleration =
      2 * Math.PI; // radians per second squared

  private static final double kModuleMaxLinearVelocity = DriveSubsystem.kMaxSpeed;
  private static final double kModuleMaxLinearAcceleration = 1;

  private final CANSparkMax m_driveMotor;
  private final CANSparkMax m_turningMotor;

  private final RelativeEncoder m_driveEncoder;
  private final RelativeEncoder m_turningEncoder;

  // Gains are for example purposes only - must be determined for your own robot!
  private final SparkPIDController m_drivePIDController;

  // Gains are for example purposes only - must be determined for your own robot!
  private final SparkPIDController m_turningPIDController;

  private final CANcoder m_turningAbsoluteEncoder;

  /**
   * Constructs a SwerveModule with a drive motor, turning motor, drive encoder and turning encoder.
   *
   * @param driveMotorChannel PWM output for the drive motor.
   * @param turningMotorChannel PWM output for the turning motor.
   * @param driveEncoderChannelA DIO input for the drive encoder channel A
   * @param driveEncoderChannelB DIO input for the drive encoder channel B
   * @param turningEncoderChannelA DIO input for the turning encoder channel A
   * @param turningEncoderChannelB DIO input for the turning encoder channel B
   */
  public SwerveModule(
      int driveMotorChannel,
      int turningMotorChannel,
      int absoluteEncoderChannel) {
    m_driveMotor = new CANSparkMax(driveMotorChannel, MotorType.kBrushless);
    m_turningMotor = new CANSparkMax(turningMotorChannel, MotorType.kBrushless);

    m_driveEncoder = m_driveMotor.getEncoder();
    m_turningEncoder = m_turningMotor.getEncoder();

    m_turningAbsoluteEncoder = new CANcoder(absoluteEncoderChannel);

    m_drivePIDController = m_driveMotor.getPIDController();
    m_turningPIDController = m_turningMotor.getPIDController();

    m_driveEncoder.setPositionConversionFactor(2 * Math.PI * kWheelRadius / kDriveGearRatio);
    m_driveEncoder.setVelocityConversionFactor(2 * Math.PI * kWheelRadius / 60 / kDriveGearRatio);

    // Set the distance (in this case, angle) in radians per pulse for the turning encoder.
    // This is the the angle through an entire rotation (2 * pi) divided by the
    // encoder resolution.
    m_turningEncoder.setPositionConversionFactor(2 * Math.PI / kAngleGearRatio);

    // Limit the PID Controller's input range between -pi and pi and set the input
    // to be continuous.
    m_turningPIDController.setPositionPIDWrappingMinInput(-Math.PI);
    m_turningPIDController.setPositionPIDWrappingMaxInput(Math.PI);
    m_turningPIDController.setPositionPIDWrappingEnabled(true);

    m_turningPIDController.setSmartMotionMaxAccel(kModuleMaxAngularAcceleration, 0);
    m_turningPIDController.setSmartMotionMaxVelocity(kModuleMaxAngularVelocity, 0);

    m_drivePIDController.setSmartMotionMaxAccel(kModuleMaxLinearAcceleration, 0);
    m_drivePIDController.setSmartMotionMaxVelocity(kModuleMaxLinearVelocity, 0);

    m_turningPIDController.setP(PIDConstants.moduleAnglekP);
    m_turningPIDController.setI(PIDConstants.moduleAnglekI);
    m_turningPIDController.setD(PIDConstants.moduleAnglekD);
    m_turningPIDController.setSmartMotionAllowedClosedLoopError(PIDConstants.moduleAnglekAllowedError, 0);
    
    m_drivePIDController.setP(PIDConstants.moduleDrivekP);
    m_drivePIDController.setI(PIDConstants.moduleDrivekI);
    m_drivePIDController.setD(PIDConstants.moduleDrivekD);

    m_turningEncoder.setPosition(m_turningAbsoluteEncoder.getAbsolutePosition().getValueAsDouble() * 2 * Math.PI);

    m_turningPIDController.setOutputRange(-0.75, 0.75);

  }

  /**
   * Returns the current state of the module.
   *
   * @return The current state of the module.
   */
  public SwerveModuleState getState() {
    return new SwerveModuleState(
        m_driveEncoder.getVelocity(), new Rotation2d(m_turningEncoder.getPosition()));
  }

  /**
   * Returns the current position of the module.
   *
   * @return The current position of the module.
   */
  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
        m_driveEncoder.getPosition(), new Rotation2d(m_turningEncoder.getPosition()));
  }

  /**
   * Sets the desired state for the module.
   *
   * @param desiredState Desired state with speed and angle.
   */
  public void setDesiredState(SwerveModuleState desiredState) {

    var encoderRotation = new Rotation2d(m_turningEncoder.getPosition());

    // Optimize the reference state to avoid spinning further than 90 degrees
    SwerveModuleState state = SwerveModuleState.optimize(desiredState, encoderRotation);

    // Scale speed by cosine of angle error. This scales down movement perpendicular to the desired
    // direction of travel that can occur when modules change directions. This results in smoother
    // driving.
    state.speedMetersPerSecond *= state.angle.minus(encoderRotation).getCos();

    // Calculate the drive output from the drive PID controller.
    // m_drivePIDController.setReference(state.speedMetersPerSecond, ControlType.kVelocity);

    // Calculate the turning motor output from the turning PID controller.
    m_turningPIDController.setReference(state.angle.getRadians(), ControlType.kSmartMotion);

  }
}
