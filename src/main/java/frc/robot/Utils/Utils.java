package frc.robot.Utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Utils {

    public static enum RobotStates {
        Teleop,
        Autonomous,
        Test,
        Disabled
    }

    public static double normalizeValue(double value, double valMin, double valMax, double newMin, double newMax){
        return (((value - valMin) * (newMax - newMin)) / (valMax - valMin)) + newMin;
    }

    // Returns true if red alliance
    public static Alliance getAlliance() {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            return alliance.get();
        }
        return Alliance.Blue;
    }

    public static RobotStates getRobotState() {
        if (DriverStation.isAutonomousEnabled()) {
            return RobotStates.Autonomous;
        }
        
        if (DriverStation.isTeleopEnabled()) {
            return RobotStates.Teleop;
        }

        if (DriverStation.isTestEnabled()) {
            return RobotStates.Test;
        }

        return RobotStates.Disabled;
    }

    
}