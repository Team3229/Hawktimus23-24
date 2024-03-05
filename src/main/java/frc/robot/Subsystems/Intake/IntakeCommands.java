package frc.robot.Subsystems.Intake;

import frc.robot.CommandsV2.Command;

public class IntakeCommands {
    
    public static Command intakeNote = new Command() {
        @Override
        public void init() {
            Intake.intake();
        }

        @Override
        public void periodic() {}

        @Override
        public void end() {
            Intake.stop();
        }

        @Override
        public boolean isDone() {
            return Intake.hasNote;
        }
    };

    public static Command feed = new Command() {
        @Override
        public void init() {
            Intake.feed();
        }

        @Override
        public void periodic() {}

        @Override
        public void end() {
            Intake.stop();
        }

        @Override
        public boolean isDone() {
            return !Intake.hasNote;
        }
    };

}