package frc.robot.Subsystems.Arm;
/*
-Slides to forward and backward positions
-Refuses to slide if arm is in the way
 */

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class Linear {

    private static CANSparkMax linearRail;
    private static final int RAIL_ID = 7;
    private static SparkPIDController pid;

    private static RelativeEncoder encoder;
    public static boolean atTarget = false;
    private static double LINEAR_DEADBAND = 1;
    private static double DISTANCE = 8;
    public static boolean goingBackwards = false;

    private static final int LINEAR_P = 0;
    private static final int LINEAR_I = 0;
    private static final int LINEAR_D = 0;

    public static void init(){
        linearRail = new CANSparkMax(RAIL_ID, MotorType.kBrushless);
        pid = linearRail.getPIDController();
        encoder = linearRail.getEncoder();
        encoder.setPositionConversionFactor(Math.PI * 1.375 * 36 * DISTANCE);
        pid.setP(LINEAR_P);
        pid.setI(LINEAR_I);
        pid.setD(LINEAR_D);

    }

    public static void update(){
        atTarget = Math.abs(encoder.getPosition()) <= LINEAR_DEADBAND;
    }

    public static void front(){
        pid.setReference(1, ControlType.kPosition);
        goingBackwards = false;
    }

    public static void back(){
        pid.setReference(0, ControlType.kPosition);
        goingBackwards = true;
    }
    
}
