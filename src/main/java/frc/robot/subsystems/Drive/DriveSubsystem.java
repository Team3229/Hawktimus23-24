package frc.robot.subsystems.drive;

import java.util.function.Supplier;

import com.choreo.lib.Choreo;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.constants.IDConstants;
import frc.robot.constants.PIDConstants;

/** Represents a swerve drive style drivetrain. */
public class DriveSubsystem extends SubsystemBase {

    // Constants for max speed and angular velocity
    public static final double kMaxSpeed = 3.0; // 3 meters per second
    public static final double kMaxAngularSpeed = Math.PI; // 1/2 rotation per second

    // Module Locations
    private final Translation2d m_frontLeftLocation = new Translation2d(-0.2778125, -0.2778125);
    private final Translation2d m_frontRightLocation = new Translation2d(-0.2778125, 0.2778125);
    private final Translation2d m_backLeftLocation = new Translation2d(0.2778125, -0.2778125);
    private final Translation2d m_backRightLocation = new Translation2d(0.2778125, 0.2778125);

    // Swerve Modules
    private final SwerveModule m_frontLeft = new SwerveModule(IDConstants.FL_DRIVE, IDConstants.FL_ANGLE, IDConstants.FL_ABS, true, -0.103759765625);
    private final SwerveModule m_frontRight = new SwerveModule(IDConstants.FR_DRIVE, IDConstants.FR_ANGLE, IDConstants.FR_ABS, true, 0.201904296875);
    private final SwerveModule m_backLeft = new SwerveModule(IDConstants.BL_DRIVE, IDConstants.BL_ANGLE, IDConstants.BL_ABS, true, 0.43701171875);
    private final SwerveModule m_backRight = new SwerveModule(IDConstants.BR_DRIVE, IDConstants.BR_ANGLE, IDConstants.BR_ABS, true, -0.3232421875);

    // Sensors
    private final AHRS m_gyro = new AHRS(Port.kMXP);

