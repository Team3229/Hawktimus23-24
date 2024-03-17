package frc.robot.Inputs;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Autonomous.Sequences.CancelGrab;
import frc.robot.Autonomous.Sequences.Grab;
import frc.robot.Autonomous.Sequences.Stow;
import frc.robot.CommandsV2.CommandScheduler;
import frc.robot.Inputs.Controller.ControllerType;
import frc.robot.Inputs.Controller.Controls;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.Linear;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
import frc.robot.Subsystems.Drivetrain.SwerveOdometry;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Intake.Intake.IntakeStates;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Subsystems.Shooter.Shooter.ShooterStates;
import frc.robot.Utils.FieldConstants;
import frc.robot.Utils.Utils;

public class RunControls {

    private static Controller driveStick;
    private static Controller manipStick;

    public static boolean manipManualControl = true;

    public static void init(){
        driveStick = new Controller(ControllerType.FlightStick, 0);
        manipStick = new Controller(ControllerType.FlightStick, 1);
    }

    public static void nullControls(){
        driveStick.nullControls();
        manipStick.nullControls();
    }

    public static void run(){
        nullControls();
        update();

        runDriver();
        runManip();
        
    }
    
    private static void runDriver(){

        if((boolean) driveStick.get(Controls.FlightStick.Trigger)){
            SwerveKinematics.relativeMode = true;
        } else {
            SwerveKinematics.relativeMode = false;
        }
    
        if ((boolean) driveStick.get(Controls.FlightStick.Hazard)) {
            if (Utils.getAlliance() == Alliance.Blue) {
                SwerveKinematics.driveWithRotation(
						(double) driveStick.get(Controls.FlightStick.AxisX),
						(double) driveStick.get(Controls.FlightStick.AxisY),
						new Rotation2d(
                            FieldConstants.BLUE_SPEAKER[0] - SwerveOdometry.getPose().getX(),
                            FieldConstants.BLUE_SPEAKER[1] - SwerveOdometry.getPose().getY())
					);
            } else {
                SwerveKinematics.driveWithRotation(
						(double) driveStick.get(Controls.FlightStick.AxisX),
						(double) driveStick.get(Controls.FlightStick.AxisY),
						new Rotation2d(
                            FieldConstants.RED_SPEAKER[0] - SwerveOdometry.getPose().getX(),
                            FieldConstants.RED_SPEAKER[1] - SwerveOdometry.getPose().getY())
					);
            }
        } else {
            SwerveKinematics.drive(
						(double) driveStick.get(Controls.FlightStick.AxisX),
						(double) driveStick.get(Controls.FlightStick.AxisY),
						(double) driveStick.get(Controls.FlightStick.AxisZ)
					);
        }

		if((boolean) driveStick.get(Controls.FlightStick.Button10Toggle)){
			SwerveKinematics.zeroGyro();
		}

    }

    private static void runManip(){
        if((boolean) manipStick.get(Controls.FlightStick.Button10Toggle)){
            manipManualControl = !manipManualControl;
            CommandScheduler.emptyTrashCan();
            if(!manipManualControl){
                Angular.manual = false;
            }
        }

        if(manipManualControl){
            
            Angular.runManual(-(double) manipStick.get(Controls.FlightStick.AxisY)/3);

            if((boolean) manipStick.get(Controls.FlightStick.Button7Toggle) & Linear.goingBackwards){
                Angular.amp();
            }
            else if((boolean) manipStick.get(Controls.FlightStick.Button6Toggle) & !Linear.goingBackwards){
                Angular.grab();
            }
            else if((boolean) manipStick.get(Controls.FlightStick.Button8Toggle) & Linear.goingBackwards){
                Angular.subwoofShoot();
            }
            else if((boolean) manipStick.get(Controls.FlightStick.Button9Toggle) & Linear.goingBackwards) {
                Angular.stow();
            }

            if((boolean) manipStick.get(Controls.FlightStick.HazardToggle)){
                if(Linear.goingBackwards){
                    Linear.front();
                } else {
                    Linear.back();
                }
            }

            if((boolean) manipStick.get(Controls.FlightStick.Button11)){
                Intake.run(-0.2);
            } else {
                Intake.stop();
            }
            
            if((boolean) manipStick.get(Controls.FlightStick.Button4Toggle)){
                if(Intake.state == IntakeStates.intaking){
                    Intake.stop();
                } else {
                    Intake.intake();
                }
            }

            if ((boolean) manipStick.get(Controls.FlightStick.Button12)) {
                Linear.linearRail.setIdleMode(IdleMode.kCoast);
                Linear.pid.setReference(0, ControlType.kDutyCycle);
            } else {
                Linear.linearRail.setIdleMode(IdleMode.kBrake);
            }

        } else {
            //Auto control scheme
            
            if((boolean) manipStick.get(Controls.FlightStick.Button8Toggle)){
                Shooter.ampIntent = !Shooter.ampIntent;
            }

            if((boolean) manipStick.get(Controls.FlightStick.Button7Toggle)){
                Intake.eject();
            }

            if((boolean) manipStick.get(Controls.FlightStick.Button4Toggle)){
                if(!CommandScheduler.isActive(Grab.command)){
                    CommandScheduler.activate(Grab.command);
                } else {
                    CommandScheduler.deactivate(Grab.command);
                    CommandScheduler.activate(CancelGrab.command);
                }
            }

            if(!Intake.hasNote & !CommandScheduler.isActive(Grab.command)) {
                CommandScheduler.activate(Stow.command);
            }

            if((boolean) manipStick.get(Controls.FlightStick.TriggerToggle)){
                if (Angular.targetAngle == Angular.AMP_ANGLE) {
                    if(Shooter.state == ShooterStates.spinningUp){
                        Shooter.stop();
                    } else {
                        Shooter.feed();
                    }
                }
                if(Intake.state == IntakeStates.feed){
                    Intake.stop();
                } else {
                    Intake.feed();
                }
            }

            if ((boolean) manipStick.get(Controls.FlightStick.Button9Toggle)) {
                Angular.amp();
            }
            
        }

    }
    private static void update(){
        driveStick.update();
        manipStick.update();
    }
}
