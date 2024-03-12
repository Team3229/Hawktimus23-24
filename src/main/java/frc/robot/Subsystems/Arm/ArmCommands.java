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
            return Angular.atTarget;
        }
    };

    public static Command raise = new Command(){
        @Override
        public void init(){
            Angular.raise();
        }
        @Override
        public boolean isDone() {
            return Angular.atTarget;
        }
    };

    public static Command intakePos = new Command() {
        @Override
        public void init() {
            Angular.grab();
        }

        @Override
        public boolean isDone() {
            return Angular.atTarget;
        }
    };
    
    public static Command ampPosition = new Command() {
        @Override
        public void init() {
            Angular.amp();
        }

        @Override
        public boolean isDone() {
            return Angular.atTarget;
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
    };

    public static Command speakerPosition = new Command() {
        @Override
        public boolean isDone() {
            //The arm is constantly trying to move to its target, thus this needs nothing more.
            return Angular.atTarget;
        }
    };
}