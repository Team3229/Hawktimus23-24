package frc.robot.Inputs;

import frc.robot.Autonomous.Sequences.Grab;
import frc.robot.Autonomous.Sequences.ScoreAmp;
import frc.robot.Autonomous.Sequences.ScoreSpeaker;
import frc.robot.CommandsV2.CommandScheduler;
import frc.robot.Inputs.Controller.ControllerType;
import frc.robot.Inputs.Controller.Controls;
import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.Linear;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Intake.Intake.IntakeStates;
import frc.robot.Subsystems.Shooter.Shooter;
import frc.robot.Subsystems.Shooter.Shooter.ShooterStates;

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

        //Those commands take driving control, only allow the override to be ran.  Everything else is fully auto.
        if((boolean) driveStick.get(Controls.FlightStick.Button5Toggle) | (boolean) manipStick.get(Controls.FlightStick.Button5Toggle)) CommandScheduler.terminated = !CommandScheduler.terminated;
        if(CommandScheduler.isActive(ScoreAmp.command) | CommandScheduler.isActive(ScoreSpeaker.command)) return;

        runDriver();
        runManip();
        
    }
    
    private static void runDriver(){

        if((boolean) driveStick.get(Controls.FlightStick.Hazard)){
            SwerveKinematics.relativeMode = true;
        } else {
            SwerveKinematics.relativeMode = false;
        }

                                                                                            //Conversion from 0-1 to 0.5-1
        SwerveKinematics.maxChassisRotationSpeed = 10 * ((double) driveStick.get(Controls.FlightStick.Throttle)/2) + 0.5;

        SwerveKinematics.drive(
						(double) driveStick.get(Controls.FlightStick.AxisX),
						(double) driveStick.get(Controls.FlightStick.AxisY),
						(double) driveStick.get(Controls.FlightStick.AxisZ)
					);

		if((boolean) driveStick.get(Controls.FlightStick.Button10Toggle)){
			SwerveKinematics.zeroGyro();
		}

    }

    private static void runManip(){
        if((boolean) manipStick.get(Controls.FlightStick.Button10Toggle)){
            manipManualControl = !manipManualControl;
            CommandScheduler.terminated = manipManualControl;
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

            if((boolean) manipStick.get(Controls.FlightStick.HazardToggle)){
                if(Linear.goingBackwards){
                    Linear.front();
                } else {
                    Linear.back();
                }
            }

            if((boolean) manipStick.get(Controls.FlightStick.TriggerToggle)){
                if(Intake.state == IntakeStates.feed){
                    Intake.stop();
                } else {
                    Intake.feed();
                }
            }

            if((boolean) manipStick.get(Controls.FlightStick.Button3Toggle)){
                if(Shooter.state == ShooterStates.spinningUp){
                    Shooter.stop();
                } else {
                    Shooter.spinUp(5000 * (((double) manipStick.get(Controls.FlightStick.Throttle)/2) + 0.5));
                }
            }

            if((boolean) manipStick.get(Controls.FlightStick.Button11Toggle)){
                Intake.eject();
            }
            
            if((boolean) manipStick.get(Controls.FlightStick.Button4Toggle)){
                if(Intake.state == IntakeStates.intaking){
                    Intake.stop();
                } else {
                    Intake.intake();
                }
            }

        } else {
            //Auto control scheme
            if((boolean) manipStick.get(Controls.FlightStick.Button7Toggle)){
                Intake.eject();
            }
            if((boolean) manipStick.get(Controls.FlightStick.Button11Toggle)){
                Shooter.ampIntent = !Shooter.ampIntent;
            }
            if((boolean) manipStick.get(Controls.FlightStick.TriggerToggle)){
                CommandScheduler.activate(Grab.command);
            }
        }

    }
    private static void update(){
        driveStick.update();
        manipStick.update();
    }
}
