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
    private static double LINEAR_DEADBAND = 0.05;
    public static boolean goingBackwards = false;

    public static void init(){
        linearRail = new CANSparkMax(RAIL_ID, MotorType.kBrushless);

        encoder = linearRail.getAlternateEncoder(8192);
        encoder.setPositionConversionFactor(1/2.0968017578125);
        encoder.setInverted(true);

        encoder.setPosition(1);

        pid = linearRail.getPIDController();
        pid.setFeedbackDevice(encoder);
        pid.setP(1.5);
        pid.setOutputRange(-1, 1);
    }

    public static void update(){
        atTarget = Math.abs(encoder.getPosition() - (goingBackwards ? 0 : 1)) <= LINEAR_DEADBAND;
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
