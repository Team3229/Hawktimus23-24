package frc.robot.Subsystems.Arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

/*
Controls the arm
-Move to target angle
 */
public class Angular {

    private static CANSparkMax arm;
    private static final int ARM_ID = 0;

    public static void init(){
        arm = new CANSparkMax(ARM_ID, MotorType.kBrushless);
    }
    
    public static void goToAngle(double angle){

    }
    
}
