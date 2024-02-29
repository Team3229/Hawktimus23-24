package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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

    private static SequentialCommandGroup sequence;

    public static Command command = new Command() {
        @Override
        public void initialize() {
            sequence = new SequentialCommandGroup(

                DrivetrainCommands.lineUpAmp,
                ArmCommands.ampPosition,
                new ParallelCommandGroup(
                    IntakeCommands.feed,
                    ShooterCommands.shootAmp
                ),
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