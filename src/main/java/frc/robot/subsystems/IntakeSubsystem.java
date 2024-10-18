package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.constants.IDConstants;

public class IntakeSubsystem extends SubsystemBase {
    
    private CANSparkMax intakeMotor;
    private DigitalInput irSensor;

    public IntakeSubsystem() {

        intakeMotor = new CANSparkMax(IDConstants.INTAKE_MOTOR, MotorType.kBrushless);
        irSensor = new DigitalInput(0);

    }

    public Command grabNote() {
        Command out = new Command() {
            
            @Override public void initialize() {
                if (!isFinished()) intakeMotor.set(0.5);
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

}
