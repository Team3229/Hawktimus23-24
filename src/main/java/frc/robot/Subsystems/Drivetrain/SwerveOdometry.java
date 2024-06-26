package frc.robot.Subsystems.Drivetrain;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Subsystems.Vision.Vision;

public class SwerveOdometry {
    
    private static SwerveDrivePoseEstimator odometry;

    public static void init(Pose2d initialPose) {
        odometry = new SwerveDrivePoseEstimator(
            SwerveKinematics.kinematics,
            SwerveKinematics.robotRotation,
            SwerveKinematics.modulePositions,
            initialPose
        );


        
    }

    public static Pose2d getPose() {
        return odometry.getEstimatedPosition();
    }

    public static void setPose(Pose2d position) {
        odometry.resetPosition(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions, position);
    }

    public static void resetPose(Pose2d position) {
        odometry.resetPosition(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions, position);
    }

    private static void addSensorData(Pose2d position, double pipelineLatency, boolean isValidData) {
        if (isValidData) {
            odometry.addVisionMeasurement(position, Timer.getFPGATimestamp() - pipelineLatency);
        }
    }

    public static void update(Rotation2d rotation, SwerveModulePosition[] swervePositions) {
        addSensorData(Vision.getPose(), 0, Vision.isValid());
        odometry.update(rotation, swervePositions);
    }

}