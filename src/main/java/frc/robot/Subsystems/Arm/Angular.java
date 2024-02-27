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

    public static CANSparkMax arm;
    private static CANSparkMax arm2;
    private static final int ARM_ID = 16;
    private static final int ARM2_ID = 17;

    public static double targetAngle = 0;

    public static boolean atTarget = false;
    private static double ARM_DEADBAND = 5;

    private static double STOWED_ANGLE = 90;
    private static double UNSTOWED_ANGLE = 108;
    private static double RAISING_ANGLE = 85;
    private static double AMP_ANGLE = 0;

    private static RelativeEncoder encoder;

    private static final int ARM_P = 0;
    private static final int ARM_I = 0;
    private static final int ARM_D = 0;
    private static SparkPIDController pidController;

    public static void update(){
        goToAngle();
    }

    public static void stow(){
        targetAngle = STOWED_ANGLE;
    }
    
    public static void raise(){
        targetAngle = RAISING_ANGLE;
    }

    public static void unstow(){
        targetAngle = UNSTOWED_ANGLE;
    }

    public static void amp(){
        targetAngle = AMP_ANGLE;
    }

    private static void goToAngle(){
        pidController.setReference(targetAngle,ControlType.kPosition);
        atTarget = Math.abs(encoder.getPosition()) <= ARM_DEADBAND;
    }

    public static void init(){
        arm = new CANSparkMax(ARM_ID, MotorType.kBrushless);
        arm2 = new CANSparkMax(ARM2_ID,MotorType.kBrushless);
        arm2.follow(arm,  true);
        pidController = arm.getPIDController();
        pidController.setP(ARM_P);
        pidController.setI(ARM_I);
        pidController.setD(ARM_D);
        encoder = arm.getAlternateEncoder(8192);
        encoder.setPositionConversionFactor(60 * 360);
        pidController.setOutputRange(AMP_ANGLE, UNSTOWED_ANGLE);
    }
    
}
