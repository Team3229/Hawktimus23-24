package frc.robot.Inputs;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Autonomous.Sequences.CancelGrab;
import frc.robot.Autonomous.Sequences.Grab;
import frc.robot.CommandsV2.CommandScheduler;
import frc.robot.CommandsV2.SequentialCompile;
import frc.robot.Inputs.Controller.ControllerType;
import frc.robot.Inputs.Controller.Controls;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.ArmCommands;
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

    private static boolean wasShooting = false;

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

        //Those commands take driving control, only allow the override to be ran.  Everything else is fully auto.
        if((boolean) driveStick.get(Controls.FlightStick.Button5Toggle) | (boolean) manipStick.get(Controls.FlightStick.Button5Toggle)) CommandScheduler.terminated = !CommandScheduler.terminated;

        runDriver();
        runManip();
        
    }
    
    private static void runDriver(){

        SwerveKinematics.maxChassisRotationSpeed = 10 * (double) driveStick.get(Controls.FlightStick.Throttle);

        if((boolean) driveStick.get(Controls.FlightStick.Button4) | (boolean) driveStick.get(Controls.FlightStick.Trigger)){
            SwerveKinematics.relativeMode = true;
        } else {
            SwerveKinematics.relativeMode = false;
        }
    
        if ((boolean) driveStick.get(Controls.FlightStick.Hazard)) {
            //Drive w/ Rotation towards speaker.
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
        } else if ((boolean) driveStick.get(Controls.FlightStick.Trigger)) {
            //Drive w/ Rotation towards source.
            if (Utils.getAlliance() == Alliance.Blue) {
                SwerveKinematics.driveWithRotation(
						(double) driveStick.get(Controls.FlightStick.AxisX),
						(double) driveStick.get(Controls.FlightStick.AxisY),
						new Rotation2d(
                            0.571 - SwerveOdometry.getPose().getX(),
                            0.621 - SwerveOdometry.getPose().getY())
					);
            } else {
                SwerveKinematics.driveWithRotation(
						(double) driveStick.get(Controls.FlightStick.AxisX),
						(double) driveStick.get(Controls.FlightStick.AxisY),
						new Rotation2d(
                            15.967 - SwerveOdometry.getPose().getX(),
                            0.620 - SwerveOdometry.getPose().getY())
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
        // if((boolean) manipStick.get(Controls.FlightStick.Button10Toggle)){
        //     manipManualControl = !manipManualControl;
        //     CommandScheduler.emptyTrashCan();
        //     if(!manipManualControl){
        //         Angular.manual = false;
        //     }
        // }

        if(manipManualControl){
            
            Angular.runManual(-(double) manipStick.get(Controls.FlightStick.AxisY)/3);

            if((boolean) manipStick.get(Controls.FlightStick.Button9Toggle) & Linear.goingBackwards) {
                Angular.stow();
            }

            if((boolean) manipStick.get(Controls.FlightStick.HazardToggle)){
                if(Linear.goingBackwards){
                    Linear.front();
                } else {
                    Linear.back();
                }
            }

            if((boolean) manipStick.get(Controls.FlightStick.Button3Toggle)){
                if(Shooter.state == ShooterStates.spinningUp | !Intake.hasNote){
                    Shooter.stop();
                    Angular.stow();
                    wasShooting = false;
                } else {
                    Shooter.spinUp();
                    wasShooting = true;
                    Angular.subwoofShoot();
                }
            }

            if(!Intake.hasNote){
                Angular.isShooting = false;
            }

            if ((wasShooting == true & Angular.isShooting == false) | (boolean) manipStick.get(Controls.FlightStick.Button6Toggle) | (Angular.targetAngle == Angular.AMP_ANGLE & !Intake.hasNote)) {
                //stow
                CommandScheduler.activate(
                    new SequentialCompile(
                        "Stow1",
                        ArmCommands.raise,
                        ArmCommands.backwardRail,
                        ArmCommands.stow
                    )
                );
            }

            if((boolean) manipStick.get(Controls.FlightStick.Button4Toggle)){
                if(!CommandScheduler.isActive(Grab.command)){
                    CommandScheduler.deactivate(CancelGrab.command);
                    CommandScheduler.activate(Grab.command);
                } else {
                    CommandScheduler.deactivate(Grab.command);
                    CommandScheduler.activate(CancelGrab.command);
                }
            }

            if((boolean) manipStick.get(Controls.FlightStick.TriggerToggle)){
                if(Intake.state == IntakeStates.feed){
                    Intake.stop();
                } else {
                    Intake.feed();
                }
            }

        } else {
            //Auto control scheme

            if((boolean) manipStick.get(Controls.FlightStick.Button4Toggle)){
                if(!CommandScheduler.isActive(Grab.command)){
                    CommandScheduler.deactivate(CancelGrab.command);
                    CommandScheduler.activate(Grab.command);
                } else {
                    CommandScheduler.deactivate(Grab.command);
                    CommandScheduler.activate(CancelGrab.command);
                }
            }
            
            if((boolean) manipStick.get(Controls.FlightStick.Button3Toggle)){
                if(Shooter.state == ShooterStates.spinningUp | !Intake.hasNote){
                    Shooter.stop();
                    Angular.isShooting = false;
                } else {
                    Shooter.spinUp();
                    Angular.isShooting = true;
                }
            }

            if (wasShooting == true & Angular.isShooting == false) {
                //stow
                CommandScheduler.activate(
                    new SequentialCompile(
                        "Stow1",
                        ArmCommands.raise,
                        ArmCommands.backwardRail,
                        ArmCommands.stow
                    )
                );
            }

            if ((boolean) manipStick.get(Controls.FlightStick.TriggerToggle)){
                if(Intake.state == IntakeStates.feed){
                    Intake.stop();
                } else {
                    Intake.feed();
                }
            }

            if ((boolean) manipStick.get(Controls.FlightStick.Trigger) & Angular.targetAngle == Angular.AMP_ANGLE) {
                Intake.feed();
                Shooter.feed();
            } else if (Angular.targetAngle == Angular.AMP_ANGLE) {
                Intake.stop();
                Shooter.stop();
            }

            if (Angular.targetAngle == Angular.AMP_ANGLE & !Intake.hasNote) {
                CommandScheduler.activate(
                    new SequentialCompile(
                        "Stow2",
                        ArmCommands.raise,
                        ArmCommands.backwardRail,
                        ArmCommands.stow
                    )
                );
            }

            if ((boolean) manipStick.get(Controls.FlightStick.Button9Toggle)) {
                Angular.amp();
            }

            if ((boolean) manipStick.get(Controls.FlightStick.Button6Toggle)) {
                CommandScheduler.activate(
                    new SequentialCompile(
                        "Stow2",
                        ArmCommands.raise,
                        ArmCommands.backwardRail,
                        ArmCommands.stow
                    )
                );
            }

            if ((boolean) manipStick.get(Controls.FlightStick.Button7)) {
                Intake.forceeject();
                Shooter.feed();
            } else {
                if (Shooter.targetSpeed == Shooter.AMP_SPEED & !Intake.hasNote) {
                    Shooter.stop();
                }
                if (Intake.state == IntakeStates.forceeject) {
                    Intake.stop();
                }
            }
        }

        wasShooting = Angular.isShooting;

    }
    private static void update(){
        driveStick.update();
        manipStick.update();
    }
}
