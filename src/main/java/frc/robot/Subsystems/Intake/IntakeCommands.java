package frc.robot.Subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeCommands {
    
    public static Command intakeNote = new Command() {
        @Override
        public void initialize() {

        }

        @Override
        public void execute() {
            Intake.intake();
        }

        @Override
        public void end(boolean interrupted) {
            Intake.stop();
        }

        @Override
        public boolean isFinished() {
            return Intake.hasNote;
        }
    };

    public static Command shootAmp = new Command() {
        @Override
        public void initialize() {

        }

        @Override
        public void execute() {
            Intake.intake();
        }

        @Override
        public void end(boolean interrupted) {
            Intake.stop();
        }

        @Override
        public boolean isFinished() {
            return Intake.hasNote;
        }
    };

}