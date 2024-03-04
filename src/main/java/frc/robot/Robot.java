// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Subsystems;
import frc.robot.Subsystems.Drivetrain.ModuleOffsets;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */

 
public class Robot extends TimedRobot {

  PathPlanner auto;

  SendableChooser<Command> autoDropdown;
  Command autoCommand;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    Subsystems.init();

    auto = new PathPlanner();
    autoDropdown = auto.getDropdown();

    SmartDashboard.putData("Choose Auto", autoDropdown);

  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {

    SwerveKinematics.zeroGyro();

		SwerveOdometry.init(new Pose2d(new Translation2d(), SwerveKinematics.robotRotation));

    SwerveKinematics.configureDrivetrain();
    ModuleOffsets.checkBoolean();

    SwerveKinematics.chassisState = new ChassisSpeeds();

    autoCommand = autoDropdown.getSelected();

    autoCommand.initialize();
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

    SwerveOdometry.update(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions);

    if (!autoCommand.isFinished()) {
      autoCommand.execute();
    } else {
      SwerveKinematics.stop();
    }

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {

    SwerveKinematics.zeroGyro();

		SwerveOdometry.init(new Pose2d(new Translation2d(), SwerveKinematics.robotRotation));

    SwerveKinematics.configureDrivetrain();
    ModuleOffsets.checkBoolean();

    SwerveKinematics.chassisState = new ChassisSpeeds();

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    SwerveOdometry.update(SwerveKinematics.robotRotation, SwerveKinematics.modulePositions);

    Subsystems.execute();

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
