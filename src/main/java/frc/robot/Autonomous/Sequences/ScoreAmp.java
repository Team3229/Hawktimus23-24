package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Drivetrain.DrivetrainCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Subsystems.Shooter.ShooterCommands;
import frc.robot.Utils.ParallelGroup;
import frc.robot.Utils.SequentialGroup;

/*
Dropping into the amp:
-line up via odometry
-Raise arm (set angle position)
-outtake slow speed (set speed)
-Stow
 */
public class ScoreAmp {

    private static SequentialGroup sequence;

    public static Command command = new Command() {
        @Override
        public void initialize() {
            sequence = new SequentialGroup(

                DrivetrainCommands.lineUpAmp,
                ArmCommands.ampPosition,
                new ParallelGroup(
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
        public void end(boolean interrupted) {}

        @Override
        public boolean isFinished() {
            return sequence.isFinished();
        }
    };

}