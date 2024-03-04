package frc.robot.Utils;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;

public class RunCommand {
    
    private static List<Command> commandList;
    public static boolean manualOverride = false;

    public static void init() {
        commandList = new ArrayList<Command>();
    }

    /**
     * Periodic function of {@link #RunCommand}; needs to be run every 20ms
     */
    public static void execute() {

        for (int i = 0; i < commandList.size(); i++) {
            
            Command command = commandList.get(i);
            command.execute();
            if (command.isFinished()) {
                command.end(false);
                commandList.remove(i);
            }
        }

    }

    /**
     * Will only schedule if not already scheduled.
     * @param command Command to run
     */
    public static void run(Command command) {
        // if (!isActive(command)) {
            commandList.add(command);
            command.initialize();
        // }
    }

    /**
     * Stops the command given and removes the schedule
     * @param command Command to interrupt
     */
    public static void interrupt(Command command) {
        commandList.get(commandList.indexOf(command)).end(true);
        commandList.remove(commandList.indexOf(command));
    }

    /**
     * Returns a boolean on whether or not the provided command is currently scheduled
     * @param command
     * @return true if command is running
     */
    public static boolean isActive(Command command) {
        return /*commandList.contains(command)*/false;
    }

}