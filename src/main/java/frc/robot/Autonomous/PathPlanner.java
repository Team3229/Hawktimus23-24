package frc.robot.Autonomous;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Drivetrain.SwerveKinematics;
import frc.robot.Drivetrain.SwerveOdometry;

public class PathPlanner extends SubsystemBase {

    private HolonomicPathFollowerConfig autoConfig = 
        new HolonomicPathFollowerConfig(

            new PIDConstants(40, 0.0, 0.5), // Translation PID constants
            new PIDConstants(5, 0.0, 0.0), // Rotation PID constants
            SwerveKinematics.maxModuleSpeed, // Max module speed, in m/s
            Math.sqrt(2 * (Math.pow(SwerveKinematics.robotWidth/2 - SwerveKinematics.moduleEdgeOffset, 2))), // Drive base radius in meters. Distance from robot center to furthest module.
            new ReplanningConfig() // Default path replanning config. See the API for the options here

        );

    public PathPlanner() {
        
        // Configure AutoBuilder last
        AutoBuilder.configureHolonomic(
                SwerveOdometry::getPose, // Robot pose supplier
                SwerveOdometry::setPose, // Method to reset odometry (will be called if your auto has a starting pose)
                SwerveKinematics::getSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                SwerveKinematics::drive, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
                autoConfig,
                () -> {
                    // Boolean supplier that controls when the path will be mirrored for the red alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                this // Reference to this subsystem to set requirements
        );

		SmartDashboard.putData("Choose Auto", this.getDropdown());
    }

    public SendableChooser<Command> getDropdown() {
        return AutoBuilder.buildAutoChooser();
    }
}