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
import frc.robot.subsystems.LEDSubsystem;

public class ManipSubsystem extends SubsystemBase {

    private ArmSubsystem arm;
    private RailSubsystem rail;
    private IntakeSubsystem intake;
    private ShooterSubsystem shooter;
    public LEDSubsystem leds;

    private static final double kShooterSpeed = 3500;

    private static final double kShootAngle = 75;
    private static final double kStowAngle = 86;
    private static final double kIntakeAngle = 108;

    public ManipSubsystem(Supplier<Double> manualArm) {

        arm = new ArmSubsystem(manualArm);
        rail = new RailSubsystem();
        intake = new IntakeSubsystem();
        shooter = new ShooterSubsystem();
        leds = new LEDSubsystem();

        intake.note.whileTrue(leds.hasNote());

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

        ).withName("Grabbing...");

        return out;
    }

    public Command readyShooterCommand() {
        return new SequentialCommandGroup(
            new ParallelCommandGroup(
                rail.backwardRail(),
                arm.moveToAngle(Rotation2d.fromDegrees(kShootAngle)),
                shooter.spinShooterTo(kShooterSpeed)
            )
        ).withName("Spinning Up...");
    }

    public Command shootCommand() {
        return new SequentialCommandGroup(
            intake.feedNoteToShooter(shooter::isReady),
            new ParallelCommandGroup(
                shooter.slowShooter(),
                stowCommand()
            )
        ).withName("Shoot!");
    }

    public Command ejectNote() {
        return new Command() {
            @Override public void initialize() {
                intake.ejectNote();
                shooter.ejectNote();
            }
            @Override public void end(boolean interrupted) {
                intake.stop();
                shooter.slowShooter().initialize();
            }
        }.withName("Ejecting Note...");
    }

    public Command stowCommand() {
        return new SequentialCommandGroup(
            arm.moveToAngle(Rotation2d.fromDegrees(kStowAngle)),
            rail.backwardRail()
        ).withName("Stowing...");
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
        ).withName("Homing...");
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
