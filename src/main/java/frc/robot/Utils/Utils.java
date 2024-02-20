package frc.robot.Utils;

public class Utils {
    public static double normalizeValue(double value, double valMin, double valMax, double newMin, double newMax){
        return (((value - valMin) * (newMax - newMin)) / (valMax - valMin)) + newMin;
    }
}
