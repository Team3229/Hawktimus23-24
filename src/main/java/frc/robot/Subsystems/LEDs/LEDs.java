package frc.robot.Subsystems.LEDs;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Subsystems.Shooter.Shooter.ShooterStates;
import frc.robot.Subsystems.Intake.Intake;

public class LEDs {
    
    private static PWMSparkMax blinkin;
    private static float setLED = 0f;

    private static float RAINBOW = -0.99f;
    private static float BLUE = 1.0f;
    private static float HEARTBEAT_GOLD = 0.25f;
    private static float STROBE_RED = -0.11f;
    private static float AQUA = 0.81f;

    private static boolean endgameShootColor;

    public static int matchTime = 135;

    public static void init() {
        blinkin = new PWMSparkMax(2);
        SmartDashboard.putNumber("Match Time", 0);
    }

    public static void periodic() {

        SmartDashboard.putNumber("Match Time", matchTime);

        setLED = RAINBOW;
        endgameShootColor = false;

        if (Shooter.state == ShooterStates.spinningUp) {
            setLED = HEARTBEAT_GOLD;
        }

        if (matchTime <= 25) {
            endgameShootColor = !endgameShootColor;
            setLED = STROBE_RED;
        }
        
        if (Shooter.atTarget() & Shooter.targetSpeed != 0) {
            setLED = AQUA;
            if (endgameShootColor) {
                setLED = STROBE_RED;
            }
        }
        // Check if Intake has a note
        if ( Intake.hasNote == true) {
            setLED = BLUE;
        }




        blinkin.set(setLED);
    }

}
