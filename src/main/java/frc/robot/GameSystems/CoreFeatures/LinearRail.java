package frc.robot.GameSystems.CoreFeatures;
/*
-Slides to forward and backward positions
-Refuses to slide if arm is in the way
 */

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class LinearRail {

    private static CANSparkMax linearRail;
    private static final int RAIL_ID = 0;
    public static boolean isBackward = true;
    private static SparkLimitSwitch forwardLimitSwitch;    
    private static SparkLimitSwitch backwardLimitSwitch;
    private static final double RAIL_SPEED = 0.5;


    public static void init(){
        linearRail = new CANSparkMax(RAIL_ID, MotorType.kBrushless);
        forwardLimitSwitch = linearRail.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        backwardLimitSwitch = linearRail.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
    }

    public static void update(){
        if(isBackward){
            //Going backwards
            if(backwardLimitSwitch.isPressed()){
                //At back switch
                linearRail.stopMotor();
            } else {
                linearRail.set(-RAIL_SPEED);
            }
        } else {
            //Going forwards
            if(forwardLimitSwitch.isPressed()){
                //At front switch
                linearRail.stopMotor();
            } else {
                linearRail.set(RAIL_SPEED);
            }
        }
    }

    public static void toggle(){
        isBackward = !isBackward;
    }
    
}
