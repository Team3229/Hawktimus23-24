package frc.robot.CommandsV2;

import java.util.ArrayList;

public class CommandScheduler {
    
    private static ArrayList<Command> commandList;
    public static boolean terminated = false;

    public static void init() {
        commandList = new ArrayList<Command>();
    }

    /**
     * Periodic function of {@link #RunCommand}; needs to be run every 20ms
     */
    public static void periodic() {
        if(terminated) commandList = new ArrayList<Command>();
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
        if(terminated) return;
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
        for (int i = 0; i < commandList.size(); i++) {
            Command iter = commandList.get(i);
            if (iter.getID() == command.getID()) {
                commandList.remove(i);
            }
        }
    }

    /**
     * Returns a boolean on whether or not the provided command is currently scheduled
     * @param command
     * @return true if command is running
     */
    public static boolean isActive(Command command) {
        for (int i = 0; i < commandList.size(); i++) {
            if (commandList.get(i).getID() == command.getID()) {
                return true;
            }
        }
        return false;
    }

    public static void emptyTrashCan() {
        commandList = new ArrayList<Command>();
    }

}