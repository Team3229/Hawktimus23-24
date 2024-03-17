package frc.robot.Subsystems.Shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Utils.FieldConstants;

/*
-Spin up (to given RPM)
-Request intake to send note forward
 */
public class Shooter {

    private static CANSparkMax shooter;
    private static final int SHOOTER_ID = 9;
    public static final double AMP_SPEED = .1;
    public static ShooterStates state = ShooterStates.idle;
    public static double targetSpeed = 0;
    public static boolean ampIntent = false;
    public static boolean atSpeed = false;
    public static final double RPM_DEADBAND = 10;

    public static SparkPIDController pid;
    public static RelativeEncoder encoder;
    public static enum ShooterStates {
        idle,
        spinningUp
    }

    public static void init(){
        shooter = new CANSparkMax(SHOOTER_ID, MotorType.kBrushless);
        shooter.setInverted(true);
        pid = shooter.getPIDController();
        pid.setP(0.004);
        pid.setI(0.0004);
        pid.setOutputRange(0.3, 1);
        encoder = shooter.getEncoder();
    }

    public static void update(){
        switch(state){
            case idle:
                stop();
                break;
            case spinningUp:
                spinningUp();
                break;
        }
    }

    private static void spinningUp(){

        if (Intake.hasNote) {
            double distance = SwerveOdometry.getPose().getTranslation().getDistance(FieldConstants.BLUE_SPEAKER_P);
            distance *= 39.3701;

            targetSpeed = (0.0785384 * (Math.pow(distance, 2))) + (-1.35582 * distance) + 3800;
            targetSpeed = (targetSpeed > 5200) ? 5200 : targetSpeed;
            pid.setReference(targetSpeed, ControlType.kVelocity);
        } else {
            stop();
        }
    }

    public static void run(double speed) {
        pid.setReference(speed, ControlType.kDutyCycle);
    }

    public static void spinUp(double speed){
        targetSpeed = speed;
        state = ShooterStates.spinningUp;
    }
    
    public static void spinUp(){
        state = ShooterStates.spinningUp;
    }

    public static void feed(){
        targetSpeed = 0.5;
        state = ShooterStates.spinningUp;
    }

    public static void stop(){
        targetSpeed = 0;
        state = ShooterStates.idle;
        pid.setReference(0, ControlType.kDutyCycle);
    }

    public static void toggleAmpIntent(){
        ampIntent = !ampIntent;
    }
}
