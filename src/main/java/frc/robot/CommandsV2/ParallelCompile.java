package frc.robot.CommandsV2;

import java.util.ArrayList;

public class ParallelCompile extends Command{

    private ArrayList<Command> _commands;
    private ArrayList<Command> commands;

    public ParallelCompile(Command... commands){
        this.commands = new ArrayList<Command>();
        this._commands = new ArrayList<Command>();
        for(int i = 0; i < commands.length; i++){
            this.commands.add(commands[i]);
            this._commands.add(commands[i]);
        }
    }

    @Override
    public void init(){
        for(int i = 0; i < commands.size(); i++) {
            commands.get(i).init();
        }
    }

    @Override
    public void periodic(){
        for(int i = 0; i < commands.size(); i++) {
            commands.get(i).periodic();
            if(commands.get(i).isDone()){
                commands.remove(i);
            }
        }
    }

    @Override
    public boolean isDone() {
        return commands.size() == 0;
    }

    @Override
    public void end(){
        this.commands = this._commands;
    }
}
