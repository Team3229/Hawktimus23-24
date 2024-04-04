package frc.robot.Subsystems.LEDs;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Subsystems.Shooter.Shooter.ShooterStates;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Intake.Intake;

public class LEDs {
    
    private static PWMSparkMax blinkin;
    private static float setLED = 0f;

    private static float PURPLE = 0.91f;
    private static float RAINBOW = -0.99f;
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

        if (matchTime <= 25) {
            endgameShootColor = !endgameShootColor;
            setLED = STROBE_RED;
        }

        // Check if Intake has a note
        if (Intake.hasNote) {
            setLED = PURPLE;
        }

        if (Shooter.state == ShooterStates.spinningUp) {
            setLED = HEARTBEAT_GOLD;
        }
        
        if (Angular.checkTarget() & Angular.isShooting) {
            SmartDashboard.putBoolean("Ready to Shoot", true);
            setLED = AQUA;
        } else {
            SmartDashboard.putBoolean("Ready to Shoot", false);
        }
        

        blinkin.set(setLED);
    }

}
