package frc.robot.Subsystems.LEDs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Subsystems.Shooter.Shooter.ShooterStates;

public class LEDs {
    
    private static PWMSparkMax blinkin;
    private static float setLED = 0f;

    private static float RAINBOW = -0.99f;
    private static float HEARTBEAT_GOLD = 0.25f;
    private static float STROBE_RED = -0.11f;
    private static float AQUA = 0.81f;

    private static boolean endgameShootColor;

    public static void init() {
        blinkin = new PWMSparkMax(0);
    }

    public static void periodic() {

        setLED = RAINBOW;
        endgameShootColor = false;

        if (Shooter.state == ShooterStates.spinningUp) {
            setLED = HEARTBEAT_GOLD;
        }
        
        if (Shooter.atTarget()) {
            if (endgameShootColor) {
                setLED = STROBE_RED;
            } else {
                setLED = AQUA;
            }
        }

        if (DriverStation.getMatchTime() <= 20) {
            endgameShootColor = !endgameShootColor;
        }

        blinkin.set(setLED);
    }

}
