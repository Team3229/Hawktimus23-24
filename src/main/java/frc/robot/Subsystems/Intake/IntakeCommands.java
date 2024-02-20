package frc.robot.Subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeCommands {
    
    public static Command intakeNote = new Command() {
        @Override
        public void initialize() {
            Intake.intake();
        }

        @Override
        public void execute() {}

        @Override
        public void end(boolean interrupted) {
            Intake.stop();
        }

        @Override
        public boolean isFinished() {
            return Intake.hasNote;
        }
    };

    public static Command feed = new Command() {
        @Override
        public void initialize() {
            Intake.feed();
        }

        @Override
        public void execute() {}

        @Override
        public void end(boolean interrupted) {
            Intake.stop();
        }

        @Override
        public boolean isFinished() {
            return !Intake.hasNote;
        }
    };

}