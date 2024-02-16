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
            return Shooter.currentSpeed >= Shooter.AMP_SPEED;
        }
    };

    public static Command shootSpeaker(double speed) {
        return new Command() {
            @Override
            public void initialize() {
                Shooter.spinUp(speed);
            }

            @Override
            public void end(boolean interrupted) {
                Shooter.ampIntent = false;
            }

            @Override
            public boolean isFinished() {
                return Shooter.currentSpeed >= speed;
            }
        };
    }

}