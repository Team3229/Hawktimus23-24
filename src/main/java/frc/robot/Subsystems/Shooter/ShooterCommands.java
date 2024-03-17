package frc.robot.Subsystems.Shooter;

import frc.robot.CommandsV2.Command;

public class ShooterCommands {
    
    public static Command stop = new Command(){
        public void init() {
            Shooter.stop();
        };

        public boolean isDone(){
            return true;
        };
    };

}