package frc.robot.Autonomous.Sequences;

import frc.robot.CommandsV2.Command;
import frc.robot.CommandsV2.ParallelCompile;
import frc.robot.CommandsV2.SequentialCompile;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
/*
Grabbing note
-unstow
-Slow speed intake
-Once grabbed, stow
 */
public class Grab {

    public static Command command = new SequentialCompile(
        "Grab",
        ArmCommands.raise,
        ArmCommands.forwardRail,
        new ParallelCompile(
            "Grab_Intake",
            ArmCommands.intakePos,
            IntakeCommands.intakeNote
        ),
        ArmCommands.raise,
        new ParallelCompile(
            "Grab_Stow",
            ArmCommands.backwardRail,
            ArmCommands.stow
        )
    );
}