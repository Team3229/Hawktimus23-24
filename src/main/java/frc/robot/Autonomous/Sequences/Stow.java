package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Stow extends Command {
    
    private static SequentialCommandGroup sequence;

    @Override
    public void initialize() {
        sequence.addCommands(

            

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
        return sequence.isFinished();
    }

}