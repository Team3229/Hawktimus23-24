package frc.robot.GameSystems.CoreFeatures;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

/*
-Spin up (to given RPM)
-Request intake to send note forward
 */
public class Outtake {

    private static CANSparkMax outtake;
    private static final int OUTTAKE_ID = 0;

    public static void init(){
        outtake = new CANSparkMax(OUTTAKE_ID, MotorType.kBrushless);
    }
    public static void spinUp(double rpm){

    }
    
}
