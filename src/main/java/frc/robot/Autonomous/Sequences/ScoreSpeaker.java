package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Drivetrain.DrivetrainCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Subsystems.Shooter.ShooterCommands;
import frc.robot.Utils.ParallelGroup;
import frc.robot.Utils.SequentialGroup;

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
    
    private static SequentialGroup sequence;
    public static Command command = new Command() {
        @Override
        public void initialize() {
            sequence = new SequentialGroup(
                new ParallelGroup(
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
        public void end(boolean interrupted) {}

        @Override
        public boolean isFinished() {
            return sequence.isFinished();
        }
    };
}