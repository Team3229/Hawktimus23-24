package frc.robot.Autonomous.PathPlannerStubs;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.CommandsV2.CommandScheduler;
import frc.robot.Subsystems.Intake.IntakeCommands;

public class PPShoot{

    public static Command command = new Command() {
        @Override
        public void initialize() {
            CommandScheduler.activate(IntakeCommands.feed);
        }

        @Override
        public boolean isFinished() {
            return !CommandScheduler.isActive(IntakeCommands.feed);
        }
    };

}
