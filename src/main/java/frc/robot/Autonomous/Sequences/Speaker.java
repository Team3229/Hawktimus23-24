package frc.robot.Autonomous.Sequences;

import frc.robot.CommandsV2.Command;
import frc.robot.CommandsV2.ParallelCompile;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Shooter.ShooterCommands;

public class Speaker {
    public static Command command() {
        return new ParallelCompile(
            "Speaker",
            ArmCommands.speakerPosition,
            ShooterCommands.shootSpeaker
        );
    }
}
