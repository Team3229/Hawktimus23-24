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
    public static boolean hasNote = true;
    private static CANSparkMax intake;
    private static final int INTAKE_ID = 8;
    private static DigitalInput sensor;
    private static final int SENSOR_CHANNEL = 0;
    public static IntakeStates state = IntakeStates.idle;
    public enum IntakeStates {
        intaking,
        feed,
        ejecting,
        idle,
        forceeject
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
            case forceeject:
                run(0.35);
                break;
        }
    }

    private static void ejecting(){
        if(hasNote){
            intake.set(-0.2);
        } else {
            stop();
        }
    }
    private static void intaking(){
        if(!hasNote){
            intake.set(0.6);
        } else {
            stop();
        }
    }

    private static void feeding(){
        if(hasNote){
            intake.set(1);
        } else {
            stop();
        }
    }

    public static void run(double speed) {
        intake.set(speed);
    }

    public static void forceeject() {
        state = IntakeStates.forceeject;
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