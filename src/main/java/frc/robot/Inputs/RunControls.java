package frc.robot.Inputs;

import frc.robot.Drivetrain.SwerveKinematics;
import frc.robot.Inputs.Controller.ControllerType;
import frc.robot.Inputs.Controller.Controls;

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

    }
    private static void update(){
        flightStick.update();
    }
}
