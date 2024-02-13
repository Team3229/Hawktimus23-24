package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/*
Shooting note
-automatically stop driver movement
    -After a short delay (.75seconds?)
-Rotate bot, angle arm to needed locations
-Spin up Outtake
-nudge note into shooter w/ intake
-reset everything, stow
-Return driver control
 */
public class ScoreSpeaker extends Command {
    
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