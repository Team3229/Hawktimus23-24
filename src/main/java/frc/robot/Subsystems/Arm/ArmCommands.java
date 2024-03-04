package frc.robot.Subsystems.Arm;

import edu.wpi.first.wpilibj2.command.Command;

public class ArmCommands {
    public static Command intakePos = new Command() {

        double timePassed = 0;

        @Override
        public void initialize() {
            Positions.intakePos();
        }

        @Override
        public void execute() {
            timePassed += 0.02;
        }

        @Override
        public void end(boolean interrupted) {}

        @Override
        public boolean isFinished() {
            return timePassed > 2;
        }

    };

    public static Command shootPos = new Command() {

        double timePassed = 0;

        @Override
        public void initialize() {
            Positions.shootPos();
        }

        @Override
        public void execute() {
            timePassed += 0.02;
        }

        @Override
        public void end(boolean interrupted) {}

        @Override
        public boolean isFinished() {
            return timePassed > 2;
        }

    };
}
