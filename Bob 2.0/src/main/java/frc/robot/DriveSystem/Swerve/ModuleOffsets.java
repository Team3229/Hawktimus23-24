//Otters: 3229 Programming SubTeam

package frc.robot.DriveSystem.Swerve;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Offsets class for saving swerve module angle offsets to the RIO and reading them later */
public class ModuleOffsets {

    /** The base path into the RIO */
    private final String path = "/home/lvuser/offsets/";
    /** The filenames we will be storing the offsets in */
    private final String[] fileNames = {"frontLeft.txt", "frontRight.txt", "backLeft.txt", "backRight.txt"};

    /** Adds a boolean to reset the offsets or not within SmartDashboard, Implemented in Robot.java */
    public ModuleOffsets() {
        SmartDashboard.putBoolean("resetAngleOffsets", false);
    }

    /**
     * Calculates the offsets required to zero the angles, and writes it to the RIO
     * @param fL (Rotation2d) The front left angle when rotated to be perpendicular to the forwards driving direction
     * @param fR (Rotation2d) The front right angle when rotated to be perpendicular to the forwards driving direction
     * @param bL (Rotation2d) The back left angle when rotated to be perpendicular to the forwards driving direction
     * @param bR (Rotation2d) The back right angle when rotated to be perpendicular to the forwards driving direction
     * @return (double[]) The new offsets that were just written to the RIO
     */
    public Rotation2d[] calculateOffsets(Rotation2d fL, Rotation2d fR, Rotation2d bL, Rotation2d bR) {
        // takes the current values, assumes they should be 0, and returns an [] of the new values to set them to after storing it for futute use
        // if old offsets < 90, its closer to 0. Else if > 90 & < 270, closer to 180. Else its > 270, so closer to 360
        // if the values passed, which are the current values, and should be zero.
        Rotation2d[] currentValues = read();
        Rotation2d[] newOffsets = {
            currentValues[0].plus(Rotation2d.fromDegrees((fL.getDegrees() < 90 ? -fL.getDegrees(): fL.getDegrees() > 90 && fL.getDegrees() < 270 ? 180 - fL.getDegrees():360 - fL.getDegrees()))),
            currentValues[1].plus(Rotation2d.fromDegrees((fR.getDegrees() < 90 ? -fR.getDegrees(): fR.getDegrees() > 90 && fR.getDegrees() < 270 ? 180 - fR.getDegrees():360 - fR.getDegrees()))),
            currentValues[2].plus(Rotation2d.fromDegrees((bL.getDegrees() < 90 ? -bL.getDegrees(): bL.getDegrees() > 90 && bL.getDegrees() < 270 ? 180 - bL.getDegrees():360 - bL.getDegrees()))),
            currentValues[3].plus(Rotation2d.fromDegrees((bR.getDegrees() < 90 ? -bR.getDegrees(): bR.getDegrees() > 90 && bR.getDegrees() < 270 ? 180 - bR.getDegrees():360 - bR.getDegrees())))
        };
        writeOffsets(newOffsets);
        return newOffsets;
    }

    /**
     * Writes the new offsets to the RIO in the files
     * @param newAngles (double[]) The new angles to be written to the RIO
     */
    private void writeOffsets(Rotation2d[] newAngles) {
        // attempt to write the new offsets to the file, catches exceptions if failure
        // writes strings to the file, need to parse it to doubl.getDegrees()e once read.
        try {
            for (int i = 0; i < fileNames.length; i++) {
                FileWriter writer = new FileWriter(path + fileNames[i]);
                writer.write(String.valueOf(newAngles[i].getDegrees()));
                writer.close();
            }
        } catch(IOException e){
            System.out.println("An error occurred while writing the swerve angle offsets to file.");
            e.printStackTrace();
        }
    }

    /**
     * Reads the currently stored values in the RIO
     * @return (Rotation2d[]) The currently stored values in the RIO 
     */
    public Rotation2d[] read() {
        // returns the currently stored values, 0,0,0,0 if none, as doubl.getDegrees()es.
        Rotation2d[] values = {new Rotation2d(), new Rotation2d(), new Rotation2d(), new Rotation2d()};
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
            System.out.println("An error occurred while reading the swerve angle offsets from file.");
            e.printStackTrace();
        }
        return values;
    }
}
