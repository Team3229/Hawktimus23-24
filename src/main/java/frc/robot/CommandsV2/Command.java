package frc.robot.CommandsV2;

public class Command extends edu.wpi.first.wpilibj2.command.Command{

    public static Command createFromWPILIB(edu.wpi.first.wpilibj2.command.Command command) {
        return new Command() {
            @Override
            public void init() {
                command.initialize();
            }

            @Override
            public void periodic() {
                command.execute();
            }

            @Override
            public boolean isDone() {
                return command.isFinished();
            }

            @Override
            public void end() {
                command.end(false);
            }

            @Override
            public String getID() {
                return command.getName();
            }
        };
    }
    
    public void init(){}

    public void periodic(){}

    public boolean isDone(){return false;}

    public void end(){}

    public String getID(){return "";}

    @Override
    public void initialize(){
        init();
    }

    @Override
    public void execute(){
        periodic();
    }

    @Override
    public boolean isFinished(){
        return isDone();
    }

    @Override
    public void end(boolean interrupted){
        end();
        if (interrupted) System.out.println("INTERRUPTED " + getID());
    }

}
