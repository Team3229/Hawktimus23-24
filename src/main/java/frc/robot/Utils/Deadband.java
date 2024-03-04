package frc.robot.Utils;

public class Deadband {
    
    public static boolean tolerance(double value, double check, double deadband) {
        return (value < (check + deadband) & value > (check - deadband));
    }

}
