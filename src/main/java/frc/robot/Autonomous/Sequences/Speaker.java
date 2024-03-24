package frc.robot.Autonomous.Sequences;

import frc.robot.CommandsV2.Command;
import frc.robot.Subsystems.Arm.ArmCommands;

public class Speaker {
    public static Command command() {
        return ArmCommands.speakerPosition();
    }
}
