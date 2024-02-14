package frc.robot.Subsystems.Arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;

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
    
    public static void goToAngle(Rotation2d angle){

    }
    
}
