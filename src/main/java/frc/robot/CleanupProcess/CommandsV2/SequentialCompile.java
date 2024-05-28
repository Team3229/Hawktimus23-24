package frc.robot.CleanupProcess.CommandsV2;

/**
*Compiles multiple commands sequentialy into one command to schedule
 */
public class SequentialCompile extends Command{
    
    private Command[] commands;
    private int index = 0;
    private String commandID = "ERROR";

/**
* @param commandID (String) The name of the Sequential Command
* @param commands (Command[]) Array of commands to be sequentialy ran in the order of the array
 */
    public SequentialCompile(String commandID, Command... commands){
        this.commandID = commandID;
        this.commands = commands;
    }

    @Override
    public void init(){
        if(index < commands.length){
            commands[index].init();
        }
    }

    @Override
    public void periodic(){
        if(index < commands.length){
            commands[index].periodic();
            if(commands[index].isDone()){
                commands[index].end();
                index++;
                if(index < commands.length){
                    commands[index].init();
                }
            }
        }
    }

    @Override
    public void end(){
        if(index < commands.length){
            commands[index].end();
        }
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
