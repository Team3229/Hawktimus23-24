package frc.robot.Autonomous.Sequences;

import frc.robot.CommandsV2.Command;
import frc.robot.CommandsV2.ParallelCompile;
import frc.robot.CommandsV2.SequentialCompile;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Drivetrain.DrivetrainCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Subsystems.Shooter.ShooterCommands;

/*
Dropping into the amp:
-line up via odometry
-Raise arm (set angle position)
-outtake slow speed (set speed)
-Stow
 */
public class ScoreAmp {

    public static final Command command = new SequentialCompile(
        DrivetrainCommands.lineUpAmp,
        ArmCommands.ampPosition,
        new ParallelCompile(
            IntakeCommands.feed,
            ShooterCommands.shootAmp
        ),
        ArmCommands.stow
    );

}