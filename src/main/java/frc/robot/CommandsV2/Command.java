package frc.robot.CommandsV2;

public class Command extends edu.wpi.first.wpilibj2.command.Command{
    
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
    }

}
