package frc.robot.Drivetrain;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CANcoderConfigurator;
import com.ctre.phoenix6.hardware.CANcoder;
import com.pathplanner.lib.util.PIDConstants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkLowLevel.PeriodicFrame;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

/**
 * This class is used to represent one module of a swerve drivetrain. Included is a method to set the state
 * of the module, which passes the desired angle and speed of the module to the module's respective PID
 * controllers. Proper implementation of this class is shown in the class SwerveKinematics.
 */
public class SwerveModule {

    /**The SparkMax motor controller connected to the driving motor of a module. */
    public CANSparkMax driveMotor;
    /**The SparkMax motor controller connected to the angular motor of a module. */
    public CANSparkMax angleMotor;

    /**The CANcoder used to determine the absolute rotation of a swerve module. */
    public CANcoder absoluteEncoder;

    private CANcoderConfigurator absoluteEncoderConfigurator;
    private CANcoderConfiguration absoluteEncoderConfig;
    /**The RelativeEncoder object used for the driving motor's PID controller. */
    private RelativeEncoder driveEncoder;
    /**The RelativeEncoder object used for the angular motor's PID controller. */
    private RelativeEncoder angleEncoder;

    /**The module's driving motor's PID controller. */
    public SparkPIDController drivePIDController;
    /**The module's angular motor's PID controller. */
    public SparkPIDController anglePIDController;

    /**An object used to represent the current state of the module. */
    public SwerveModuleState currentState;
    /**An object used to represent the desired state of the module. */
    public SwerveModuleState desiredState;

    public SwerveModulePosition currentPosition;

    /**The location of the swerve module relative to robot center. (in meters) */
    public Translation2d location;

    /**The radius (in meters) of the module's wheel. */
    private static final double wheelRadius = 0.0508;

    /**The gear ratio of the angular aspect of the module. (default is from the SDS MK4 L1) */
    private static final double angleGearRatio = 21.42857;

    /**The gear ratio of the drive aspect of the module. (default is from the SDS MK4 L1) */
    private static final double driveGearRatio = 8.14;

    /**
     * Create a swerve module object.
     * @param driveID (int) The CAN ID of the driving motor's Spark Max.
     * @param angleID (int) The CAN ID of the angular motor's Spark Max.
     * @param encoderID (int) The CAN ID of the module's CANcoder.
     */
    public SwerveModule(int driveID, int angleID, int encoderID, Translation2d moduleLocation) {
        
        this.driveMotor = new CANSparkMax(driveID, MotorType.kBrushless);
        this.angleMotor = new CANSparkMax(angleID, MotorType.kBrushless);
        
        this.absoluteEncoder = new CANcoder(encoderID);
        this.absoluteEncoderConfigurator = this.absoluteEncoder.getConfigurator();
        this.absoluteEncoderConfig = new CANcoderConfiguration();
        this.driveEncoder = this.driveMotor.getEncoder();
        this.angleEncoder = this.angleMotor.getEncoder();

        this.drivePIDController = this.driveMotor.getPIDController();
        this.anglePIDController = this.angleMotor.getPIDController();

        this.location = moduleLocation;

        this.desiredState = new SwerveModuleState(0, Rotation2d.fromDegrees(0));

    }

    /**
     * Sets the state of the swerve module. (should be called periodically)
     * @param desiredState (SwerveModuleState) The desired state of the swerve module.
     */
    public void setModuleState(SwerveModuleState desiredState) {
        this.desiredState = SwerveModuleState.optimize(desiredState, getAbsolutePosition());

        // If you don't want module to move, don't force it backwards
        if (this.desiredState.speedMetersPerSecond == 0) {
            this.drivePIDController.setReference(0, ControlType.kDutyCycle);
            this.anglePIDController.setReference(0, ControlType.kDutyCycle);
        } else {
            this.drivePIDController.setReference(this.desiredState.speedMetersPerSecond, ControlType.kVelocity);
            this.anglePIDController.setReference(this.desiredState.angle.getDegrees(), ControlType.kPosition);
        }

    }

    /**
     * Returns the position of the absolute encoder.
     * @return (Rotation2d) The position of the absolute encoder
     */
    public Rotation2d getAbsolutePosition() {
        return Rotation2d.fromDegrees(MathUtil.inputModulus(this.absoluteEncoder.getAbsolutePosition().getValueAsDouble() * 360, 0, 360));
    }

