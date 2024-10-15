// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.inputs.FlightStick;
import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.RailSubsystem;
import frc.robot.subsystems.drive.DriveSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  public FlightStick driveStick;
  public FlightStick manipStick;

  private RailSubsystem rail;
  private DriveSubsystem drive;
  private LEDSubsystem leds;

  private Command homeRail;

  private SendableChooser<String> autoDropdown = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    driveStick = new FlightStick(0);
    manipStick = new FlightStick(1);

    rail = new RailSubsystem();
    drive = new DriveSubsystem(driveStick::a_X, driveStick::a_Y, driveStick::a_Z);
    leds = new LEDSubsystem();

    homeRail = rail.homeRail();

    leds.setPattern(LEDSubsystem.Pattern.Rainbow);

    registerTelemetry();

    autoDropdown.addOption("", "");

    SmartDashboard.putData(autoDropdown);

  }

  public void configureForTeleop() {

    manipStick.b_Trigger().onTrue(homeRail);

    manipStick.b_Hazard().onTrue(rail.forwardRail());
    manipStick.b_4().onTrue(rail.backwardRail());

  }

  // Runs once when enabled in auto
  public void configureForAuto() {

    drive.generateAuto(autoDropdown.getSelected());
    drive.executeAutoDrivePath();

  }

  private void registerTelemetry() {
    SmartDashboard.putData(rail);
    SmartDashboard.putData(drive);
    SmartDashboard.putData(CommandScheduler.getInstance());
  }

}
