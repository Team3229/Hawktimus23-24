package frc.robot.Subsystems.Arm;
/*
-Slides to forward and backward positions
-Refuses to slide if arm is in the way
 */

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class Linear {

    private static CANSparkMax linearRail;
    private static final int RAIL_ID = 0;
    public static boolean goingBackwards = true;
    public static boolean atSide = false;
    private static SparkLimitSwitch forwardLimitSwitch;
    private static SparkLimitSwitch backwardLimitSwitch;
    private static final double RAIL_SPEED = 0.5;


    public static void init(){
        linearRail = new CANSparkMax(RAIL_ID, MotorType.kBrushless);
        forwardLimitSwitch = linearRail.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        backwardLimitSwitch = linearRail.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
    }

    public static void update(){
        // TODO: Could we lift this state and de-duplicate this logic? 
        // Could we do something like?: 
        // SparkLimitSwitch targetLimitSwitch = (goingBackwards) ? backwardLimitSwitch : forwardLimitSwitch;
        // atSide = targetLimitSwitch.isPressed();
        // if (atSide) {
        //     linearRail.stopMotor();
        // } else {
        //     linearRail.set(goingBackwards ? -RAIL_SPEED : RAIL_SPEED);
        // }

        if(goingBackwards){
            //Going backwards
            if(backwardLimitSwitch.isPressed()){
                //At back switch
                atSide = true;
                linearRail.stopMotor();
                //if the rail reaches the end the motor stops
            } else {
                atSide = false;
                linearRail.set(-RAIL_SPEED);
                //if the rail hasen't yet reached the end the motor goes
            }
        } else {
            //Going forwards
            if(forwardLimitSwitch.isPressed()){
                //At front switch
                atSide = true;
                linearRail.stopMotor();
                //if the rail reaches the end the motor stops
            } else {
                atSide = false;
                linearRail.set(RAIL_SPEED);
                 //if the rail hasen't yet reached the end the motor goes
            }
        }
    }

    public static void front(){
        goingBackwards = false;
    }

    public static void back(){
        goingBackwards = true;
    }
    
}
