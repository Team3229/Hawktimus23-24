package frc.robot.Subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class DrivetrainCommands {

    public static Command lineUpAmp = new Command() {
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
            return false;
        }
    };

    public static Command lineUpSpeaker(Rotation2d rotation) {
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
                return false;
            }
        };
    }
    
}