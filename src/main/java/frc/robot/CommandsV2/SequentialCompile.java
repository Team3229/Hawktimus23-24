package frc.robot.CommandsV2;

public class SequentialCompile extends Command{
    
    private Command[] commands;
    private int index = 0;
    private String commandID = "ERROR";

    public SequentialCompile(String commandID, Command... commands){
        this.commandID = commandID;
        this.commands = commands;
    }

    @Override
    public void init(){
        commands[index].init();
    }

    @Override
    public void periodic(){
        commands[index].periodic();
        if(commands[index].isDone()){
            commands[index].end();
            index++;
            if(index < commands.length){
                commands[index].init();
            }
        }
    }

    @Override
    public void end(){
        index = 0;
    }

    @Override
    public boolean isDone() {
        return index == commands.length;
    }

    @Override
    public String getID() {
        return commandID;
    }

}
