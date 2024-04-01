package frc.robot.Subsystems.Shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Utils.FieldConstants;
import frc.robot.Utils.Utils;
import frc.robot.Utils.Utils.RobotStates;

/*
-Spin up (to given RPM)
-Request intake to send note forward
 */
public class Shooter {

    private static CANSparkMax shooter;
    private static final int SHOOTER_ID = 9;
    public static final double AMP_SPEED = 0.5;
    public static ShooterStates state = ShooterStates.idle;
    public static double targetSpeed = 0;
    public static boolean ampIntent = false;
    public static final double RPM_DEADBAND = 30;

    public static SparkPIDController pid;
    public static RelativeEncoder encoder;
    public static enum ShooterStates {
        idle,
        spinningUp
    }

    public static void init(){
        shooter = new CANSparkMax(SHOOTER_ID, MotorType.kBrushless);
        shooter.setInverted(true);
        pid = shooter.getPIDController();
        pid.setP(0.00015);
        pid.setI(0.0000003);
        pid.setD(0.0015);
        pid.setFF(0.000015);

        pid.setOutputRange(0, 1);
        encoder = shooter.getEncoder();
    }

    public static void update(){
        switch(state){
            case idle:
                stop();
                break;
            case spinningUp:
                spinningUp();
                break;
        }
    }

    private static void spinningUp(){
        
        if (Utils.getRobotState() == RobotStates.Autonomous) {
            double distance = 0;
            if (Utils.getAlliance() == Alliance.Blue) {
                distance = SwerveOdometry.getPose().getTranslation().getDistance(FieldConstants.BLUE_SPEAKER_P);
            } else {
                distance = SwerveOdometry.getPose().getTranslation().getDistance(FieldConstants.RED_SPEAKER_P);
            }
            distance *= 39.3701;

            distance -= 34/2;

            targetSpeed = (1.6966e-10*Math.pow(distance, 5.59732)) + 4492.38;
            targetSpeed = (targetSpeed > 5200) ? 5200 : targetSpeed;
            pid.setReference(targetSpeed, ControlType.kVelocity);
        } else {
            if (Intake.hasNote & targetSpeed != AMP_SPEED & targetSpeed != -AMP_SPEED) {
                double distance = 0;
                if (Utils.getAlliance() == Alliance.Blue) {
                    distance = SwerveOdometry.getPose().getTranslation().getDistance(FieldConstants.BLUE_SPEAKER_P);
                } else {
                    distance = SwerveOdometry.getPose().getTranslation().getDistance(FieldConstants.RED_SPEAKER_P);
                }
                distance *= 39.3701;

                distance -= 34/2;

                targetSpeed = (1.6966e-10*Math.pow(distance, 5.59732)) + 4492.38;
                targetSpeed = (targetSpeed > 5200) ? 5200 : targetSpeed;
                pid.setReference(targetSpeed, ControlType.kVelocity);

            } else if (targetSpeed == AMP_SPEED) {
                pid.setReference(targetSpeed, ControlType.kDutyCycle);
            } else if (targetSpeed == -AMP_SPEED) {
                pid.setReference(targetSpeed, ControlType.kDutyCycle);
            } else {
                stop();
            }
        }
    }

    public static void spinUp(double speed){
        targetSpeed = speed;
        state = ShooterStates.spinningUp;
    }
    
    public static void spinUp(){
        state = ShooterStates.spinningUp;
    }

    public static void feed(){
        targetSpeed = AMP_SPEED;
        state = ShooterStates.spinningUp;
    }

    public static void stop(){
        targetSpeed = 0;
        state = ShooterStates.idle;
        pid.setReference(0, ControlType.kDutyCycle);
    }

    public static boolean atTarget() {
        return Math.abs(encoder.getVelocity() - targetSpeed) < RPM_DEADBAND;
    }
}
