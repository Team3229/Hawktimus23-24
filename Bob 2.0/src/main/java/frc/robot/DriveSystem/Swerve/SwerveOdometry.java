package frc.robot.DriveSystem.Swerve;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;

public class SwerveOdometry {
    
    private static SwerveDrivePoseEstimator odometry;

    public SwerveOdometry(Pose2d intitialPose) {
        odometry = new SwerveDrivePoseEstimator(SwerveKinematics.kinematics, SwerveKinematics.robotRotation, SwerveKinematics.modulePositions, intitialPose);
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