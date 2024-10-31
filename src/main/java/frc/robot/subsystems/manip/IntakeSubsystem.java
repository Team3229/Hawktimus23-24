package frc.robot.subsystems.manip;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.IDConstants;

public class IntakeSubsystem extends SubsystemBase {
    
    private static final int kReverseMaxIntakeSpeed = -1;
    private static final int kMaxIntakeSpeed = 1;
    private static final double kIntakingSpeed = 0.8;
    private static final int kCurrentLimit = 80;
    private static final int kIrChannel = 0;
    private CANSparkMax intakeMotor;
    private DigitalInput irSensor;

    public Trigger note;

    public IntakeSubsystem() {

        intakeMotor = new CANSparkMax(IDConstants.INTAKE_MOTOR, MotorType.kBrushless);
        irSensor = new DigitalInput(kIrChannel);

        note = new Trigger(this::hasNote);

        intakeMotor.setSmartCurrentLimit(kCurrentLimit);

        intakeMotor.burnFlash();

    }

    private boolean hasNote() {
        return !irSensor.get();
    }

    public Command grabNote() {
        Command out = new Command() {
            
            @Override public void initialize() {
                if (!isFinished()) intakeMotor.set(kIntakingSpeed);
            }

            @Override public void end(boolean interrupted) {
                intakeMotor.stopMotor();
            }

            @Override public boolean isFinished() {
                return !irSensor.get();
            }

        };

        out.addRequirements(this);
        return out;
    }

    public Command feedNoteToShooter(Supplier<Boolean> isReady) {
        Command out = new Command() {
            @Override public void initialize() {

                if (!isFinished() && isReady.get()) {
                    intakeMotor.set(kMaxIntakeSpeed);
                }
            }

            @Override
            public void execute() {
                if (isReady.get()) {
                    intakeMotor.set(kMaxIntakeSpeed);
                }
            }

            @Override public void end(boolean interrupted) {
                intakeMotor.stopMotor();
            }

            @Override public boolean isFinished() {
                return irSensor.get();
            }
        };

        out.addRequirements(this);

        return out;
    }

    public void ejectNote() {
        intakeMotor.set(kReverseMaxIntakeSpeed);
    }

    public void stop() {
        intakeMotor.stopMotor();
    }

    @Override public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);

        builder.addBooleanProperty("Has Note", () -> !irSensor.get(), null);

    }

}
