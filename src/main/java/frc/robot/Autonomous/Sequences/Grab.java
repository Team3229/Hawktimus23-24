package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Utils.RunCommand;
/*
Grabbing note
-unstow
-Slow speed intake
-Once grabbed, stow
 */
public class Grab {
    
    private static SequentialCommandGroup sequence;

    public static Command command = new Command() {
        @Override
        public void initialize() {
            sequence.addCommands(
                ArmCommands.raise,
                ArmCommands.forwardRail,
                ArmCommands.intakePos,
                IntakeCommands.intakeNote,
                ArmCommands.raise,
                ArmCommands.backwardRail,
                ArmCommands.stow           
            );
            sequence.initialize();
        }

        @Override
        public void execute() {
            sequence.execute();
        }

        @Override
        public void end(boolean interrupted) {
            sequence.end(interrupted);
        }

        @Override
        public boolean isFinished() {
            return sequence.isFinished() | RunCommand.manualOverride;
        }

    };

}