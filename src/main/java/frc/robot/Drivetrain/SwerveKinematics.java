package frc.robot.Drivetrain;

import com.kauailabs.navx.frc.AHRS;
import com.pathplanner.lib.util.PIDConstants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.SPI;

public class SwerveKinematics {

    // Swerve Modules
    public static SwerveModule frontLeftModule;
    public static SwerveModule frontRightModule;
    public static SwerveModule backLeftModule;
    public static SwerveModule backRightModule;

    /**PID values for the swerve modules' angular motion. (Automatically populated with our constants we used for the 22-23 season) */
    private static final PIDConstants anglePID = new PIDConstants(0.009, 0, 0);
    // private static final double[] anglePID = {0.01, 0.0001, 0};
    /**PID values for the swerve modules' driving motion. (Automatically populated with our constants we used for the 22-23 season) */
    private static final PIDConstants drivePID = new PIDConstants(0.1, 0, 0);
    // private static final double[] drivePID = {0.1, 0, 0};

    /**Kauai Labs NavX Gyro. */
    public static AHRS navxGyro;
    /**The current rotation of the chassis. */
    public static Rotation2d robotRotation = Rotation2d.fromDegrees(0);

    public static Rotation2d gyroOffset = Rotation2d.fromDegrees(0);
    /**
     * Robot relative mode uses the current rotation of the robot as forward, instead of the default
     * field-relative mode. This is useful for picking up objects or lining up with a game piece.
     */
    private static boolean relativeMode;

    /**An object used to calculate module velocities from overall chassis movement. */
    public static SwerveDriveKinematics kinematics;
    /**The current state of chassis velocity. */
    public static ChassisSpeeds chassisState;
    /**The current set module states. */
    private static SwerveModuleState[] moduleStates;
    /**The current set module positions. */
    public static SwerveModulePosition[] modulePositions = {new SwerveModulePosition(), new SwerveModulePosition(), new SwerveModulePosition(), new SwerveModulePosition()};

    /**The width of the robot chassis in meters. */
    public static final double robotWidth = 0.6858;
    //TOD0: Measure and set this constant
    //**The distance between the edge of the chassis to the center of a wheel in meters. */
    public static final double moduleEdgeOffset = 0.0508;
    /**The maximum speed (in meters/sec) that a singular swerve module can reach. */
    public static final double maxModuleSpeed = 20;
    /**The maximum linear speed (in meters/sec) the chassis should move at. (Automatically set for SDS MK4 L1 modules) */
    private static final double chassisSpeed = 12.5;
    /**The percentage of the Chassis Speed of which the bot runs at. */
    private static final double chassisSpeedPercentage = 1;
    /**The maximum angular speed (in radians/sec) that the chassis can rotate at. */
    public static double maxChassisRotationSpeed = 5;
    /**Whether or not to run the drive motors in brake mode. */
    private static final boolean brakeMode = true;
    
    public SwerveKinematics() {}

    /**Initializes the drivetrain. */
    public static void initialize() {

        // Replace the CAN IDs to suit your needs.
        frontLeftModule = new SwerveModule(2, 3, 1, new Translation2d((robotWidth/2), (robotWidth/2)));
        frontRightModule = new SwerveModule(14, 15, 16, new Translation2d((robotWidth/2), -(robotWidth/2)));
        backLeftModule = new SwerveModule(6, 7, 8, new Translation2d(-(robotWidth/2), (robotWidth/2)));
        backRightModule = new SwerveModule(10, 11, 9, new Translation2d(-(robotWidth/2), -(robotWidth/2)));

        configOffsets(ModuleOffsets.read());

        navxGyro = new AHRS(SPI.Port.kMXP);

        kinematics = new SwerveDriveKinematics(frontLeftModule.location, frontRightModule.location, backLeftModule.location, backRightModule.location);

        configureDrivetrain();

        modulePositions[0] = new SwerveModulePosition();
        modulePositions[1] = new SwerveModulePosition();
        modulePositions[2] = new SwerveModulePosition();
        modulePositions[3] = new SwerveModulePosition();

        chassisState = new ChassisSpeeds();
    }

    public static void configureDrivetrain() {
        configureEncoders();
        configureMotors();
        configurePID();
    }

