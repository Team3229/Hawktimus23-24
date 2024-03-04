package frc.robot.Subsystems.Arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Linear {

    private static CANSparkMax railMotor;

    public static RelativeEncoder encoder;

    private static SparkPIDController pidController;

    public static void init() {
        railMotor = new CANSparkMax(7, MotorType.kBrushless);

        encoder = railMotor.getAlternateEncoder(8192);
        encoder.setPositionConversionFactor(1/2.0968017578125);
        encoder.setInverted(true);

        encoder.setPosition(1);

        pidController = railMotor.getPIDController();
        pidController.setFeedbackDevice(encoder);
        pidController.setP(1.5);
        pidController.setOutputRange(-1, 1);
    }

    public static void runRail(double position) {
        // railMotor.set(speed);
        pidController.setReference(position, ControlType.kPosition);
        // pidController.setReference(position, ControlType.kDutyCycle);
        SmartDashboard.putNumber("railInput", position);
        SmartDashboard.putNumber("railOutput", getPos());
    }

    public static double getPos() {
        return encoder.getPosition();
    }
    
}
