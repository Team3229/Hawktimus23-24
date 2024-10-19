package frc.robot.subsystems.manip;

import java.util.function.Supplier;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IDConstants;
import frc.robot.constants.PIDConstants;

public class ArmSubsystem extends SubsystemBase {
    
    private CANSparkMax m_motor;
    private CANSparkMax m_motorFollower;

    private AbsoluteEncoder m_encoder;
    private SparkPIDController m_pidController;

    private static final double kAllowedError = 5;

    private Double currentSetpoint;

    public ArmSubsystem(Supplier<Double> manualArm) {

        m_motor = new CANSparkMax(IDConstants.ARM_M1, MotorType.kBrushless);
        m_motorFollower = new CANSparkMax(IDConstants.ARM_M2, MotorType.kBrushless);

        m_motorFollower.follow(m_motor, true);

        m_encoder = m_motor.getAbsoluteEncoder();
        m_pidController = m_motor.getPIDController();

        m_encoder.setPositionConversionFactor(360);

        m_pidController.setP(PIDConstants.P_ARM);
        m_pidController.setI(PIDConstants.I_ARM);
        m_pidController.setD(PIDConstants.D_ARM);
        m_pidController.setFF(PIDConstants.FF_ARM);


        m_pidController.setFeedbackDevice(m_encoder);

        m_pidController.setReference(0, ControlType.kDutyCycle);

        m_motor.setIdleMode(IdleMode.kBrake);
        m_motorFollower.setIdleMode(IdleMode.kBrake);

        this.setDefaultCommand(manualArm(manualArm));

    }

    public Command moveToAngle(Rotation2d rot) {

        Command out = new Command() {

            @Override public void initialize() {
                m_pidController.setReference(rot.getDegrees(), ControlType.kPosition);
                currentSetpoint = rot.getDegrees();
            }
            
            @Override public boolean isFinished() {
                return Math.abs(rot.getDegrees() - m_encoder.getPosition()) < kAllowedError;
            }

        };

        out.addRequirements(this);
        return out;
    }

    public Command manualArm(Supplier<Double> speed) {
        Command out = new Command() {
            @Override public void execute() {
                if (currentSetpoint == null && speed.get() == 0) {
                    currentSetpoint = m_encoder.getPosition();
                } else if (speed.get() != 0) {
                    currentSetpoint = null;
                    m_pidController.setReference(speed.get() * 0.2, ControlType.kDutyCycle);
                } else if (currentSetpoint != null) {
                    m_pidController.setReference(currentSetpoint, ControlType.kPosition);
                }
            }

            @Override public void end(boolean interrupted) {
                if (interrupted) currentSetpoint = null;
            }
        };

        out.addRequirements(this);

        return out;
    }

    @Override public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
    
        builder.addDoubleProperty("ArmA", () -> m_encoder.getPosition(), null);
    }


}
