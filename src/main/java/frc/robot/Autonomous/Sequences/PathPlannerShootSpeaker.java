package frc.robot.Autonomous.Sequences;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.CommandsV2.Command;
import frc.robot.CommandsV2.ParallelCompile;
import frc.robot.CommandsV2.SequentialCompile;
import frc.robot.Subsystems.Subsystems;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Drivetrain.DrivetrainCommands;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Utils.FieldConstants;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/*
Pathplanner shooting is regular scoring, the difference being it skips the "can i shoot" checks, as thats not important. We just trust the auto that the shooting is possible.
 */
public class PathPlannerShootSpeaker {
    private static SequentialCompile innerCommand;
    public static final Command command = new Command() {
        @Override
        public void init() {

            Pose2d pose = SwerveOdometry.getPose();

            if(DriverStation.getAlliance().get() == DriverStation.Alliance.Blue){
                //If we are past the line of no shooting
                //Make a line from the robot towards the speaker
                Point2D speaker = new Point2D.Double(FieldConstants.BLUE_SPEAKER[0], FieldConstants.BLUE_SPEAKER[1]);
                Point2D bot = new Point2D.Double(pose.getX(),pose.getY());
                Line2D myLine = new Line2D.Double(speaker,bot);
                double shootingAngle = Math.atan(2/speaker.distance(bot));
                double rotDegrees = Math.atan((myLine.getX2()-myLine.getX1())/(myLine.getY2()-myLine.getY1())) * 180/Math.PI;
                Angular.targetAngle = shootingAngle;
                Subsystems.targetRotation = Rotation2d.fromDegrees(rotDegrees);
            } else {
                //Red team, same deal as before.
                Point2D speaker = new Point2D.Double(FieldConstants.RED_SPEAKER[0], FieldConstants.RED_SPEAKER[1]);
                Point2D bot = new Point2D.Double(pose.getX(),pose.getY());
                Line2D myLine = new Line2D.Double(speaker,bot);
                double shootingAngle = Math.atan(2/speaker.distance(bot));
                double rotDegrees = Math.atan((myLine.getX2()-myLine.getX1())/(myLine.getY2()-myLine.getY1())) * 180/Math.PI + 180;
                Angular.targetAngle = shootingAngle;
                Subsystems.targetRotation = Rotation2d.fromDegrees(rotDegrees);
            }

            innerCommand = new SequentialCompile(
                new ParallelCompile(
                    ArmCommands.speakerPosition,
                    DrivetrainCommands.lineUpSpeaker
                ),
                IntakeCommands.feed,
                ArmCommands.stow
            );
            innerCommand.init();

        }

        @Override
        public void periodic() {
            innerCommand.periodic();
        }

        @Override
        public boolean isDone() {
            return innerCommand.isFinished();
        }
    };
}