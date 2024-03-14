//BIG RAT
package frc.robot.Subsystems.Arm;

import com.revrobotics.AbsoluteEncoder;
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
    private static double GRAB_ANGLE = 108;
    private static double AMP_ANGLE = 0;
    private static double SUBWOOF_SHOOT = 75;
    private static double RAISED = 78;
    public static boolean manual = false;

    public static AbsoluteEncoder encoder;
;
    private static SparkPIDController pidController;

    public static void update(){
        if(!manual){
            if(Linear.goingBackwards & ((targetAngle > STOWED_ANGLE) | encoder.getPosition() > STOWED_ANGLE+1)){
                targetAngle = STOWED_ANGLE;
            } else if(!Linear.goingBackwards & ((targetAngle > GRAB_ANGLE) | encoder.getPosition() > GRAB_ANGLE+1)){
                targetAngle = GRAB_ANGLE;
            }
        } else {
            if(targetAngle > 0.3) {
                targetAngle = 0.3;
            }
            if(Linear.goingBackwards & ((targetAngle > STOWED_ANGLE) | encoder.getPosition() > STOWED_ANGLE+1)){
                targetAngle = 0;
            } else if(!Linear.goingBackwards & ((targetAngle > GRAB_ANGLE) | encoder.getPosition() > GRAB_ANGLE+1)){
                targetAngle = 0;
            }
        }
        goToAngle();
    }

    public static void runManual(double speed){
        if (speed == 0) {
            if (manual) {
                targetAngle = encoder.getPosition();
            }
            manual = false;
        } else {
            manual = true;
            targetAngle = speed;
        }
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

    public static void raise(){
        manual = false;
        targetAngle = RAISED;
    }

    private static void goToAngle(){
        if(manual){
            pidController.setReference(targetAngle,ControlType.kDutyCycle);
        } else {
            pidController.setReference(targetAngle,ControlType.kPosition);
            atTarget = Math.abs(encoder.getPosition()-targetAngle) <= ARM_DEADBAND;
        }
    }

    public static void init(){
        left = new CANSparkMax(LEFT_ID, MotorType.kBrushless);
        right = new CANSparkMax(RIGHT_ID, MotorType.kBrushless);

        encoder = left.getAbsoluteEncoder();
        encoder.setPositionConversionFactor(360);
        encoder.setZeroOffset(0);

        right.follow(left, true);

        pidController = left.getPIDController();

        pidController.setFeedbackDevice(encoder);
        pidController.setP(0.02);

        pidController.setPositionPIDWrappingEnabled(false);
        
        pidController.setOutputRange(-0.3, 0.3);

        
    }
    
}
