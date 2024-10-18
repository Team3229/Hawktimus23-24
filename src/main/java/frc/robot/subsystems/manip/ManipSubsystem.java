package frc.robot.subsystems.manip;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ManipSubsystem extends SubsystemBase {
    
    private ArmSubsystem arm;
    private RailSubsystem rail;
    private IntakeSubsystem intake;
    private ShooterSubsystem shooter;

    private static final Rotation2d kArmShootPos = Rotation2d.fromDegrees(50);
    private static final double kShooterSpeed = 2000;

    public ManipSubsystem() {

        arm = new ArmSubsystem();
        rail = new RailSubsystem();
        intake = new IntakeSubsystem();
        shooter = new ShooterSubsystem();

    }

    public Command homeRail() {
        return rail.homeRail();
    }

    public Command grabCommand() {
        return new SequentialCommandGroup(
            new ParallelDeadlineGroup(
                rail.forwardRail(),
                arm.moveToAngle(Rotation2d.fromDegrees(70))
            ),
            arm.moveToAngle(Rotation2d.fromDegrees(90)),
            intake.grabNote(),
            arm.moveToAngle(Rotation2d.fromDegrees(70)),
            rail.backwardRail()
        );
    }

    public Command readyShooterCommand() {
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                arm.moveToAngle(kArmShootPos),
                shooter.spinShooterTo(kShooterSpeed)
            )
        );
    }

    public Command shootCommand() {
        return new SequentialCommandGroup(
            intake.feedNoteToShooter(),
            new ParallelCommandGroup(
                shooter.slowShooter(),
                arm.moveToAngle(Rotation2d.fromDegrees(70))
            )
        );
    }

    public Command railBack() {
        return rail.backwardRail();
    }

    public Command railFront() {
        return rail.forwardRail();
    }

    public void sendSubsystemTelemetry() {
        SmartDashboard.putData(rail);
        SmartDashboard.putData(arm);
        SmartDashboard.putData(intake);
        SmartDashboard.putData(shooter);
    }

    public void manualArmControl(double speed) {
        arm.manualArm(speed);
    }

    @Override public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        sendSubsystemTelemetry();
    }

}
