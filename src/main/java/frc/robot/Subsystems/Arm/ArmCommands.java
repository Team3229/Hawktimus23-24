package frc.robot.Subsystems.Arm;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class ArmCommands {
    
    public static Command stowPosition = new Command() {
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

    public static Command intakePosition = new Command() {
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
    
    public static Command ampPosition = new Command() {
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

    public static Command speakerPosition(Rotation2d rot) {
        return new Command() {
            @Override
            public void initialize() {

            }

            @Override
            public void execute() {
                Angular.goToAngle(rot);
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