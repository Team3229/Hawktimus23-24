package frc.robot.Utils;

import edu.wpi.first.wpilibj2.command.Command;

public class ParallelCommandGroup extends Command {

    private Command[] commands;
    private boolean done = false;

    public ParallelCommandGroup(Command... commands){
        this.commands = commands;
    }

    @Override
    public void initialize(){
        for(int i = 0; i < commands.length; i++) {
            commands[i].initialize();
        }
    }

    @Override
    public void execute(){
        done = true;
        for(int i = 0; i < commands.length; i++) {
            commands[i].execute();
            if(!commands[i].isFinished()){
                done = false;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public void end(boolean interrupted){}
}
