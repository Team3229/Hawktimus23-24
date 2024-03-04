package frc.robot.Subsystems.Intake;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;

import com.revrobotics.CANSparkMax;

public class Intake {
    
    private static CANSparkMax intake;

    private static DigitalInput irSensor;

    public static void init() {
        intake = new CANSparkMax(8, MotorType.kBrushless);
        irSensor = new DigitalInput(0);
    }

    public static void runIntake() {
        if(irSensor.get()) {
            intake.set(0.5);
        } else {
            stopIntake();
        }
    }

    public static void fireNote() {
        intake.set(1);
    }

    public static void stopIntake() {
        intake.stopMotor();
    }

    public static void eject() {
        intake.set(-0.2);
    }

    public static Command intakeNote = new Command() {
        @Override
        public void initialize() {
            
            intake.set(1);
        }

        @Override
        public void end(boolean isInterruped) {
            intake.stopMotor();
        }

        @Override
        public boolean isFinished() {
            return irSensor.get();
        }
    };

    public static Command shootNote = new Command() {

        private double timer;

        @Override
        public void initialize() {
            intake.set(1);
            timer = 0;
        }

        @Override
        public void execute() {
            timer += 0.02;
        }

        @Override
        public void end(boolean isInterruped) {
            intake.stopMotor();
        }

        @Override
        public boolean isFinished() {
            return (timer >= 1);
        }
    };

}