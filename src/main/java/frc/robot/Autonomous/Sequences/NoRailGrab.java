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
public class NoRailGrab {

    public static Command command = new SequentialCompile(
        "Grab4",
        new ParallelCompile(
            "Grab_Intake4",
            ArmCommands.intakePos,
            IntakeCommands.intakeNote
        )
    );
}