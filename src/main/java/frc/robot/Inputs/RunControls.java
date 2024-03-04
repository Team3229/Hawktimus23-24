package frc.robot.Inputs;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Inputs.Controller.ControllerType;
import frc.robot.Inputs.Controller.Controls;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.Linear;
import frc.robot.Subsystems.Arm.Positions;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Shooter.Shooter;

public class RunControls {

    private static Controller drive;
    private static Controller manip;
    
    public static void init() {
        manip = new Controller(ControllerType.FlightStick, 1);

        drive = new Controller(ControllerType.FlightStick, 0);
    }

    public static void execute() {
        SwerveKinematics.maxChassisRotationSpeed = 20 * ((double) drive.get(Controls.FlightStick.Throttle)/2) + 0.5;

        SwerveKinematics.drive(
                (double) drive.get(Controls.FlightStick.AxisX),
                (double) drive.get(Controls.FlightStick.AxisY),
                (double) drive.get(Controls.FlightStick.AxisZ)
            );

        manip.update();
        drive.update();

        if((boolean) drive.get(Controls.FlightStick.Button10)){
            SwerveKinematics.navxGyro.zeroYaw();
        }


        if ((boolean) manip.get(Controls.FlightStick.Button9)) {
            Positions.intakePos();
        } else if ((boolean) manip.get(Controls.FlightStick.Button10)) {
            Positions.ampPos();
        } else if ((boolean) manip.get(Controls.FlightStick.Button7)) {
            Positions.shootPos();
        } else if ((boolean) manip.get(Controls.FlightStick.Button3)) {
            Positions.stowPos();
        } else {
            Angular.runArm(-(double) manip.get(Controls.FlightStick.AxisY)/3
            );
        }

        if ((boolean) manip.get(Controls.FlightStick.Hazard)) {
            Intake.runIntake();
        } else if ((boolean) manip.get(Controls.FlightStick.Trigger)) {
            Intake.fireNote();
        } else if ((boolean) manip.get(Controls.FlightStick.Button8)) {
            Intake.eject();
        } else {
            Intake.stopIntake();
        }

        if ((boolean) manip.get(Controls.FlightStick.Button4)) {
            Shooter.runShooter();
        } else { 
            Shooter.stopShooter();
        }

        // Linear.runRail((double) manip.get(Controls.FlightStick.Throttle));

        if ((boolean) manip.get(Controls.FlightStick.Button5) && !DriverStation.isFMSAttached()) {
            Linear.encoder.setPosition(0);
        }

        if ((boolean) manip.get(Controls.FlightStick.Button6) && !DriverStation.isFMSAttached()) {
            Angular.resetZero();
        }

        SmartDashboard.putNumber("armA", Angular.getPos());
    }

}