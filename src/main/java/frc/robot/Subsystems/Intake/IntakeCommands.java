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

        @Override
        public String getID() {
            return "intakeNote";
        }
    };

    public static Command stop = new Command() {
        @Override
        public void init() {
            Intake.stop();
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public String getID() {
            return "stopIntake";
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

        @Override
        public String getID() {
            return "intakeFeed";
        }
    };

}