package frc.robot.Utils;

import edu.wpi.first.wpilibj2.command.Command;

public class SequentialGroup {
    
    private Command[] commands;
    private int index = 0;

    public SequentialGroup(Command... commands){
        this.commands = commands;
    }

    public void initialize(){
        commands[index].initialize();
    }

    public void execute(){
        commands[index].execute();
        if(commands[index].isFinished()){
            commands[index].end(false);
            index++;
            if(index < commands.length){
                commands[index].initialize();
            }
        }
    }

    public boolean isFinished() {
        return index == commands.length;
    }

}
