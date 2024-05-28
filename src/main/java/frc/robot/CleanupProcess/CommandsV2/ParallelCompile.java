package frc.robot.CleanupProcess.CommandsV2;

import java.util.ArrayList;

/**
 * This file is a command that runs all commands at the same time
 * @see Command
 */
public class ParallelCompile extends Command{

    /**
     *  The name of a command, 
     */
    private String commandID = "ERROR";

    /**
     * Gathering the commands you put in, a command "deletes" after you run it
     */
    private ArrayList<Command> commands;

    /**
     * A second copy of the list, keeps the commands stored for reseting after the parallel compile is complete
     */
    private ArrayList<Command> _commands;

    /**
     * The basis of a Parallel Compile, created each time you create a Parallel Compile, commands are replaced with the command you want to run
     */
    public ParallelCompile(String commandID, Command... commands){
        this.commandID = commandID;
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
                commands.get(i).end();
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
        commands.clear();
        commands.addAll(_commands);
    }

    @Override
    public String getID() {return commandID;}
}
