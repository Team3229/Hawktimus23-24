package frc.robot.subsystems.manip;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IDConstants;
import frc.robot.constants.PIDConstants;

public class ArmSubsystem extends SubsystemBase {
    
    private CANSparkMax m_motor;
    private CANSparkMax m_motorFollower;

    private RelativeEncoder m_encoder;
    private SparkPIDController m_pidController;

    private static final double kAllowedError = 1;

    public ArmSubsystem() {

        m_motor = new CANSparkMax(IDConstants.ARM_M1, MotorType.kBrushless);
        m_motorFollower = new CANSparkMax(IDConstants.ARM_M2, MotorType.kBrushless);

        m_motorFollower.follow(m_motor);

        m_encoder = m_motor.getAlternateEncoder(8192);
        m_pidController = m_motor.getPIDController();

        m_encoder.setPositionConversionFactor(360);
        m_encoder.setPosition(0);

        m_pidController.setP(PIDConstants.P_ARM);
        m_pidController.setI(PIDConstants.I_ARM);
        m_pidController.setD(PIDConstants.D_ARM);

        m_pidController.setFeedbackDevice(m_encoder);

        m_motor.setIdleMode(IdleMode.kBrake);
        m_motorFollower.setIdleMode(IdleMode.kBrake);

    }

    public Command moveToAngle(Rotation2d rot) {

        Command out = new Command() {

            @Override public void initialize() {
                m_pidController.setReference(rot.getDegrees(), ControlType.kPosition);
            }

            @Override public boolean isFinished() {
                return Math.abs(rot.getDegrees() - m_encoder.getPosition()) < kAllowedError;
            }

        };

        out.addRequirements(this);
        return out;
    }


}
