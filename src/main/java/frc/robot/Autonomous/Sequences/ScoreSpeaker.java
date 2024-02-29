package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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
    
    private static SequentialCommandGroup sequence;
    public static Command command = new Command() {
        @Override
        public void initialize() {
            sequence = new SequentialCommandGroup(
                new ParallelCommandGroup(
                    ShooterCommands.shootSpeaker,
                    ArmCommands.speakerPosition,
                    DrivetrainCommands.lineUpSpeaker
                ),
                IntakeCommands.feed,
                ArmCommands.stow
            );
            sequence.initialize();
        }

        @Override
        public void execute() {
            sequence.execute();
        }

        @Override
        public void end(boolean interrupted) {
            sequence.end(interrupted);
        }

        @Override
        public boolean isFinished() {
            return sequence.isFinished();
        }
    };
}