// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.manip;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PIDConstants;
import frc.robot.constants.IDConstants;

public class RailSubsystem extends SubsystemBase {

  private CANSparkMax motor;
  private RelativeEncoder encoder;
  private SparkPIDController pid;

  private DigitalInput backLimit;
  private DigitalInput frontLimit;

  private boolean isHoming;

  private final double HOMING_SPEED = 0.2;

  public RailSubsystem() {

    motor = new CANSparkMax(IDConstants.RAIL, MotorType.kBrushless);
    encoder = motor.getEncoder();
    pid = motor.getPIDController();

    motor.setSmartCurrentLimit(40);

    motor.setIdleMode(IdleMode.kCoast);
    motor.setClosedLoopRampRate(0.2);

    encoder.setPositionConversionFactor((double) 1/(80));

    System.out.println(encoder.getPositionConversionFactor());

    backLimit = new DigitalInput(IDConstants.RAIL_LIMIT_BACK);
    frontLimit = new DigitalInput(IDConstants.RAIL_LIMIT_FRONT);

    pid.setP(PIDConstants.P_RAIL);
    pid.setI(PIDConstants.I_RAIL);
    pid.setD(PIDConstants.D_RAIL);
    
    pid.setFeedbackDevice(encoder);

    motor.burnFlash();

  }

  private class RailMoveCommand extends Command {
    
    private final RailSubsystem railSubsystem;
    private final boolean dir;

    public RailMoveCommand(RailSubsystem subsystem, boolean direction) {
        railSubsystem = subsystem;
        dir = direction;
        addRequirements(railSubsystem);
    }

    @Override
    public void initialize() {
      if (dir) {
        pid.setReference(0, ControlType.kPosition);
      } else {
        pid.setReference(1, ControlType.kPosition);
      }
    }

    @Override
    public void end(boolean interrupted) {
      pid.setReference(0, ControlType.kDutyCycle);
    }

    @Override
    public boolean isFinished() {
      if (dir) {
        return encoder.getPosition() < PIDConstants.ALLOWED_ERROR_RAIL;
      } else {
        return encoder.getPosition() > 1 - PIDConstants.ALLOWED_ERROR_RAIL;
      }
    }

  }


  public Command homeRail() {

    Command first = new Command() {
      @Override public void initialize() {
            pid.setReference(HOMING_SPEED, ControlType.kDutyCycle);
            isHoming = true;
        }

        @Override public void end(boolean interrupted) {
          pid.setReference(0, ControlType.kDutyCycle);
        }

        @Override public boolean isFinished() { return frontLimit.get(); }

        @Override public String getName() {return "Homing Rail...";}
    };

    Command second = new Command() {
      @Override public void initialize() {
        pid.setReference(-HOMING_SPEED/4, ControlType.kDutyCycle);
      }

      @Override public void end(boolean interrupted) {
        pid.setReference(0, ControlType.kDutyCycle);
        isHoming = false;
        if (!interrupted) {
          encoder.setPosition(1);
        }
      }

      @Override public boolean isFinished() { return !frontLimit.get(); }
    };

    first.addRequirements(this);
    second.addRequirements(this);

    return new SequentialCommandGroup(
        first,
        second
    );

  }

  public Command forwardRail() {
    return new RailMoveCommand(this, false).withName("Forwards");
  }

  public Command backwardRail() {
    return new RailMoveCommand(this, true).withName("Backwards");
  }

  public Command forceStopRail() {
    return this.runOnce(this::forceStop);
  }

  @Override
  public void periodic() {

    if (!isHoming && backLimit.get()) {
      if (pid.getOutputMin() != 0) pid.setOutputRange(0, 1);
      encoder.setPosition(0);
    }

    if (!isHoming && frontLimit.get()) {
      if (pid.getOutputMax() != 0) pid.setOutputRange(-1, 0);
      encoder.setPosition(1);
    }

    if (!frontLimit.get() && !backLimit.get()) {
      if (!(pid.getOutputMax() == 1 && pid.getOutputMin() == -1)) pid.setOutputRange(-1, 1);
    }
  }

  public void forceStop() {
    isHoming = false;
    pid.setReference(0, ControlType.kDutyCycle);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addBooleanProperty("homing", () -> isHoming, null);
    builder.addBooleanProperty("frontLimitPressed", () -> frontLimit.get(), null);
    builder.addBooleanProperty("backLimitPressed", () -> backLimit.get(), null);
    builder.addDoubleProperty("position", () -> encoder.getPosition(), null);
  }

}
