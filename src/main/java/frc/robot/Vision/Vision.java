package frc.robot.Vision;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

public class Vision {

    private static PhotonCamera camera;
    private static AprilTagFieldLayout aprilTagFieldLayout;
    private static PhotonPipelineResult visionData;

    private static Transform3d robotToCam;

    private static PhotonPoseEstimator photonPoseEstimator;
    private static Pose2d output = new Pose2d();

    public static void initialize() {
        camera = new PhotonCamera("photonvision");

        aprilTagFieldLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();

        robotToCam = new Transform3d(new Translation3d(0,0,0), new Rotation3d(0,0,0));

        photonPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, camera, robotToCam);
    }

    public static void periodic() {
        visionData = camera.getLatestResult();
    }

    public static Pose2d getPose() {
        photonPoseEstimator.update().ifPresent(
            (EstimatedRobotPose pose) -> {
                output = pose.estimatedPose.toPose2d();
            });
        return output;
    }

    public static double getLatency() {
        return visionData.getLatencyMillis() / 1000;
    }

    public static boolean isValid() {
        return visionData.hasTargets();
    }

}