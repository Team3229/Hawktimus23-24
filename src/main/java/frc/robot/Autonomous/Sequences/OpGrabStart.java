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
public class OpGrabStart {

    public static Command command = new SequentialCompile(
        "Grab3",
        ArmCommands.raise,
        ArmCommands.forwardRail,
        new ParallelCompile(
            "Grab_Intake2",
            ArmCommands.intakePos,
            IntakeCommands.intakeNote
        )
    );
}