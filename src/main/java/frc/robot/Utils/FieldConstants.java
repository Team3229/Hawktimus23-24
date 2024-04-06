package frc.robot.Utils;

import edu.wpi.first.math.geometry.Translation2d;

public class FieldConstants {
    public static final double[] BLUE_AMP = {1.845, 8.2};
    public static final double[] BLUE_SPEAKER = {0, 5.5};
    public static final double[] BLUE_SHOOTING_LINE = {5.8, 0};
    public static final double[][] BLUE_STAGE = {{2.9, 4.3},{5.8, 5.8},{5.8,2.8}};

    public static final double[] CENTER = {8.27, 4.1};
    
    public static final double[] RED_AMP = {14.7, 8.2};
    public static final double[] RED_SPEAKER = {16.5, 5.5};
    public static final double[] RED_SHOOTING_LINE = {10.8, 0};
    public static final double[][] RED_STAGE = {{13.7,4.3},{10.5,5.8},{10.5,2.8}};

    public static final Translation2d BLUE_SPEAKER_P = new Translation2d(BLUE_SPEAKER[0], BLUE_SPEAKER[1]);
    public static final Translation2d RED_SPEAKER_P = new Translation2d(RED_SPEAKER[0], RED_SPEAKER[1]);

}
