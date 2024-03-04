package frc.robot.Subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Shooter.Shooter;

public class IntakeCommands {
    
    public static Command fireNote = new Command() {

        double timePassed = 0;

        @Override
        public void initialize() {
            Intake.fireNote();
        }

        @Override
        public void execute() {
            timePassed += 0.02;
        }

        @Override
        public void end(boolean interrupted) {
            Intake.stopIntake();
            Shooter.stopShooter();
        }

        @Override
        public boolean isFinished() {
            return timePassed > 1;
        }

    };

    public static Command intakeNote = new Command() {

        double timePassed = 0;

        @Override
        public void initialize() {
            Intake.runIntake();
        }

        @Override
        public void execute() {
            timePassed += 0.02;
        }

        @Override
        public void end(boolean interrupted) {
            Intake.stopIntake();
        }

        @Override
        public boolean isFinished() {
            return timePassed > 2;
        }

    };

    public static Command stopIntake = new Command() {

        @Override
        public void initialize() {
            Intake.stopIntake();
        }

        @Override
        public void execute() {}

        @Override
        public void end(boolean interrupted) {}

        @Override
        public boolean isFinished() {
            return true;
        }

    };

}