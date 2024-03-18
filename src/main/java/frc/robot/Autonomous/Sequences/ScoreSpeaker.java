package frc.robot.Autonomous.Sequences;

import frc.robot.CommandsV2.Command;
import frc.robot.CommandsV2.ParallelCompile;
import frc.robot.CommandsV2.SequentialCompile;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Drivetrain.DrivetrainCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Subsystems.Shooter.ShooterCommands;

/*
Shooting note
-automatically stop driver movement
    -After a short delay (.75seconds?)
-Rotate bot, angle arm to needed locations
-Spin up Outtake
-nudge note into shooter w/ intake
-reset everything, stow
-Return driver control
 */
public class ScoreSpeaker {
    
    public static Command command = new SequentialCompile(
        new ParallelCompile(
            ShooterCommands.shootSpeaker,
            ArmCommands.speakerPosition,
            DrivetrainCommands.lineUpSpeaker
        ),
        IntakeCommands.feed,
        ArmCommands.stow
    );
    
}