package frc.robot.Subsystems.Arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

/*
Controls the arm
-Move to target angle
 */
public class Angular {

    private static CANSparkMax arm;
    private static CANSparkMax arm2;
    private static final int ARM_ID = 0;
    private static final int ARM2_ID = 0;

    private static double targetAngle = 0;

    public static boolean atTarget = false;
    private static double ARM_DEADBAND = 5;

    private static double STOWED_ANGLE = 0;
    private static double UNSTOWED_ANGLE = 1;
    private static double RAISING_ANGLE = 2;
    private static double AMP_ANGLE = 3;

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

    public static void shoot(double degrees){
        targetAngle = degrees;
    }

    private static void goToAngle(){
        //If not at angle (Within deadband)
        //If at angle set boolean to true
    }

    public static void init(){
        arm = new CANSparkMax(ARM_ID, MotorType.kBrushless);
        arm2 = new CANSparkMax(ARM2_ID,MotorType.kBrushless);
        arm2.follow(arm);
    }
    
}
