package frc.robot.subsystems.drive;

import com.ctre.phoenix6.configs.MagnetSensorConfigs;
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

    // Constants related to gear ratios and limits
    private static final double kWheelRadius = 0.0508;
    private static final double kAngleGearRatio = 21.42857;
    private static final double kDriveGearRatio = 6.12;
    private static final double kDriveFreeSpeed = 5.12;

    // Motor and Encoder objects
    private final CANSparkMax m_driveMotor;
    private final CANSparkMax m_turningMotor;
    private final RelativeEncoder m_driveEncoder;
    private final RelativeEncoder m_turningEncoder;
    private final SparkPIDController m_drivePIDController;
    private final SparkPIDController m_turningPIDController;
    private final CANcoder m_turningAbsoluteEncoder;

    /**
     * Constructs a SwerveModule with drive and turning motors, encoders, and absolute encoder.
     *
     * @param driveMotorChannel  CAN ID for the drive motor.
     * @param turningMotorChannel CAN ID for the turning motor.
     * @param absoluteEncoderChannel CAN ID for the absolute encoder.
     */
    public SwerveModule(int driveMotorChannel, int turningMotorChannel, int absoluteEncoderChannel, boolean inverted, double offset) {
        // Initialize hardware components
        m_driveMotor = new CANSparkMax(driveMotorChannel, MotorType.kBrushless);
        m_turningMotor = new CANSparkMax(turningMotorChannel, MotorType.kBrushless);

        m_driveEncoder = m_driveMotor.getEncoder();
        m_turningEncoder = m_turningMotor.getEncoder();

        m_turningAbsoluteEncoder = new CANcoder(absoluteEncoderChannel);

        setEncoderOffset(offset);

        m_drivePIDController = m_driveMotor.getPIDController();
        m_turningPIDController = m_turningMotor.getPIDController();

        m_driveMotor.setInverted(inverted);

        // Initialize the module's configuration
        configureEncoders();
        configurePIDControllers();
        initializeTurningEncoderPosition();
    }

    /** 
     * Configures encoders for position and velocity conversion. 
     */
    private void configureEncoders() {
        // Configure drive encoder conversion factors
        m_driveEncoder.setPositionConversionFactor(2 * Math.PI * kWheelRadius / kDriveGearRatio);
        m_driveEncoder.setVelocityConversionFactor(2 * Math.PI * kWheelRadius / 60 / kDriveGearRatio);

        // Configure turning encoder conversion factor for position
        m_turningEncoder.setPositionConversionFactor(2 * Math.PI / kAngleGearRatio);
    }

    /**
     * Configures the PID controllers for both the drive and turning motors.
     */
    private void configurePIDControllers() {
        // Configure turning PID controller
        m_turningPIDController.setPositionPIDWrappingMinInput(-Math.PI);
        m_turningPIDController.setPositionPIDWrappingMaxInput(Math.PI);
        m_turningPIDController.setPositionPIDWrappingEnabled(true);

        m_turningPIDController.setP(PIDConstants.P_ANGLE);
        m_turningPIDController.setI(PIDConstants.I_ANGLE);
        m_turningPIDController.setD(PIDConstants.D_ANGLE);
        m_turningPIDController.setOutputRange(-0.75, 0.75);

        // Configure drive PID controller
        m_drivePIDController.setP(PIDConstants.P_DRIVE);
        m_drivePIDController.setI(PIDConstants.I_DRIVE);
        m_drivePIDController.setD(PIDConstants.D_DRIVE);
        m_drivePIDController.setFF(1/kDriveFreeSpeed);
    }

    /**
     * Initializes the turning encoder position using the absolute encoder value.
     */
    private void initializeTurningEncoderPosition() {

        // HERE
        m_turningEncoder.setPosition(
            m_turningAbsoluteEncoder.getPosition().getValueAsDouble() * 2 * Math.PI
        );
    }

    /**
     * Returns the current state of the swerve module.
     *
     * @return The current state of the module with velocity and rotation.
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(
                m_driveEncoder.getVelocity(), 
                new Rotation2d(m_turningEncoder.getPosition())
        );
    }

    /**
     * Returns the current position of the swerve module.
     *
     * @return The current position of the module with encoder positions.
     */
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
                m_driveEncoder.getPosition(), 
                new Rotation2d(m_turningEncoder.getPosition())
        );
    }

    public void setEncoderOffset(double offset) {
        this.m_turningAbsoluteEncoder.getConfigurator().apply(new MagnetSensorConfigs().withMagnetOffset(offset));
    }

    /**
     * Sets the desired state for the module by optimizing and controlling drive and turning motors.
     *
     * @param desiredState Desired state with speed and angle.
     */
    public void setDesiredState(SwerveModuleState desiredState) {
        Rotation2d currentRotation = new Rotation2d(m_turningEncoder.getPosition());

        // Optimize the desired state to minimize rotation
        SwerveModuleState optimizedState = SwerveModuleState.optimize(desiredState, currentRotation);

        // Scale speed to adjust for angle error
        optimizedState.speedMetersPerSecond *= optimizedState.angle.minus(currentRotation).getCos();

        // Set the desired speed for the drive motor
        m_drivePIDController.setReference(optimizedState.speedMetersPerSecond, ControlType.kVelocity);

        // Set the desired angle for the turning motor
        m_turningPIDController.setReference(optimizedState.angle.getRadians(), ControlType.kPosition);
    }
}
