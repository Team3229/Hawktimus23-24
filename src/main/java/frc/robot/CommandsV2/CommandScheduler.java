package frc.robot.CommandsV2;

import java.util.ArrayList;

public class CommandScheduler {
    
    private static ArrayList<Command> commandList;

    public static void init() {
        commandList = new ArrayList<Command>();
    }

    /**
     * Periodic function of {@link #RunCommand}; needs to be run every 20ms
     */
    public static void periodic() {
        for (int i = 0; i < commandList.size(); i++) {
            Command command = commandList.get(i);
            command.periodic();
            if (command.isDone()) {
                command.end();
                commandList.remove(i);
            }
        }
    }

    /**
     * Will only schedule if not already scheduled.
     * @param command Command to run
     */
    public static void activate(Command command) {
        if (!isActive(command)) {
            commandList.add(command);
            command.init();
        }
    }

    /**
     * Stops the command given and removes the schedule
     * @param command Command to interrupt
     */
    public static void deactivate(Command command) {
        commandList.get(commandList.indexOf(command)).end();
        commandList.remove(commandList.indexOf(command));
    }

    /**
     * Returns a boolean on whether or not the provided command is currently scheduled
     * @param command
     * @return true if command is running
     */
    public static boolean isActive(Command command) {
        return commandList.contains(command);
    }

    public static void emptyTrashCan() {
        commandList = new ArrayList<Command>();
    }

}