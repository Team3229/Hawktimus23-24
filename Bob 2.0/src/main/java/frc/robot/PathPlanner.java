package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.DriveSystem.Swerve.SwerveKinematics;
import frc.robot.DriveSystem.Swerve.SwerveOdometry;

public class PathPlanner extends SubsystemBase {

    public PathPlanner() {
        
        // Configure AutoBuilder last
        AutoBuilder.configureHolonomic(
                SwerveOdometry::getPose, // Robot pose supplier
                SwerveOdometry::setPose, // Method to reset odometry (will be called if your auto has a starting pose)
                SwerveKinematics::getSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                SwerveKinematics::drive, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
                new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your
                                                 // Constants class
                        new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
                        4.5, // Max module speed, in m/s
                        Math.sqrt(2 * (Math.pow(SwerveKinematics.robotWidth/2 - SwerveKinematics.moduleEdgeOffset, 2))), // Drive base radius in meters. Distance from robot center to furthest module.
                        new ReplanningConfig() // Default path replanning config. See the API for the options here
                ),
                this // Reference to this subsystem to set requirements
        );
    }

    public Command getCommand(String autoName) {
        return new PathPlannerAuto(autoName);
    }
}