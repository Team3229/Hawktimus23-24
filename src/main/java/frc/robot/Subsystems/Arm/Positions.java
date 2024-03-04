package frc.robot.Subsystems.Arm;

public class Positions {
    
    public static void intakePos() {
        if (Linear.getPos() > 0.6) {
            Angular.setPos(110);
        }
        Linear.runRail(1);
    }

    public static void stowPos() {
        if (Angular.getPos() < 112) {
            Linear.runRail(0.3);
        }
        Angular.setPos(96);
    }

    public static void ampPos() {
        if (Angular.getPos() < 100) {
            Linear.runRail(0);
        }
        Angular.setPos(0);
    }

    public static void shootPos() {
        if (Angular.getPos() < 100) {
            Linear.runRail(0.3);
        }
        Angular.setPos(75);
    }

    public static void diagShootPos() {
        if (Angular.getPos() < 100) {
            Linear.runRail(0.3);
        }
        Angular.setPos(75);
    }
}