package frc.robot.Sequences;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Shooter.Shooter;

public class Shoot {

    public static Command shootNote = new Command() {

        private static SequentialCommandGroup commandGroup;

        @Override
        public void initialize() {

            commandGroup = new SequentialCommandGroup(
                Shooter.spinUp,
                Intake.shootNote,
                Shooter.slowDown
            );

            commandGroup.initialize();
        }

        @Override
        public void execute() {
            commandGroup.execute();
        }

        @Override
        public void end(boolean isInterruped) {
            commandGroup.end(isInterruped);
        }

        @Override
        public boolean isFinished() {
            return commandGroup.isFinished();
        }
    };

}
