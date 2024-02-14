package frc.robot.Subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class ShooterCommands {
    
    public static Command shootAmp = new Command() {
        @Override
        public void initialize() {

        }

        @Override
        public void execute() {
            
        }

        @Override
        public void end(boolean interrupted) {
            
        }

        @Override
        public boolean isFinished() {
            return true;
        }
    };

    public static Command shootSpeaker(double RPM) {
        return new Command() {
            @Override
            public void initialize() {

            }

            @Override
            public void execute() {
                
            }

            @Override
            public void end(boolean interrupted) {
                
            }

            @Override
            public boolean isFinished() {
                return true;
            }
        };
    }

}