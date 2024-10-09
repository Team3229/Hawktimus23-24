package frc.robot.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.RailSubsystem;

public class GrabNote extends SequentialCommandGroup {

    public GrabNote(RailSubsystem rail) {

        addCommands(
            
            rail.forwardRail(),
            rail.backwardRail()

        );

    }
    
}
