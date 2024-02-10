package frc.robot.GameSystems.CoreFeatures;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

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
    private static final int INTAKE_ID = 0;

    public static void init(){
        intake = new CANSparkMax(INTAKE_ID, MotorType.kBrushless);
    }

    private static void detectNote(){

    }

    public static void spinUp(double rpm){

    }

    public static void stop(){

    }
}
;