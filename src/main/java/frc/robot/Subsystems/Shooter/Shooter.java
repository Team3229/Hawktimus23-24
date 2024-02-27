package frc.robot.Subsystems.Shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Subsystems.Intake.Intake;

/*
-Spin up (to given RPM)
-Request intake to send note forward
 */
public class Shooter {

    private static CANSparkMax outtake;
    private static final int OUTTAKE_ID = 8;
    public static final double AMP_SPEED = .1;
    public static ShooterStates state = ShooterStates.idle;
    public static double targetSpeed = 0;
    public static boolean ampIntent = false;
    public static boolean atSpeed = false;
    public static final double RPM_DEADBAND = 10;
    
    private static final int SHOOT_P = 0;
    private static final int SHOOT_I = 0;
    private static final int SHOOT_D = 0;

    public static SparkPIDController pid;
    public static RelativeEncoder encoder;
    public static enum ShooterStates {
        idle,
        spinningUp
    }

    public static void init(){
        outtake = new CANSparkMax(OUTTAKE_ID, MotorType.kBrushless);
        pid = outtake.getPIDController();
        pid.setP(SHOOT_P);
        pid.setI(SHOOT_I);
        pid.setD(SHOOT_D);
        encoder = outtake.getEncoder();
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
            pid.setReference(targetSpeed,ControlType.kVelocity);
            atSpeed = Math.abs(encoder.getPosition()) <= RPM_DEADBAND;
        } else {
            stop();
        }
    }

    public static void spinUp(double speed){
        targetSpeed = speed;
        state = ShooterStates.spinningUp;
    }
    
    public static void spinUp(){
        state = ShooterStates.spinningUp;
    }

    public static void stop(){
        targetSpeed = 0;
        state = ShooterStates.idle;
        outtake.stopMotor();
    }

    public static void toggleAmpIntent(){
        ampIntent = !ampIntent;
    }
}
