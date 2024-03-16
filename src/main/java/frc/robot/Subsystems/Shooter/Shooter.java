package frc.robot.Subsystems.Shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        SmartDashboard.putNumber("targetRPM", 0);
        shooter = new CANSparkMax(SHOOTER_ID, MotorType.kBrushless);
        shooter.setInverted(true);
        pid = shooter.getPIDController();
        pid.setP(0.001);
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

        // if (Intake.hasNote) {
            double distance = SwerveOdometry.getPose().getTranslation().getDistance(FieldConstants.BLUE_SPEAKER_P);

        //     pid.setReference(distance * 1000, ControlType.kVelocity);
        // } else {
        //     stop();
        // }

        SmartDashboard.putNumber("distance", distance);

       if(Intake.hasNote){
            pid.setReference(SmartDashboard.getNumber("targetRPM", 0),ControlType.kVelocity);
            atSpeed = Math.abs(SmartDashboard.getNumber("targetRPM", 0) - encoder.getVelocity()) <= RPM_DEADBAND;
       } else {
           stop();
       }
    }

    public static void spinUp(double speed){
        targetSpeed = speed;
        state = ShooterStates.spinningUp;
    }
    
    public static void spinUp(){
        state = ShooterStates.spinningUp;
    }

    public static void stop(){
        targetSpeed = 0;
        state = ShooterStates.idle;
        shooter.stopMotor();
    }

    public static void toggleAmpIntent(){
        ampIntent = !ampIntent;
    }
}
