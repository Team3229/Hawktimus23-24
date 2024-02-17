package frc.robot.Subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Autonomous.Sequences.ScoreAmp;
import frc.robot.Autonomous.Sequences.ScoreSpeaker;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.Linear;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Utils.FieldConstants;
import frc.robot.Utils.RunCommand;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Subsystems {

    private static final double AMP_RADIUS = 1.25;
    public static Rotation2d rotation = null;
    
    public static void init(){
        Intake.init();
		Shooter.init();
		Angular.init();
		Linear.init();
    }

    public static void update(){
        Linear.update();
		Intake.update();
		Shooter.update();
        Angular.update();
        if(Intake.hasNote){
            if(Shooter.ampIntent){
                ampAttempt();
            } else {
                shootAttempt();
            }
        } else {
            Shooter.targetSpeed = 0;
            Angular.targetAngle = 0;
            rotation = new Rotation2d();
        }
    }

    private static void shootAttempt(){
        /*
         * We can shoot IF
         * -We are not within the other alliances zone
         * -Ideal Rotation does not intersect with LoS blocker
         * -Dist. + Angle is possible (0-60 deg possible angle)
         */
        Pose2d pose = SwerveOdometry.getPose();
        
        if(DriverStation.getAlliance().get() == DriverStation.Alliance.Blue){
            if((pose.getX() < FieldConstants.RED_SHOOTING_LINE[0])){
                Point2D speaker = new Point2D.Double(FieldConstants.BLUE_SPEAKER[0], FieldConstants.BLUE_SPEAKER[1]);
                Point2D bot = new Point2D.Double(pose.getX(),pose.getY());
                Line2D myLine = new Line2D.Double(speaker,bot);
                double degrees = Math.atan((myLine.getX2()-myLine.getX1())/(myLine.getY2()-myLine.getY1()));
                Point2D[] triangle = {new Point2D.Double(FieldConstants.BLUE_STAGE[0][0],FieldConstants.BLUE_STAGE[0][1]),new Point2D.Double(FieldConstants.BLUE_STAGE[1][0],FieldConstants.BLUE_STAGE[1][1]),new Point2D.Double(FieldConstants.BLUE_STAGE[2][0],FieldConstants.BLUE_STAGE[2][1])};
                boolean intersect = false;
                for (int i = 0; i < triangle.length - 1; i++) {
                    intersect = myLine.intersectsLine(triangle[i].getX(), triangle[i].getY(), triangle[i+1].getX(), triangle[i+1].getY());
                    if (intersect) {
                        break;
                    }
                }
                if(!intersect){
                    //We have LoS on the speaker (Through the stage) therefore we are good to shoot, as long as its possible!
                    double shootingAngle = Math.atan(2/speaker.distance(bot));
                    if(60 <= shootingAngle & shootingAngle <= 90){
                        //We can shoot!
                        Rotation2d rot = Rotation2d.fromDegrees(degrees);
                        Shooter.targetSpeed = speaker.distance(bot) * 1200;
                        Angular.targetAngle = shootingAngle;
                        rotation = rot;
                        RunCommand.run(ScoreSpeaker.command);
                        return;
                    }
                }
            }
        } else {
            //Red team
            if((pose.getX() > FieldConstants.BLUE_SHOOTING_LINE[0])){
                Point2D speaker = new Point2D.Double(FieldConstants.RED_SPEAKER[0], FieldConstants.RED_SPEAKER[1]);
                Point2D bot = new Point2D.Double(pose.getX(),pose.getY());
                Line2D myLine = new Line2D.Double(speaker,bot);
                double degrees = Math.atan((myLine.getX2()-myLine.getX1())/(myLine.getY2()-myLine.getY1()));
                Point2D[] triangle = {new Point2D.Double(FieldConstants.RED_STAGE[0][0],FieldConstants.RED_STAGE[0][1]),new Point2D.Double(FieldConstants.RED_STAGE[1][0],FieldConstants.RED_STAGE[1][1]),new Point2D.Double(FieldConstants.RED_STAGE[2][0],FieldConstants.RED_STAGE[2][1])};
                boolean intersect = false;
                for (int i = 0; i < triangle.length - 1; i++) {
                    intersect = myLine.intersectsLine(triangle[i].getX(), triangle[i].getY(), triangle[i+1].getX(), triangle[i+1].getY());
                    if (intersect) {
                        break;
                    }
                }
                if(!intersect){
                    //We have LoS on the speaker (Through the stage) therefore we are good to shoot, as long as its possible!
                    double shootingAngle = Math.atan(2/speaker.distance(bot));
                    if(60 <= shootingAngle & shootingAngle <= 90){
                        //We can shoot!
                        Rotation2d rot = Rotation2d.fromDegrees(degrees);
                        Shooter.targetSpeed = speaker.distance(bot) * 1200;
                        Angular.targetAngle = shootingAngle;
                        rotation = rot;
                        RunCommand.run(ScoreSpeaker.command);
                        return;
                    }
                }
            }
        }

        Shooter.targetSpeed = 0;
        Angular.targetAngle = 0;
        rotation = new Rotation2d();
        
    }

    private static void ampAttempt(){
        //Shoot in amp if possible
        //If we are in the radius to amp score
        Pose2d pose = SwerveOdometry.getPose();
        if(DriverStation.getAlliance().get() == DriverStation.Alliance.Blue){
            //Blue team
            if(Math.pow((pose.getX() - FieldConstants.BLUE_AMP[0]),2) + Math.pow((pose.getY() - FieldConstants.BLUE_AMP[1]),2) < Math.pow(AMP_RADIUS, 2)){
                //Within range to score amp
                RunCommand.run(ScoreAmp.command);
            }
        } else {
            //Red team
            if(Math.pow((pose.getX() - FieldConstants.RED_AMP[0]),2) + Math.pow((pose.getY() - FieldConstants.RED_AMP[1]),2) < Math.pow(AMP_RADIUS, 2)){
                //Within range to score amp
                RunCommand.run(ScoreAmp.command);
            }
        }
        
    }

}