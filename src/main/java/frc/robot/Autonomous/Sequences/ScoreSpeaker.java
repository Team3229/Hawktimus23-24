package frc.robot.Autonomous.Sequences;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Arm.ArmCommands;
import frc.robot.Subsystems.Drivetrain.DrivetrainCommands;
import frc.robot.Subsystems.Intake.IntakeCommands;
import frc.robot.Subsystems.Shooter.ShooterCommands;
import frc.robot.Utils.RunCommand;

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

            new ParallelCommandGroup(
                ShooterCommands.shootSpeaker(/*put output of RPMs equation etc. */0),
                ArmCommands.speakerPosition(/*put output of distance equation etc. */0),
                DrivetrainCommands.lineUpSpeaker(/*desired rotation goes here*/null)
            ),
            IntakeCommands.feed,
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

}