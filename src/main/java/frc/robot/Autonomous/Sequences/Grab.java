package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Utils.ParallelGroup;
import frc.robot.Utils.SequentialGroup;
/*
Grabbing note
-unstow
-Slow speed intake
-Once grabbed, stow
 */
public class Grab {
    
    private static SequentialGroup sequence;

    public static Command command = new Command() {
        @Override
        public void initialize() {
            sequence = new SequentialGroup(
                new ParallelGroup(
                    ArmCommands.raise,
                    ArmCommands.forwardRail
                ),
                ArmCommands.intakePos,
                IntakeCommands.intakeNote,
                new ParallelGroup(
                    ArmCommands.raise,
                    ArmCommands.backwardRail
                ),
                ArmCommands.stow           
            );
            sequence.initialize();
        }

        @Override
        public void execute() {
            sequence.execute();
        }

        @Override
        public void end(boolean interrupted) {}

        @Override
        public boolean isFinished() {
            return sequence.isFinished();
        }

    };

}