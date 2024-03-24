//BIG RAT
package frc.robot.Subsystems.Arm;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Utils.FieldConstants;
import frc.robot.Utils.Utils;

import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;


/*
Controls the arm
-Move to target angle
 */
public class Angular {

    public static CANSparkMax left;
    private static CANSparkMax right;
    private static final int LEFT_ID = 16;
    private static final int RIGHT_ID = 17;

    public static double targetAngle = 0;

    private static double ARM_DEADBAND = 10;

    private static double STOWED_ANGLE = 90;
    private static double GRAB_ANGLE = 110;
    public static double AMP_ANGLE = 0;
    private static double SUBWOOF_SHOOT = 75;
    public static double RAISED = 83;
    public static boolean manual = false;

    public static AbsoluteEncoder encoder;
;
    private static SparkPIDController pidController;

    public static boolean isShooting = false;

    public static void update(){
        goToAngle();
    }

    public static void runManual(double speed){

        if (speed < 0 & (encoder.getPosition() < 0 | encoder.getPosition() > 180)) {
            speed = 0;
        }

        if (speed > 0 & (encoder.getPosition() >= 109.25 & encoder.getPosition() < 180) & !Linear.goingBackwards) {
            speed = 0;
        }

        if (speed > 0 & (encoder.getPosition() >= STOWED_ANGLE & encoder.getPosition() < 180) & Linear.goingBackwards) {
            speed = 0;
        }

        if (speed == 0) {
            if (manual) {
                targetAngle = encoder.getPosition();
            }
            manual = false;
        } else {
            manual = true;
            isShooting = false;
            targetAngle = speed;
        }
    }

    public static void stow(){
        isShooting = false;
        manual = false;
        targetAngle = STOWED_ANGLE;
    }

    public static void grab(){
        isShooting = false;
        manual = false;
        targetAngle = GRAB_ANGLE;
    }

    public static void amp(){
        isShooting = false;
        manual = false;
        targetAngle = AMP_ANGLE;
    }

    public static void subwoofShoot(){
        isShooting = false;
        manual = false;
        targetAngle = SUBWOOF_SHOOT;
    }

    public static void raise(){
        isShooting = false;
        manual = false;
        targetAngle = RAISED;
    }

    public static void shoot() {
        double distance = 0;
        if (Utils.getAlliance() == Alliance.Blue) {
            distance = SwerveOdometry.getPose().getTranslation().getDistance(FieldConstants.BLUE_SPEAKER_P);
        } else {
            distance = SwerveOdometry.getPose().getTranslation().getDistance(FieldConstants.RED_SPEAKER_P);
        }
        distance *= 39.3701;
        distance -= 34/2;
        targetAngle = (411.464*Math.pow(distance, -0.632142)) + 37.4285;
    }

    private static void goToAngle(){

        if(isShooting & !manual) {
            if (!Intake.hasNote) isShooting = false;
            shoot();
        }

        if(manual){
            if (Math.abs(targetAngle) <= 1) {
                pidController.setReference(targetAngle,ControlType.kDutyCycle);
            }
        } else {
            pidController.setReference(targetAngle,ControlType.kPosition);
        }
    }

    public static boolean checkTarget() {
        return Math.abs(encoder.getPosition()-targetAngle) <= ARM_DEADBAND;
    }

    public static void init(){
        left = new CANSparkMax(LEFT_ID, MotorType.kBrushless);
        right = new CANSparkMax(RIGHT_ID, MotorType.kBrushless);

        encoder = left.getAbsoluteEncoder();
        encoder.setPositionConversionFactor(360);
        encoder.setZeroOffset(58.64289093017578);
        left.setInverted(false);
        right.follow(left, true);

        pidController = left.getPIDController();

        pidController.setFeedbackDevice(encoder);
        pidController.setP(0.03);

        pidController.setPositionPIDWrappingEnabled(true);
        pidController.setPositionPIDWrappingMinInput(0);
        pidController.setPositionPIDWrappingMinInput(360);
        
        pidController.setOutputRange(-0.4, 0.4);

        targetAngle = encoder.getPosition();
        
    }
    
}
