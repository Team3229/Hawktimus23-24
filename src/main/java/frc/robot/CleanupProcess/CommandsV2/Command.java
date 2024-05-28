package frc.robot.CleanupProcess.CommandsV2;

/**
 * Commands help to add an extra dimension to work with. It adds a "time" dimension, making the program more practical for real-life application.
 * This is especially useful for robotics. To create a command, you must first "extend" a command, which is to assign different tasks to different parts of the command,
 * which are init, periodic, isDone, end, and getID.
 */
public class Command extends edu.wpi.first.wpilibj2.command.Command{

    public static Command createFromWPILIB(edu.wpi.first.wpilibj2.command.Command command) {
        
        return new Command() {
            @Override
            public void init() {
                command.initialize();
            }

            @Override
            public void periodic() {
                command.execute();
            }

            @Override
            public boolean isDone() {
                return command.isFinished();
            }

            @Override
            public void end() {
                command.end(false);
            }

            @Override
            public String getID() {
                return command.getName();
            }
        };
    }
    
    /**
     * Called once when the command is scheduled
     * @see CommandScheduler 
     */
    public void init(){}

    /**
     * Called once every robot loop 
     */
    public void periodic(){}

    /**
     * Used to determine when the command is ended
     */
    public boolean isDone(){return false;}

    /**
     * Ends the command
     */
    public void end(){}

    /**
     * Returns the command's unique identifier (its ID)
     */
    public String getID(){return "";}

    @Override
    /**
     * Overrides WPILib's "initialize" command with our init command
     */
    public void initialize(){
        init();
    }

    @Override
    /**
     * Overrides WPILib's "execute" command with our periodic command
     */
    public void execute(){
        periodic();
    }

    @Override
    /**
     * Overrides WPILib's "isFinished" command with our isDone command
     */
    public boolean isFinished(){
        return isDone();
    }

    @Override
    /**
     * Overrides WPILib's command with our "end" command
     */
    public void end(boolean interrupted){
        end();
        if (interrupted) System.out.println("INTERRUPTED " + getID());
    }

}
