// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.inputs.FlightStick;
import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.swerve.DriveSubsystem;
import frc.robot.subsystems.manip.ManipSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  public FlightStick driveStick;
  public FlightStick manipStick;

  private DriveSubsystem drive;
  @SuppressWarnings("unused")
  private LEDSubsystem leds;
  private ManipSubsystem manip;

  private SendableChooser<String> autoDropdown = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    driveStick = new FlightStick(0);
    manipStick = new FlightStick(1);

    drive = new DriveSubsystem(driveStick::a_X, driveStick::a_Y, driveStick::a_Z);
    leds = new LEDSubsystem();
    manip = new ManipSubsystem(manipStick::a_Y);

    registerTelemetry();

    autoDropdown.addOption("", "");

    SmartDashboard.putData(autoDropdown);

  }

  public void setupControls() {

    driveStick.b_10().onTrue(drive.zeroGyro());

    // driveStick.b_Trigger().onTrue(drive.pointTowards(driveStick::a_X, driveStick::a_Y, () -> 0));

    manipStick.b_10().onTrue(manip.homeRail());

    manipStick.b_8().onTrue(manip.railFront());
    manipStick.b_7().onTrue(manip.railBack());

    manipStick.b_3().onTrue(manip.readyShooterCommand());
    manipStick.b_Trigger().onTrue(manip.shootCommand());

    manipStick.b_4().onTrue(manip.grabCommand());

    manipStick.b_Hazard().onTrue(manip.stowCommand());

  }

  // Runs once when enabled in auto
  public void setupAuto() {

    // drive.generateAuto(autoDropdown.getSelected());
    // drive.executeAutoDrivePath();

    new SequentialCommandGroup(
        manip.initialHoming(),
        manip.readyShooterCommand(),
        manip.shootCommand(),
        drive.taxi()
    ).schedule();

  }

  private void registerTelemetry() {
    SmartDashboard.putData(drive);
    SmartDashboard.putData(manip);
    SmartDashboard.putData(CommandScheduler.getInstance());
  }

}
