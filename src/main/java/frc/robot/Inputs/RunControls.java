package frc.robot.Inputs;

import frc.robot.Autonomous.Sequences.Grab;
import frc.robot.Inputs.Controller.ControllerType;
import frc.robot.Inputs.Controller.Controls;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
import frc.robot.Utils.RunCommand;

public class RunControls {

    private static Controller flightStick;

    public static void init(){
        flightStick = new Controller(ControllerType.FlightStick, 0);
    }

    public static void nullControls(){
        flightStick.nullControls();
    }

    public static void run(){
        nullControls();
        update();
        RunCommand.periodic();

        runDriver();
        runManip();
        
    }
    
    private static void runDriver(){
        SwerveKinematics.maxChassisRotationSpeed = 10 * 0.5 * ((-(double) flightStick.get(Controls.FlightStick.Throttle)) + 1);
        SwerveKinematics.drive(
						(double) flightStick.get(Controls.FlightStick.AxisX),
						(double) flightStick.get(Controls.FlightStick.AxisY),
						(double) flightStick.get(Controls.FlightStick.AxisZ)
					);

		if((boolean) flightStick.get(Controls.FlightStick.Button10)){
			SwerveKinematics.navxGyro.zeroYaw();
		}
    }

    private static void runManip(){

        //Command example
        if ((boolean) flightStick.get(Controls.FlightStick.Button8)) {
            RunCommand.run(Grab.command);
        }

    }
    private static void update(){
        flightStick.update();
    }
}
