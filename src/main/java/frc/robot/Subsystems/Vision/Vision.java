package frc.robot.Subsystems.Vision;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Utils.FieldConstants;
import frc.robot.Utils.Utils;
import frc.robot.Utils.Utils.RobotStates;

public class Vision {

    private static PhotonCamera camera;
    private static AprilTagFieldLayout aprilTagFieldLayout;
    private static PhotonPipelineResult visionData;

    private static Transform3d robotToCam;

    private static PhotonPoseEstimator photonPoseEstimator;
    private static Pose2d output = new Pose2d();
    private static Pose3d output2 = new Pose3d();

    public static void init() {
        camera = new PhotonCamera("Shooter_Cam");

        aprilTagFieldLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();

        robotToCam = new Transform3d(new Translation3d(-0.2032,0.3229,0.384175), new Rotation3d(0,0,Math.PI));

        photonPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, camera, robotToCam);
    
        visionData = new PhotonPipelineResult();
    }

    public static Pose2d getPose() {
        visionData = camera.getLatestResult();
        photonPoseEstimator.setLastPose(output);
        photonPoseEstimator.update().ifPresent(
            (EstimatedRobotPose pose) -> {
                output = pose.estimatedPose.toPose2d();
            });
        return output;
    }

    private static Pose3d robotPose() {
        photonPoseEstimator.update().ifPresent(
            (EstimatedRobotPose pose) -> {
                output2 = pose.estimatedPose;
            });
        return output2;
    }

    public static double getDistance(Pose3d object) {
        return new Transform3d(robotPose(), object).getTranslation().getDistance(new Translation3d());
    }

    public static double getLatency() {
        return visionData.getLatencyMillis();
    }

    public static boolean isValid() {

        if (Utils.getRobotState() == RobotStates.Autonomous) {
            return false;
        }
        // if(Utils.getAlliance() == Alliance.Blue){
        //     if(SwerveOdometry.getPose().getX() < FieldConstants.BLUE_SHOOTING_LINE[0]){
        //         return false;
        //     }
        // } else if(SwerveOdometry.getPose().getX() > FieldConstants.RED_SHOOTING_LINE[0]){
        //     return false;
        // }

        return visionData.hasTargets();
    }

}