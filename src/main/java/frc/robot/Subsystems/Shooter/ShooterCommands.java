package frc.robot.Subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class ShooterCommands {
    
    public static Command spinUp = new Command() {

        double timePassed = 0;

        @Override
        public void initialize() {
            Shooter.runShooter();
        }

        @Override
        public void execute() {
            timePassed += 0.02;
        }

        @Override
        public void end(boolean interrupted) {
            
        }

        @Override
        public boolean isFinished() {
            return timePassed > 3;
        }
    };

    public static Command stopShooter = new Command() {

        @Override
        public void initialize() {
            Shooter.stopShooter();
        }

        @Override
        public void execute() {}

        @Override
        public void end(boolean interrupted) {
            
        }

        @Override
        public boolean isFinished() {
            return true;
        }
    };

}