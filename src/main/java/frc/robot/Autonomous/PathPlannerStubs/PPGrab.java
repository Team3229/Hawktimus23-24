package frc.robot.Autonomous.PathPlannerStubs;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Autonomous.Sequences.Grab;
import frc.robot.CommandsV2.CommandScheduler;

public class PPGrab{

    public static Command command = new Command() {
        @Override
        public void initialize() {
            CommandScheduler.activate(Grab.command());
        }

        @Override
        public boolean isFinished() {
            return !CommandScheduler.isActive(Grab.command());
        }
    };

}
