package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IDConstants;

public class ArmSubsystem extends SubsystemBase {
    
    private CANSparkMax m_motor;
    private CANSparkMax m_motorFollower;

    private RelativeEncoder m_encoder;

    private SparkPIDController m_pidController;

    public ArmSubsystem() {

        m_motor = new CANSparkMax(IDConstants.ARM_MOTOR1, MotorType.kBrushless);
        m_motorFollower = new CANSparkMax(IDConstants.ARM_MOTOR2, MotorType.kBrushless);

        m_motor.restoreFactoryDefaults(true);
        m_motorFollower.restoreFactoryDefaults(true);

        m_encoder = m_motor.getAlternateEncoder(8192);
        m_pidController = m_motor.getPIDController();

        m_encoder.setPositionConversionFactor(360);
        // m_pidController.setFeedbackDevice(m_encoder);

        m_encoder.setPosition(0);

        m_pidController.setP(0);
        m_pidController.setI(0);
        m_pidController.setD(0);

        m_motorFollower.follow(m_motor, true);

    }

    public void moveArm(double speed) {
        m_pidController.setReference(speed/5, ControlType.kDutyCycle);
    }

    @Override
    public void initSendable(SendableBuilder builder) {

        super.initSendable(builder);

        builder.addDoubleProperty("Arm Angle", m_encoder::getPosition, null);

    }

}
