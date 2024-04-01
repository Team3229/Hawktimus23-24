package frc.robot.Autonomous.Sequences;

import frc.robot.CommandsV2.Command;
import frc.robot.CommandsV2.ParallelCompile;
import frc.robot.CommandsV2.SequentialCompile;
import frc.robot.Subsystems.Arm.ArmCommands;
/*
Grabbing note
-unstow
-Slow speed intake
-Once grabbed, stow
 */
public class OpGrabEnd {

    public static Command command = new SequentialCompile(
        "Grab2",
        ArmCommands.raise,
        new ParallelCompile(
            "Grab_Stow2",
            ArmCommands.backwardRail,
            ArmCommands.stow
        )
    );
}