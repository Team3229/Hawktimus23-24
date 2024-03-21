package frc.robot.Subsystems.Arm;

import frc.robot.CommandsV2.Command;
import frc.robot.Subsystems.Shooter.Shooter;

public class ArmCommands {
    
    public static Command stow = new Command() {
        @Override
        public void init() {
            Angular.stow();
            Angular.isShooting = false;
        }

        @Override
        public boolean isDone() {
            return Angular.checkTarget();
        }

        @Override
        public String getID() {
            return "stow";
        }
    };

    public static Command raise = new Command(){
        @Override
        public void init(){
            Angular.isShooting = false;
            Angular.raise();
        }

        @Override
        public void periodic() {}

        @Override
        public boolean isDone() {
            return Angular.encoder.getPosition() <= 90;
        }

        @Override
        public void end() {}

        @Override
        public String getID() {
            return "raise";
        }
    };

    public static Command intakePos = new Command() {
        @Override
        public void init() {
            Angular.grab();
            Angular.isShooting = false;
        }

        @Override
        public boolean isDone() {
            return Angular.checkTarget();
        }

        @Override
        public String getID() {
            return "intakePos";
        }
    };
    
    public static Command ampPosition = new Command() {
        @Override
        public void init() {
            Angular.amp();
            Angular.isShooting = false;
        }

        @Override
        public boolean isDone() {
            return Angular.checkTarget();
        }

        @Override
        public String getID() {
            return "ampPos";
        }
    };

    public static Command forwardRail = new Command() {
        @Override
        public void init() {
            Linear.front();
        }

        @Override
        public boolean isDone() {
            return Linear.atTarget;
        }

        @Override
        public String getID() {
            return "forwardRail";
        }
    };

    public static Command backwardRail = new Command() {
        @Override
        public void init() {
            Linear.back();
        }

        @Override
        public boolean isDone() {
            return Linear.atTarget;
        }

        @Override
        public String getID() {
            return "backwardRail";
        }
    };

    public static Command speakerPosition = new Command() {

        @Override
        public void init() {
            Angular.isShooting = true;
            Angular.shoot();
            System.out.println("startSpeaker");
        }

        @Override
        public void periodic() {}

        @Override
        public boolean isDone() {
            //The arm is constantly trying to move to its target, thus this needs nothing more.
            return Angular.checkTarget() & Shooter.atTarget();
        }

        @Override
        public String getID() {
            return "speakerPos";
        }
    };
}