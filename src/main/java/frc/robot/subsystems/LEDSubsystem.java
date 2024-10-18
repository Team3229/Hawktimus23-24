package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDSubsystem extends SubsystemBase {
    
    private AddressableLED led;
    private AddressableLEDBuffer ledBuffer;

    private int frame;
    private double frameSlow;
    private int rainbowFirstPixelHue;

    private static final Color kNoteInIntake = new Color(63, 197, 10);

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

        this.setDefaultCommand(defaultRainbow());

    }

    @Override
    public void periodic() {
        led.setData(ledBuffer);
    }

    private Command defaultRainbow() {

        Command out = new Command() {
            @Override public void execute() {
                rainbowPattern(ledBuffer.getLength());
            }
                        };

        out.addRequirements(this);

        return out;
    }


    public Command noteIntake() {
        Command out = new Command() {
            
            @Override public void initialize() {
                setLEDRange(0,ledBuffer.getLength(),kNoteInIntake);
            }

            @Override public void end(boolean interrupted) {
              
            }

            @Override public boolean isFinished() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true; 
            }

        };

        out.addRequirements(this);
        return out;
    }





    // Helpers

    private  void setLED(int index, Color color) {
        ledBuffer.setLED(index % ledBuffer.getLength(), new Color(color.red, color.blue, color.green));
    }

    private  void setLEDRange(int first, int last, Color color) {
        for (int i = first; i < last; i++) {
            setLED(i, color);
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

    }