    /**
     * Returns the position of the relative encoder.
     * @return (Rotation2d) The position of the relative encoder
     */
    public Rotation2d getPosition() {
        return Rotation2d.fromDegrees(MathUtil.inputModulus(this.angleEncoder.getPosition(), 0, 360));
    }

    /**
     * Returns the velocity of the swerve module in meters/second.
     * @return (double) The velocity of the module
     */
    public double getVelocity() {
        return this.driveEncoder.getVelocity();
    }

    /**Updates the module's current state (should be called periodically) */
    public void updateModuleState() {
        this.currentState = new SwerveModuleState(getVelocity(), getAbsolutePosition());
    }

    public void updateModulePosition() {
        this.currentPosition = new SwerveModulePosition(-driveEncoder.getPosition(), getPosition());
    }

    /**
     * Configures the module's driving motor.
     * @param brakeMode (boolean) Whether or not to enable brake mode on the module.
     * @param invert (boolean) Whether or not to invert the module's drive motor.
     */
    public void configureDriveMotor(boolean brakeMode, boolean invert) {

        this.driveMotor.setInverted(invert);

        if (brakeMode) {
            this.driveMotor.setIdleMode(IdleMode.kBrake);
        } else {
            this.driveMotor.setIdleMode(IdleMode.kCoast);
        }

        this.driveMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20);
        this.driveMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20);
    }

    /**Configures the module's angular motor.*/
    public void configureAngleMotor() {
        this.angleMotor.setInverted(true);
        this.angleMotor.setIdleMode(IdleMode.kCoast);
        this.angleMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20);
        this.angleMotor.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20);
    }

    /**
     * Configures all of the module's encoders
     * @param encoderOffset (Rotation2d) The module's angular offset.
     */
    public void configureEncoders() {
        this.absoluteEncoder.getAbsolutePosition().setUpdateFrequency(5);

        this.angleEncoder.setPositionConversionFactor(360/angleGearRatio);
        this.angleEncoder.setPosition(getAbsolutePosition().getDegrees());

        this.driveEncoder.setVelocityConversionFactor(((2*Math.PI*wheelRadius)/60)/driveGearRatio);

        this.driveEncoder.setPositionConversionFactor(((2*Math.PI*wheelRadius))/driveGearRatio);
        this.driveEncoder.setPosition(0);
    }

    public void doOffset(Rotation2d offset) {

        absoluteEncoderConfig.MagnetSensor.MagnetOffset = offset.getDegrees()/360;
        this.absoluteEncoderConfigurator.apply(absoluteEncoderConfig.MagnetSensor);
        this.angleEncoder.setPosition(getAbsolutePosition().getDegrees());
    }

    /**
     * Configures all of the module's PID controllers.
     * @param anglePID (PIDConstants) Angular PID values.
     * @param drivePID (PIDConstants) Drive PID values.
     */
    public void configurePID(PIDConstants anglePID, PIDConstants drivePID) {
        this.anglePIDController.setP(anglePID.kP);
        this.anglePIDController.setI(anglePID.kI);
        this.anglePIDController.setD(anglePID.kD);
        this.anglePIDController.setPositionPIDWrappingMinInput(0);
        this.anglePIDController.setPositionPIDWrappingMaxInput(360);
        this.anglePIDController.setPositionPIDWrappingEnabled(true);
        // this.anglePIDController.setSmartMotionAccelStrategy(AccelStrategy.kTrapezoidal, 0);
        // this.anglePIDController.setSmartMotionMaxAccel(angleMaxAccel, 0);
        // this.anglePIDController.setSmartMotionMaxVelocity(angleMaxVel, 0);

        this.drivePIDController.setP(drivePID.kP);
        this.drivePIDController.setI(drivePID.kI);
        this.drivePIDController.setD(drivePID.kD);
        
    }

    /**Stops the module's motors. (should not be called during driving; the motors should continue to their setpoints) */
    public void stopMotors() {
        this.angleMotor.stopMotor();
        this.driveMotor.stopMotor();

        this.anglePIDController.setReference(0, ControlType.kDutyCycle);
        this.drivePIDController.setReference(0, ControlType.kDutyCycle);
    }

}