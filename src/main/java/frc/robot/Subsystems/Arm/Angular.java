package frc.robot.Subsystems.Arm;

import com.revrobotics.CANSparkLowLevel.MotorType;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;

public class Angular {
    
    private static CANSparkMax left;
    private static CANSparkMax right;

    private static SparkPIDController pidController;

    private static RelativeEncoder encoder;

    public static void init() {
        left = new CANSparkMax(16, MotorType.kBrushless);
        right = new CANSparkMax(17, MotorType.kBrushless);

        encoder = left.getAlternateEncoder(8192);
        encoder.setPositionConversionFactor(360);

        encoder.setPosition(0);

        right.follow(left, true);

        pidController = left.getPIDController();

        pidController.setFeedbackDevice(encoder);
        pidController.setP(0.02);

        pidController.setOutputRange(-0.5, 0.5);
    }

    public static void runArm(double speed) {
        if (speed != 0) {
            pidController.setReference(speed, ControlType.kDutyCycle);
        }
    }

    public static void setPos(double pos) {
        pidController.setReference(pos, ControlType.kPosition);
    }

    public static void resetZero() {
        encoder.setPosition(0);
    }

    public static double getPos() {
        return encoder.getPosition();
    }

}
