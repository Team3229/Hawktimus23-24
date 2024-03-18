package frc.robot.Subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.CommandsV2.Command;
import frc.robot.Subsystems.Subsystems;
import frc.robot.Subsystems.Vision.Vision;
import frc.robot.Utils.FieldConstants;

public class DrivetrainCommands {

    public static Command lineUpAmp = new Command() {

        @Override
        public void periodic() {
            if(DriverStation.getAlliance().get() == DriverStation.Alliance.Blue){
                SwerveKinematics.goToPosition(new Pose2d(FieldConstants.BLUE_AMP[0], FieldConstants.BLUE_AMP[1]-(SwerveKinematics.robotWidth/2)-.25, Rotation2d.fromDegrees(90)));
            } else {
                SwerveKinematics.goToPosition(new Pose2d(FieldConstants.RED_AMP[0], FieldConstants.RED_AMP[1]-(SwerveKinematics.robotWidth/2)-.25, Rotation2d.fromDegrees(90)));
            }
        }

        @Override
        public boolean isDone() {
            return SwerveKinematics.linearXMovement.atSetpoint() & SwerveKinematics.linearYMovement.atSetpoint() & SwerveKinematics.angularMovement.atSetpoint();
        }
    };

    public static Command lineUpSpeaker = new Command() {
        @Override
        public void periodic() {
            SwerveKinematics.goToPosition(new Pose2d(Vision.getPose().getX(), Vision.getPose().getY(), Subsystems.targetRotation));
        }

        @Override
        public boolean isDone() {
            return SwerveKinematics.linearXMovement.atSetpoint() & SwerveKinematics.linearYMovement.atSetpoint() & SwerveKinematics.angularMovement.atSetpoint();
        }
    };
}