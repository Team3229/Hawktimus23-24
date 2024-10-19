package frc.robot.subsystems.manip;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {

    private CANSparkMax m_motor;
    private RelativeEncoder m_encoder;
    private SparkPIDController m_pidController;

    private final double kFreeSpeed = 4700;
    private final double kVelTolerance = 100;


    private double currentSetpoint = 0;

    public ShooterSubsystem() {

        m_motor = new CANSparkMax(9, MotorType.kBrushless);

        m_motor.restoreFactoryDefaults(true);

        m_motor.setInverted(true);
        m_encoder = m_motor.getEncoder();
        m_pidController = m_motor.getPIDController();

        m_pidController.setP(0.0004);
        m_pidController.setI(0);
        m_pidController.setD(0);
        m_pidController.setFF(1 / kFreeSpeed);

        m_pidController.setOutputRange(0, 1);

    }

    public Command spinShooterTo(double velocity) {
        Command out = new Command() {
            @Override public void initialize() {
                m_pidController.setReference(velocity, ControlType.kVelocity);
                currentSetpoint = velocity;
            }
            @Override public boolean isFinished() {
                return isReady();
            }
        };

        out.addRequirements(this);

        return out;
    }

    public Command slowShooter() {
        return Commands.runOnce(() -> {
            m_pidController.setReference(0, ControlType.kDutyCycle);
        }, this);
    }

    public void ejectNote() {
        m_pidController.setReference(-0.3, ControlType.kDutyCycle);
    }

    private boolean isReady() {
        return Math.abs(currentSetpoint - m_encoder.getVelocity()) < kVelTolerance;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);

        builder.addDoubleProperty("Shooter RPM", m_encoder::getVelocity, null);
        builder.addBooleanProperty("Ready to Shoot", this::isReady, null);

    }


    
}