    // Swerve Drive Components
    private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
        m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation
    );

    private final SwerveDrivePoseEstimator m_odometry = new SwerveDrivePoseEstimator(
        m_kinematics,
        m_gyro.getRotation2d().plus(Rotation2d.fromDegrees(180)),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_backLeft.getPosition(),
            m_backRight.getPosition()
        },
        new Pose2d()
    );

    // PID Controllers for Auto Mode
    private final PIDController xController = new PIDController(PIDConstants.P_TRANS, PIDConstants.I_TRANS, PIDConstants.D_TRANS);
    private final PIDController yController = new PIDController(PIDConstants.P_TRANS, PIDConstants.I_TRANS, PIDConstants.D_TRANS);
    private final PIDController rotController = new PIDController(PIDConstants.P_ROT, PIDConstants.I_ROT, PIDConstants.D_ROT);

    private Alliance alliance;
    private Command autoCommand;

    private Field2d field = new Field2d();

    /**
     * Constructs the DriveSubsystem and sets the default drive command.
     *
     * @param x Supplier for the X axis speed
     * @param y Supplier for the Y axis speed
     * @param z Supplier for the rotation speed
     */
    public DriveSubsystem(Supplier<Double> x, Supplier<Double> y, Supplier<Double> z) {
        initializeSubsystem(x, y, z);
    }

    @Override
    public void periodic() {

        m_odometry.update(m_gyro.getRotation2d(),
            new SwerveModulePosition[] {
                m_frontLeft.getPosition(),
                m_frontRight.getPosition(),
                m_backLeft.getPosition(),
                m_backRight.getPosition()
            }
        );

        LimelightHelpers.SetRobotOrientation("limelight", m_odometry.getEstimatedPosition().getRotation().getDegrees(), 0, 0, 0, 0, 0);

        LimelightHelpers.PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight");

        if (mt2 != null && Math.abs(m_gyro.getRate()) < 720 && mt2.tagCount != 0) {
            m_odometry.addVisionMeasurement(
                new Pose2d(
                    mt2.pose.getX(),
                    mt2.pose.getY(),
                    mt2.pose.getRotation()
                ),
                mt2.timestampSeconds,
                VecBuilder.fill(0.7, 0.7, 9999999));
        }

        field.setRobotPose(m_odometry.getEstimatedPosition());

    }

    /**
     * Initializes the subsystem, including resetting the gyro and setting the default drive command.
     */
    private void initializeSubsystem(Supplier<Double> x, Supplier<Double> y, Supplier<Double> z) {
        // Creating a new thread so the gyro won't block the robot resetting.
        new Thread(()->{
            try{
                // Team 6814's on programming swerve drive suggested sleeping.
                Thread.sleep(1000);
                m_gyro.reset();
            }
            catch(Exception e){
                System.out.println("Expection Resetting Gyro");
            }
        }).start();
        
        this.setDefaultCommand(driveCommand(x, y, z));
    }

    /**
     * Generates an autonomous command based on a path.
     *
     * @param pathName The name of the path to follow.
     */
    public void generateAuto(String pathName) {
        var trajectory = Choreo.getTrajectory(pathName);

        autoCommand = Choreo.choreoSwerveCommand(
                trajectory,
                m_odometry::getEstimatedPosition,
                xController,
                yController,
                rotController,
                this::drive,
                this::isRedAlliance,
                this
        );
    }

    /**
     * Schedules the autonomous drive command.
     */
    public void executeAutoDrivePath() {
        autoCommand.schedule();
    }

    /**
     * Determines if the robot is on the Red Alliance.
     */
    private boolean isRedAlliance() {
        DriverStation.getAlliance().ifPresent((Alliance alliance) -> {
            this.alliance = alliance;
        });
        return (alliance == Alliance.Red);
    }

    /**
     * Drives the robot based on joystick input.
     *
     * @param xSpeed        Speed in the X direction (forward).
     * @param ySpeed        Speed in the Y direction (sideways).
     * @param rot           Angular velocity.
     * @param fieldRelative Whether the input is field-relative.
     * @param periodSeconds Time period for discretization.
     */
    public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative, double periodSeconds) {

        var swerveModuleStates = m_kinematics.toSwerveModuleStates(
                ChassisSpeeds.discretize(
                        fieldRelative
                                ? ChassisSpeeds.fromFieldRelativeSpeeds(ySpeed, xSpeed, rot, m_gyro.getRotation2d())
                                : new ChassisSpeeds(ySpeed, xSpeed, rot),
                        periodSeconds));

        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);

        m_frontLeft.setDesiredState(swerveModuleStates[0]);
        m_frontRight.setDesiredState(swerveModuleStates[1]);
        m_backLeft.setDesiredState(swerveModuleStates[2]);
        m_backRight.setDesiredState(swerveModuleStates[3]);
    }

    /**
     * Drives the robot using ChassisSpeeds directly.
     *
     * @param speeds Chassis speeds object.
     */
    public void drive(ChassisSpeeds speeds) {
        drive(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond, false, 1 / 50.0);
    }

    /**
     * Updates the robot's odometry based on module positions and gyro readings.
     */
    public void updateOdometry() {
        m_odometry.update(
                m_gyro.getRotation2d().plus(Rotation2d.fromDegrees(180)),
                new SwerveModulePosition[] {
                        m_frontLeft.getPosition(),
                        m_frontRight.getPosition(),
                        m_backLeft.getPosition(),
                        m_backRight.getPosition()
                });
    }

    /**
     * Command to drive the robot based on joystick inputs.
     *
     * @param x Supplier for the X axis speed
     * @param y Supplier for the Y axis speed
     * @param z Supplier for the rotation speed
     * @return The drive command.
     */
    public Command driveCommand(Supplier<Double> x, Supplier<Double> y, Supplier<Double> z) {
        Command out = new Command() {
            @Override
            public void execute() {
                drive(-x.get() * kMaxSpeed, -y.get() * kMaxSpeed, z.get() * kMaxAngularSpeed, true, 1 / 50.0);
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        };

        out.addRequirements(this);
        return out;
    }

    /**
     * Initializes SmartDashboard values for Swerve Module angles.
     */
    @Override
    public void initSendable(SendableBuilder builder) {
		
        super.initSendable(builder);

        SmartDashboard.putData("Odometry", field);

		builder.setSmartDashboardType("SwerveDrive");

		builder.addDoubleProperty("Front Left Angle", () -> m_frontLeft.getState().angle.getRadians(), null);
		builder.addDoubleProperty("Front Left Velocity", () -> m_frontLeft.getState().speedMetersPerSecond, null);

		builder.addDoubleProperty("Front Right Angle", () -> m_frontRight.getState().angle.getRadians(), null);
		builder.addDoubleProperty("Front Right Velocity", () -> m_frontRight.getState().speedMetersPerSecond, null);

		builder.addDoubleProperty("Back Left Angle", () -> m_backLeft.getState().angle.getRadians(), null);
		builder.addDoubleProperty("Back Left Velocity", () -> m_backLeft.getState().speedMetersPerSecond, null);

		builder.addDoubleProperty("Back Right Angle", () -> m_backRight.getState().angle.getRadians(), null);
		builder.addDoubleProperty("Back Right Velocity", () -> m_backRight.getState().speedMetersPerSecond, null);

		builder.addDoubleProperty("Robot Angle", () -> m_gyro.getRotation2d().getRadians(), null);

        builder.addDoubleProperty("Front Left ABS", () -> m_frontLeft.getAbsoluteState().angle.getDegrees(), null);
        builder.addDoubleProperty("Front Right ABS", () -> m_frontRight.getAbsoluteState().angle.getDegrees(), null);
        builder.addDoubleProperty("Back Left ABS", () -> m_backLeft.getAbsoluteState().angle.getDegrees(), null);
        builder.addDoubleProperty("Back Right ABS", () -> m_backRight.getAbsoluteState().angle.getDegrees(), null);

        builder.addDoubleProperty("Front Left Deg", () -> m_frontLeft.getState().angle.getDegrees(), null);
        builder.addDoubleProperty("Front Right Deg", () -> m_frontRight.getState().angle.getDegrees(), null);
        builder.addDoubleProperty("Back Left Deg", () -> m_backLeft.getState().angle.getDegrees(), null);
        builder.addDoubleProperty("Back Right Deg", () -> m_backRight.getState().angle.getDegrees(), null);

    }
}