    public static void configOffsets(Rotation2d[] offset){
        frontLeftModule.doOffset(offset[0]);
        frontRightModule.doOffset(offset[1]);
        backLeftModule.doOffset(offset[2]);
        backRightModule.doOffset(offset[3]);
    }
    /**
     * Drives the chassis given an x, y, and rotational movement speed.
     * @param X X axis speed (left/right relative to driverstation)
     * @param Y Y axis speed (forward/backward relative to driverstation)
     * @param Z Rotational speed
     */
    public static void drive(double X, double Y, double Z) {

        // If we are in robot relative mode, override the chassis rotation to 180 degrees.
        if (relativeMode) {
            robotRotation = Rotation2d.fromDegrees(180);
        } else {
            robotRotation = Rotation2d.fromDegrees(MathUtil.inputModulus(gyroOffset.getDegrees()-navxGyro.getYaw(), 0, 360));
        }

        // Calculate reverse kinematics
        chassisState = ChassisSpeeds.fromFieldRelativeSpeeds(-Y*chassisSpeed*chassisSpeedPercentage, -X*chassisSpeed*chassisSpeedPercentage, -Z*maxChassisRotationSpeed, robotRotation);
        
        moduleStates = kinematics.toSwerveModuleStates(chassisState);
        SwerveDriveKinematics.desaturateWheelSpeeds(moduleStates, maxModuleSpeed);

        // Set each module state
        frontLeftModule.setModuleState(moduleStates[0]);
        frontRightModule.setModuleState(moduleStates[1]);
        backLeftModule.setModuleState(moduleStates[2]);
        backRightModule.setModuleState(moduleStates[3]);

        frontLeftModule.updateModuleState();
        frontRightModule.updateModuleState();
        backLeftModule.updateModuleState();
        backRightModule.updateModuleState();

        frontLeftModule.updateModulePosition();
        frontRightModule.updateModulePosition();
        backLeftModule.updateModulePosition();
        backRightModule.updateModulePosition();

        modulePositions[0] = frontLeftModule.currentPosition;
        modulePositions[1] = frontRightModule.currentPosition;
        modulePositions[2] = backLeftModule.currentPosition;
        modulePositions[3] = backRightModule.currentPosition;
    }

    /**
     * Drives the chassis given a chassis relative speed
     * @param speeds (ChassisSpeeds)
     */
    public static void drive(ChassisSpeeds speeds) {

        robotRotation = Rotation2d.fromDegrees(MathUtil.inputModulus(gyroOffset.getDegrees()-navxGyro.getYaw(), 0, 360));

        chassisState = speeds;

        chassisState.omegaRadiansPerSecond *= -1;
        chassisState.vxMetersPerSecond *= -1;
        chassisState.vyMetersPerSecond *= -1;
        
        moduleStates = kinematics.toSwerveModuleStates(chassisState);
        SwerveDriveKinematics.desaturateWheelSpeeds(moduleStates, maxModuleSpeed);

        // Set each module state
        frontLeftModule.setModuleState(moduleStates[0]);
        frontRightModule.setModuleState(moduleStates[1]);
        backLeftModule.setModuleState(moduleStates[2]);
        backRightModule.setModuleState(moduleStates[3]);

        frontLeftModule.updateModuleState();
        frontRightModule.updateModuleState();
        backLeftModule.updateModuleState();
        backRightModule.updateModuleState();

        frontLeftModule.updateModulePosition();
        frontRightModule.updateModulePosition();
        backLeftModule.updateModulePosition();
        backRightModule.updateModulePosition();

        modulePositions[0] = frontLeftModule.currentPosition;
        modulePositions[1] = frontRightModule.currentPosition;
        modulePositions[2] = backLeftModule.currentPosition;
        modulePositions[3] = backRightModule.currentPosition;
    }

    public static ChassisSpeeds getSpeeds() {
        return chassisState;
    }

    public static void zeroGyro() {
        navxGyro.zeroYaw();
        robotRotation = Rotation2d.fromDegrees(0);
    }

    /**Configures each module's motors. */
    private static void configureMotors() {

        frontLeftModule.configureAngleMotor();
        frontLeftModule.configureDriveMotor(brakeMode, true);
        frontRightModule.configureAngleMotor();
        frontRightModule.configureDriveMotor(brakeMode, true);
        backLeftModule.configureAngleMotor();
        backLeftModule.configureDriveMotor(brakeMode, true);
        backRightModule.configureAngleMotor();
        backRightModule.configureDriveMotor(brakeMode, true);
    }

    /**Configures and resets the offsets of each module's encoders. */
    private static void configureEncoders() {
        frontLeftModule.configureEncoders();
        frontRightModule.configureEncoders();
        backLeftModule.configureEncoders();
        backRightModule.configureEncoders();
    }

    /**Configures each module's PID Controllers with the provided constants at the top of this class. */
    private static void configurePID() {

        frontLeftModule.configurePID(anglePID, drivePID);
        frontRightModule.configurePID(anglePID, drivePID);
        backLeftModule.configurePID(anglePID, drivePID);
        backRightModule.configurePID(anglePID, drivePID);
    }

    public static void setGyroOffset(Rotation2d offset) {
        gyroOffset = offset;
        robotRotation = Rotation2d.fromDegrees(MathUtil.inputModulus(gyroOffset.getDegrees()-navxGyro.getYaw(), 0, 360));
    }

    /**Stops every module's motors. (should not be called during driving; the motors should continue to their setpoints) */
    public static void stop() {

        frontLeftModule.stopMotors();
        frontRightModule.stopMotors();
        backLeftModule.stopMotors();
        backRightModule.stopMotors();
    }

}