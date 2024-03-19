package frc.robot.Subsystems.Arm;

import frc.robot.CommandsV2.Command;

public class ArmCommands {
    
    public static Command stow = new Command() {
        @Override
        public void init() {
            Angular.stow();
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
            Angular.raise();
        }
        @Override
        public boolean isDone() {
            return Angular.encoder.getPosition() <= Angular.RAISED+2;
        }

        @Override
        public void end() {
            Angular.runManual(0);
        }

        @Override
        public String getID() {
            return "raise";
        }
    };

    public static Command intakePos = new Command() {
        @Override
        public void init() {
            Angular.grab();
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
        }

        @Override
        public void periodic() {
            Angular.shoot();
        }

        @Override
        public boolean isDone() {
            //The arm is constantly trying to move to its target, thus this needs nothing more.
            return Angular.checkTarget();
        }

        @Override
        public String getID() {
            return "speakerPos";
        }
    };
}