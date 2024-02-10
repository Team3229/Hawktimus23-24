package frc.robot.GameSystems.CoreFeatures;
/*
-Slides to forward and backward positions
-Refuses to slide if arm is in the way
 */

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class LinearRail {

    private static CANSparkMax linearRail;
    private static final int RAIL_ID = 0;
    public static boolean isBackward = true;

    public static void init(){
        linearRail = new CANSparkMax(RAIL_ID, MotorType.kBrushless);
    }
    public static void toggle(){

    }
    
}
