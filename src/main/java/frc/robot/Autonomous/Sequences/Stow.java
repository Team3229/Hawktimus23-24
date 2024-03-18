package frc.robot.Autonomous.Sequences;

import frc.robot.CommandsV2.Command;
import frc.robot.CommandsV2.SequentialCompile;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Subsystems.Shooter.ShooterCommands;

public class Stow {
    
    public static Command command = new SequentialCompile(
        ShooterCommands.stop,
        IntakeCommands.stop,
        ArmCommands.raise,
        ArmCommands.backwardRail,
        ArmCommands.stow
    );

}