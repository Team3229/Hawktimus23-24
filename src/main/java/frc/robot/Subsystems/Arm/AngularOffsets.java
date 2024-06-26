package frc.robot.Subsystems.Arm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Subsystems.Drivetrain.SwerveKinematics;
/** Offsets class for saving swerve module angle offsets to the RIO and reading them later */
public class AngularOffsets {

    /** The base path into the RIO */
    private static final String path = "/home/lvuser/offsets/";
    /** The filenames we will be storing the offsets in */
    private static final String[] fileNames = {"Arm.txt"};

    public static void checkBoolean(){
        if(SmartDashboard.getBoolean("resetAngleOffsets", false)){
			SwerveKinematics.configOffsets(AngularOffsets.calculateOffsets(SwerveKinematics.frontLeftModule.getAbsolutePosition(), SwerveKinematics.frontRightModule.getAbsolutePosition(), SwerveKinematics.backLeftModule.getAbsolutePosition(), SwerveKinematics.backRightModule.getAbsolutePosition()));
			SmartDashboard.putBoolean("resetAngleOffsets", false);
		} else {
			SwerveKinematics.configOffsets(AngularOffsets.read());
		}
    }
    /** Adds a boolean to reset the offsets or not within SmartDashboard, Implemented in Robot.java */
    public static void init() {
        SmartDashboard.putBoolean("resetAngleOffsets", false);
        
        // create files if they don't exist
        new File(path).mkdirs();
        try {
            new File(path + fileNames[0]).createNewFile();
        } catch (Exception e) {
            System.out.println(" [MODULE OFFSETS] An error occured while creating offset files.");
        }
    }

    /**
     * Calculates the offsets required to zero the angles, and writes it to the RIO
     * @param Arm (Rotation2d) The front left angle when rotated to be parallel to the forwards driving direction
     * @return (double[]) The new offsets that were just written to the RIO
     */
    public static Rotation2d[] calculateOffsets(Rotation2d Arm) {
        // takes the current values, assumes they should be 0, and returns an [] of the new values to set them to after storing it for future use
        Rotation2d[] currentValues = read();
        Rotation2d[] newOffsets = {
            currentValues[0].plus(Rotation2d.fromDegrees((Arm.getDegrees() < 90 ? -Arm.getDegrees(): Arm.getDegrees() > 90 && Arm.getDegrees() < 270 ? 180 - Arm.getDegrees():360 - Arm.getDegrees()))),
        };

        writeOffsets(newOffsets);
        return newOffsets;
    }

    /**
     * Writes the new offsets to the RIO in the files
     * @param newAngles (double[]) The new angles to be written to the RIO
     */
    private static void writeOffsets(Rotation2d[] newAngles) {
        // attempt to write the new offsets to the file, catches exceptions if failure
        // writes strings to the file, need to parse it to doubl.getDegrees()e once read.
        try {
            for (int i = 0; i < fileNames.length; i++) {
                FileWriter writer = new FileWriter(path + fileNames[i]);
                writer.write(String.valueOf(newAngles[i].getDegrees()));
                writer.close();
            }
        } catch(IOException e){
            System.out.println(" [ANGULAR OFFSETS] An error occurred while writing the arm angle offsets to file.");
            e.printStackTrace();
        }
    }

    /**
     * Reads the currently stored values in the RIO
     * @return (Rotation2d[]) The currently stored values in the RIO 
     */
    public static Rotation2d[] read() {
        // returns the currently stored values, 0,0,0,0 if none, as doubl.getDegrees()es.
        Rotation2d[] values = {new Rotation2d()};
        try {
            for (int i = 0; i < fileNames.length; i++) {
                File file = new File(path + fileNames[i]);
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine()) {
                    values[i] = Rotation2d.fromDegrees(Double.parseDouble(scanner.nextLine()));
                }
                scanner.close();
            }
        } catch(IOException e){
            System.out.println(" [ANGULAR OFFSETS] An error occurred while reading the arm angle offsets from file.");
            e.printStackTrace();
        }
        return values;
    }
}
