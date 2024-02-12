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
    public static OuttakeStates state = OuttakeStates.idle;
    private static double targetSpeed = 0;
    public static double currentSpeed = 0;
    private static enum OuttakeStates {
        idle,
        spinningUp
    }

    public static void init(){
        outtake = new CANSparkMax(OUTTAKE_ID, MotorType.kBrushless);
    }

    public static void update(){
        switch(state){
            case idle:
                stop();
                break;
            case spinningUp:
                spinningUp();
                break;
        }
    }

    private static void spinningUp(){
        if(Intake.hasNote){
            outtake.set(targetSpeed);
            currentSpeed = outtake.get();
        } else {
            stop();
        }
    }

    public static void spinUp(double target){
        targetSpeed = target;
        state = OuttakeStates.spinningUp;
    }

    public static void stop(){
        currentSpeed = 0;
        targetSpeed = 0;
        state = OuttakeStates.idle;
        outtake.stopMotor();
    }
}
