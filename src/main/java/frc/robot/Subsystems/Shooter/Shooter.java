package frc.robot.Subsystems.Shooter;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Utils.Deadband;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class Shooter {

    private static CANSparkMax shooter;
    private static RelativeEncoder encoder;

    private static double lastVel = 0;
    private static boolean atSpeed = false;

    private static final double SHOOTER_SPEED = 1;
    
    public static void init() {
        shooter = new CANSparkMax(9, MotorType.kBrushless);
        shooter.setInverted(true);
        encoder = shooter.getEncoder();
    }

    public static void runShooter() {
        shooter.set(SHOOTER_SPEED);
    }

    public static void stopShooter() {
        shooter.stopMotor();
    }

    public static Command spinUp = new Command() {
        @Override
        public void initialize() {
            shooter.set(SHOOTER_SPEED);
        }

        @Override
        public void execute() {
            atSpeed = Deadband.tolerance(encoder.getVelocity(), lastVel, 10);
            lastVel = encoder.getVelocity();
        }

        @Override
        public void end(boolean isInterruped) {}

        @Override
        public boolean isFinished() {
            return atSpeed;
        }
    };

    public static Command slowDown = new Command() {
        @Override
        public void initialize() {
            shooter.stopMotor();
        }

        @Override
        public boolean isFinished() {
            return true;
        }
    };

}