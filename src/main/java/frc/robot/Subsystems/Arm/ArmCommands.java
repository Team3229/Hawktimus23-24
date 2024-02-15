package frc.robot.Subsystems.Arm;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class ArmCommands {
    
    public static Command stow = new Command() {
        @Override
        public void initialize() {
            Angular.stow();
        }

        @Override
        public boolean isFinished() {
            return Angular.atTarget;
        }
    };

    public static Command intakePos = new Command() {
        @Override
        public void initialize() {
            Angular.unstow();
        }

        @Override
        public boolean isFinished() {
            return Angular.atTarget;
        }
    };
    
    public static Command raise = new Command() {
        @Override
        public void initialize() {
            Angular.raise();
        }

        @Override
        public boolean isFinished() {
            return Angular.atTarget;
        }
    };
    
    public static Command ampPosition = new Command() {
        @Override
        public void initialize() {
            Angular.amp();
        }

        @Override
        public boolean isFinished() {
            return Angular.atTarget;
        }
    };

    public static Command forwardRail = new Command() {
        @Override
        public void initialize() {
            Linear.front();
        }

        @Override
        public boolean isFinished() {
            return Linear.atSide;
        }
    };

    public static Command backwardRail = new Command() {
        @Override
        public void initialize() {
            Linear.back();
        }

        @Override
        public boolean isFinished() {
            return Linear.atSide;
        }
    };

    public static Command speakerPosition(double degrees) {
        return new Command() {
            @Override
            public void initialize() {
                Angular.shoot(degrees);
            }

            @Override
            public boolean isFinished() {
                return Angular.atTarget;
            }
        };
    }

}