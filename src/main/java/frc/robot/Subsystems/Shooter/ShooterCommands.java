package frc.robot.Subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class ShooterCommands {
    
    public static Command shootAmp = new Command() {
        @Override
        public void initialize() {
            Shooter.spinUp(Shooter.AMP_SPEED);
        }

        @Override
        public void end(boolean interrupted) {
            Shooter.ampIntent = false;
        }

        @Override
        public boolean isFinished() {
            return Shooter.atSpeed;
        }
    };

    public static Command shootSpeaker = new Command() {
        @Override
        public void initialize() {
            Shooter.spinUp();
        }

        @Override
        public boolean isFinished() {
            return Shooter.atSpeed;
        }
    };
}