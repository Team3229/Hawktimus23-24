package frc.robot.Autonomous.PathPlannerStubs;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Autonomous.Sequences.Speaker;
import frc.robot.CommandsV2.CommandScheduler;

public class PPSpeaker {

    public static Command command = new Command() {
        @Override
        public void initialize() {
            CommandScheduler.activate(Speaker.command());
        }

        @Override
        public boolean isFinished() {
            return !CommandScheduler.isActive(Speaker.command());
        }
    };

}
