package frc.robot.Drivetrain;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;

public class SwerveOdometry {
    
    private static SwerveDrivePoseEstimator odometry;

    public void initialize(Pose2d initialPose) {
        odometry = new SwerveDrivePoseEstimator(SwerveKinematics.kinematics, SwerveKinematics.robotRotation, SwerveKinematics.modulePositions, initialPose);
    }

    public static Pose2d getPose() {
        return odometry.getEstimatedPosition();
    }

    public static void setPose(Pose2d position) {
        odometry.resetPosition(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions, position);
    }

    public static void addSensorData(Pose2d position) {
        odometry.addVisionMeasurement(position, Timer.getFPGATimestamp());
    }

}