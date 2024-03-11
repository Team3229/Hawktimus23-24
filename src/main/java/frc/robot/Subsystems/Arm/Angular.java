//BIG RAT
package frc.robot.Subsystems.Arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

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

    public static boolean atTarget = false;
    private static double ARM_DEADBAND = 5;

    private static double STOWED_ANGLE = 92;
    private static double GRAB_ANGLE = 110;
    private static double AMP_ANGLE = 7.8;
    private static double SUBWOOF_SHOOT = 75;
    private static boolean manual = false;

    private static RelativeEncoder encoder;
;
    private static SparkPIDController pidController;

    public static void update(){
        goToAngle();
    }

    public static void runManual(double speed){
        manual = true;
        targetAngle = speed;
    }

    public static void stow(){
        manual = false;
        targetAngle = STOWED_ANGLE;
    }
    

    public static void grab(){
        manual = false;
        targetAngle = GRAB_ANGLE;
    }

    public static void amp(){
        manual = false;
        targetAngle = AMP_ANGLE;
    }

    public static void subwoofShoot(){
        manual = false;
        targetAngle = SUBWOOF_SHOOT;
    }

    private static void goToAngle(){
        if(manual){
            pidController.setReference(targetAngle,ControlType.kDutyCycle);
        } else {
            pidController.setReference(targetAngle,ControlType.kPosition);
            atTarget = Math.abs(encoder.getPosition()) <= ARM_DEADBAND;
        }
    }

    public static void init(){
        left = new CANSparkMax(LEFT_ID, MotorType.kBrushless);
        right = new CANSparkMax(RIGHT_ID, MotorType.kBrushless);

        encoder = left.getAlternateEncoder(8192);
        encoder.setPositionConversionFactor(360);

        encoder.setPosition(0);

        right.follow(left, true);

        pidController = left.getPIDController();

        pidController.setFeedbackDevice(encoder);
        pidController.setP(0.02);

        pidController.setOutputRange(-0.5, 0.5);
    }
    
}
