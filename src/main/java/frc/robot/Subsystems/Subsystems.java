package frc.robot.Subsystems;

import frc.robot.Subsystems.Arm.Angular;
import frc.robot.Subsystems.Arm.Linear;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.LEDs.LEDs;
import frc.robot.Subsystems.Shooter.Shooter;

public class Subsystems {
    
    public static void init(){
        Intake.init();
		Shooter.init();
		Angular.init();
		Linear.init();
        LEDs.init();
    }

    public static void update(){
        Linear.update();
		Intake.update();
		Shooter.update();
        Angular.update();
    }

}
