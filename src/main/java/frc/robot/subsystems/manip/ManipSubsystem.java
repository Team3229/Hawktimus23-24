package frc.robot.subsystems.manip;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ManipSubsystem extends SubsystemBase {

    private ArmSubsystem arm;
    private RailSubsystem rail;
    private IntakeSubsystem intake;
    private ShooterSubsystem shooter;

    private static final double kShooterSpeed = 3500;

    private static final double kShootAngle = 75;
    private static final double kStowAngle = 86;
    private static final double kIntakeAngle = 108;

    public ManipSubsystem(Supplier<Double> manualArm) {

        arm = new ArmSubsystem(manualArm);
        rail = new RailSubsystem();
        intake = new IntakeSubsystem();
        shooter = new ShooterSubsystem();

    }

    public Command homeRail() {
        return rail.homeRail();
    }

    public Command grabCommand() {
        
        Command out = new SequentialCommandGroup(
            new ParallelDeadlineGroup(
                rail.forwardRail(),
                arm.moveToAngle(Rotation2d.fromDegrees(kStowAngle))
            ),
            arm.moveToAngle(Rotation2d.fromDegrees(kIntakeAngle)),
            intake.grabNote(),
            arm.moveToAngle(Rotation2d.fromDegrees(kStowAngle)),
            rail.backwardRail()

        );

        return out;
    }

    public Command readyShooterCommand() {
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                arm.moveToAngle(Rotation2d.fromDegrees(kShootAngle)),
                shooter.spinShooterTo(kShooterSpeed)
            )
        );
    }

    public Command shootCommand() {
        return new SequentialCommandGroup(
            intake.feedNoteToShooter(),
            new ParallelCommandGroup(
                shooter.slowShooter(),
                arm.moveToAngle(Rotation2d.fromDegrees(kStowAngle))
            )
        );
    }

    public Command railBack() {
        return rail.backwardRail();
    }

    public Command railFront() {
        return rail.forwardRail();
    }

    public Command initialHoming() {
        return new SequentialCommandGroup(
            arm.moveToAngle(Rotation2d.fromDegrees(0)),
            rail.homeRail()  
        );
    }

    public void sendSubsystemTelemetry() {
        SmartDashboard.putData(rail);
        SmartDashboard.putData(arm);
        SmartDashboard.putData(intake);
        SmartDashboard.putData(shooter);
    }

    @Override public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        sendSubsystemTelemetry();
    }

}
