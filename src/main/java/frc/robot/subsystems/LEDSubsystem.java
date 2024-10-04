package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDSubsystem extends SubsystemBase {
    
    private AddressableLED led;
    private AddressableLEDBuffer ledBuffer;

    private int frame;
    private double frameSlow;
    private int rainbowFirstPixelHue;

    public enum Pattern {
        Rainbow,
        Hawk,
        Alternate
    }

    public LEDSubsystem() {
        led = new AddressableLED(3);
        ledBuffer = new AddressableLEDBuffer(180);

        led.setLength(ledBuffer.getLength());

        led.setData(ledBuffer);
        led.start();
    }

    @Override
    public void periodic() {
        setPattern(Pattern.Rainbow);
        led.setData(ledBuffer);
    }

    private  void setLED(int index, Color color) {
        ledBuffer.setLED(index % ledBuffer.getLength(), new Color(color.red, color.blue, color.green));
    }

    private  void setLEDRange(int first, int last, Color color) {
        for (int i = first; i < last; i++) {
            setLED(i, color);
        }
    }

    public void setPattern(Pattern pattern) {

        switch (pattern) {
            case Rainbow:
                rainbowPattern(ledBuffer.getLength());
                frameSlow += 0.25;
                break;
            
            case Hawk:
                hawkPattern(10, ledBuffer.getLength());
                frameSlow += 0.25;
                break;

            case Alternate:
                alternate();
                break;
        
            default:
                break;
        }
        if (frameSlow == 1) {
            frame += 1;
            frameSlow = 0;
        }

        if (frame > ledBuffer.getLength()) {
            frame = 0;
        }

    }
    
    private void hawkPattern(int segmentLength, int stripLength) {
        for (int j = 0; j < stripLength; j += segmentLength * 2) {

            setLEDRange(j + frame, j + frame + segmentLength, new Color(180, 0, 255));

            setLEDRange(j + segmentLength + frame, j + frame + segmentLength * 2, new Color(180, 0, 255));
            
        }
    }

    private void rainbowPattern(int stripLength) {
        for (var i = 0; i < stripLength; i++) {
            // Calculate the hue - hue is easier for rainbows because the color
            // shape is a circle so only one value needs to precess
            final var hue = (rainbowFirstPixelHue + (i * 180 / stripLength)) % 180;
            // Set the value
            setLED(i, Color.fromHSV(hue, 255, 128));
          }
          // Increase by to make the rainbow "move"
          rainbowFirstPixelHue += 1;
          // Check bounds
          rainbowFirstPixelHue %= 180;
    }

    private void alternate() {
        for (int i = 36; i < ledBuffer.getLength() - 36; i += 2) {
            setLED(i, Color.kRed);
            setLED(i+1, Color.kBlue);
        }
    }

}
