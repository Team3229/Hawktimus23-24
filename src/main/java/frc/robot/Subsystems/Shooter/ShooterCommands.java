package frc.robot.Subsystems.Shooter;

import frc.robot.CommandsV2.Command;

public class ShooterCommands {
    
    public static Command shootAmp = new Command() {
        @Override
        public void init() {
            Shooter.spinUp(Shooter.AMP_SPEED);
        }

        @Override
        public void end() {
            Shooter.ampIntent = false;
        }

        @Override
        public boolean isDone() {
            return Shooter.atTarget();
        }

        @Override
        public String getID() {
            return "shootAmp";
        }
    };

    public static Command shootSpeaker = new Command() {

        @Override
        public void periodic() {
            Shooter.spinUp();
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public String getID() {
            return "shootSpeaker";
        }
    };
}