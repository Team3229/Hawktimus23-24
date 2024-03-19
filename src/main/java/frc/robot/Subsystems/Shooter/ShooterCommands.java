package frc.robot.Subsystems.Shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

        double timePassed = 0;

        @Override
        public void init() {
            
            Shooter.spinUp();
            
        }

        @Override
        public void periodic() {
            timePassed += 0.05;
        }

        @Override
        public boolean isDone() {
            if (timePassed >= 5) {
                timePassed = 0;
                return true;
            }
            return false;
        }

        @Override
        public String getID() {
            return "shootSpeaker";
        }
    };
}