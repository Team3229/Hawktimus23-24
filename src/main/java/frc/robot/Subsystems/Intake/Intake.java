package frc.robot.Subsystems.Intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;

/*
Controls intaking a note
-Detect a note
-Spin up/down intake
-Stop intaking w/ note
-Nudge note into outtake
-Eject note
 */
public class Intake {
    public static boolean hasNote = false;
    private static CANSparkMax intake;
    private static final int INTAKE_ID = 9;
    private static final double INTAKE_SPEED = 0.5;
    private static DigitalInput sensor;
    private static final int SENSOR_CHANNEL = 0;
    public static IntakeStates state = IntakeStates.idle;
    public enum IntakeStates {
        intaking,
        feed,
        ejecting,
        idle
    }

    public static void init(){
        sensor = new DigitalInput(SENSOR_CHANNEL);
        intake = new CANSparkMax(INTAKE_ID, MotorType.kBrushless);
    }

    public static void update(){
        detectNote();
        switch(state){
            case ejecting:
                ejecting();
                break;
            case idle:
                stop();
                break;
            case intaking:
                intaking();
                break;
            case feed:
                feeding();
                break;
        }
    }

    private static void ejecting(){
        if(hasNote){
            intake.set(INTAKE_SPEED);
        } else {
            stop();
        }
    }

    private static void intaking(){
        if(!hasNote){
            intake.set(-INTAKE_SPEED);
        } else {
            stop();
        }
    }

    private static void feeding(){
        if(hasNote){
            intake.set(-INTAKE_SPEED);
        } else {
            stop();
        }
    }

    private static boolean detectNote(){
        hasNote = !sensor.get();
        return hasNote;
    }

    public static void intake(){
        state = IntakeStates.intaking;
    }

    public static void eject(){
        state = IntakeStates.ejecting;
    }

    public static void feed(){
        state = IntakeStates.feed;
    }

    public static void stop(){
        state = IntakeStates.idle;
        intake.stopMotor();
    }
}