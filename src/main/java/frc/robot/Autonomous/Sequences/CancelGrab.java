package frc.robot.Autonomous.Sequences;

import frc.robot.CommandsV2.Command;
import frc.robot.CommandsV2.SequentialCompile;
import frc.robot.Subsystems.Intake.IntakeCommands;
/*
Grabbing note
-unstow
-Slow speed intake
-Once grabbed, stow
 */
public class CancelGrab {

    public static Command command() {
        return new SequentialCompile(
            "CancelGrab",
            IntakeCommands.stop,
            Stow.command()
        );
    }
}