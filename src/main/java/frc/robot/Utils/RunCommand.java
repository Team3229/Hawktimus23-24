package frc.robot.Utils;

import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;

public class RunCommand {
    
    private static List<Command> commandList;

    /**
     * Periodic function of {@link #RunCommand}; needs to be run every 20ms
     */
    public static void periodic() {
        int size = commandList.size();
        for (int i = 0; i < size; i++) {
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
        if (!commandList.contains(command)) {
            commandList.add(command);
            command.initialize();
        }
    }

    /**
     * Stops the command given and removes the schedule
     * @param command Command to interrupt
     */
    public static void interrupt(Command command) {
        commandList.get(commandList.indexOf(command)).end(true);
        commandList.remove(commandList.indexOf(command));
    }

}