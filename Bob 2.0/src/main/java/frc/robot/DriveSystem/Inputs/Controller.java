package frc.robot.DriveSystem.Inputs;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class Controller {
    
    private ControllerType controllerType;
    public GenericHID controller;
    Map<Object, Object> inputs;

    public Controller(ControllerType type, int id) {
        this.controllerType = type;
        this.controller = new GenericHID(id);
        inputs = new HashMap<>();
        
        nullControls();
    }

    public void update() {
        switch(controllerType) {
            case FlightStick:
                inputs.put(Controls.FlightStick.AxisX, controller.getRawAxis(0));
                inputs.put(Controls.FlightStick.AxisY, controller.getRawAxis(1));
                inputs.put(Controls.FlightStick.AxisZ, controller.getRawAxis(2));
                inputs.put(Controls.FlightStick.Throttle, controller.getRawAxis(3));
                
                inputs.put(Controls.FlightStick.Trigger, controller.getRawButton(1));
                inputs.put(Controls.FlightStick.Button2, controller.getRawButton(2));
                inputs.put(Controls.FlightStick.Button3, controller.getRawButton(3));
                inputs.put(Controls.FlightStick.Button4, controller.getRawButton(4));
                inputs.put(Controls.FlightStick.Button5, controller.getRawButton(5));
                inputs.put(Controls.FlightStick.Button6, controller.getRawButton(6));
                inputs.put(Controls.FlightStick.Button7, controller.getRawButton(7));
                inputs.put(Controls.FlightStick.Button8, controller.getRawButton(8));
                inputs.put(Controls.FlightStick.Button9, controller.getRawButton(9));
                inputs.put(Controls.FlightStick.Button10, controller.getRawButton(10));
                inputs.put(Controls.FlightStick.Button11, controller.getRawButton(11));
                inputs.put(Controls.FlightStick.Button12, controller.getRawButton(12));

                inputs.put(Controls.FlightStick.TriggerToggle, controller.getRawButtonPressed(1));
                inputs.put(Controls.FlightStick.Button2Toggle, controller.getRawButtonPressed(2));
                inputs.put(Controls.FlightStick.Button3Toggle, controller.getRawButtonPressed(3));
                inputs.put(Controls.FlightStick.Button4Toggle, controller.getRawButtonPressed(4));
                inputs.put(Controls.FlightStick.Button5Toggle, controller.getRawButtonPressed(5));
                inputs.put(Controls.FlightStick.Button6Toggle, controller.getRawButtonPressed(6));
                inputs.put(Controls.FlightStick.Button7Toggle, controller.getRawButtonPressed(7));
                inputs.put(Controls.FlightStick.Button8Toggle, controller.getRawButtonPressed(8));
                inputs.put(Controls.FlightStick.Button9Toggle, controller.getRawButtonPressed(9));
                inputs.put(Controls.FlightStick.Button10Toggle, controller.getRawButtonPressed(10));
                inputs.put(Controls.FlightStick.Button11Toggle, controller.getRawButtonPressed(11));
                inputs.put(Controls.FlightStick.Button12Toggle, controller.getRawButtonPressed(12));

                inputs.put(Controls.FlightStick.DPad, controller.getPOV());
                
                break;
            case XboxController:
                inputs.put(Controls.XboxController.AButton, controller.getRawButton(1));
                inputs.put(Controls.XboxController.BButton, controller.getRawButton(2));
                inputs.put(Controls.XboxController.XButton, controller.getRawButton(3));
                inputs.put(Controls.XboxController.YButton, controller.getRawButton(4));
                inputs.put(Controls.XboxController.RightStickButton, controller.getRawButton(10));
                inputs.put(Controls.XboxController.LeftStickButton, controller.getRawButton(9));
                inputs.put(Controls.XboxController.StartButton, controller.getRawButton(8));
                inputs.put(Controls.XboxController.SelectButton, controller.getRawButton(7));
                inputs.put(Controls.XboxController.RightBumper, controller.getRawButton(6));
                inputs.put(Controls.XboxController.LeftBumper, controller.getRawButton(5));

                inputs.put(Controls.XboxController.AButtonToggle, controller.getRawButtonPressed(1));
                inputs.put(Controls.XboxController.BButtonToggle, controller.getRawButtonPressed(2));
                inputs.put(Controls.XboxController.XButtonToggle, controller.getRawButtonPressed(3));
                inputs.put(Controls.XboxController.YButtonToggle, controller.getRawButtonPressed(4));
                inputs.put(Controls.XboxController.SelectButtonToggle, controller.getRawButtonPressed(7));
                inputs.put(Controls.XboxController.StartButtonToggle, controller.getRawButtonPressed(8));
                inputs.put(Controls.XboxController.RightBumperToggle, controller.getRawButtonPressed(6));
                inputs.put(Controls.XboxController.LeftBumperToggle, controller.getRawButtonPressed(5));
                inputs.put(Controls.XboxController.RightStickToggle, controller.getRawButtonPressed(10));
                inputs.put(Controls.XboxController.LeftStickToggle, controller.getRawButtonPressed(9));

                inputs.put(Controls.XboxController.LeftTriggerAxis, controller.getRawAxis(2));
                inputs.put(Controls.XboxController.RightTriggerAxis, controller.getRawAxis(3));
                inputs.put(Controls.XboxController.LeftX, controller.getRawAxis(0));
                inputs.put(Controls.XboxController.LeftY, controller.getRawAxis(1));
                inputs.put(Controls.XboxController.RightX, controller.getRawAxis(4));
                inputs.put(Controls.XboxController.RightY, controller.getRawAxis(5));

                inputs.put(Controls.XboxController.DPad, controller.getPOV());

                break;
            default:
                //YOU SUCK, how the heck did this happen mate. Do better.
                System.out.println("You suck");
                break;
            
        }
    }

    public void nullControls() {
        switch(controllerType) {
            case FlightStick:
                inputs.put(Controls.FlightStick.AxisX, 0.0);
                inputs.put(Controls.FlightStick.AxisY, 0.0);
                inputs.put(Controls.FlightStick.AxisZ, 0.0);
                inputs.put(Controls.FlightStick.Throttle, 0.0);
                
                inputs.put(Controls.FlightStick.Trigger, false);
                inputs.put(Controls.FlightStick.Button2, false);
                inputs.put(Controls.FlightStick.Button3, false);
                inputs.put(Controls.FlightStick.Button4, false);
                inputs.put(Controls.FlightStick.Button5, false);
                inputs.put(Controls.FlightStick.Button6, false);
                inputs.put(Controls.FlightStick.Button7, false);
                inputs.put(Controls.FlightStick.Button8, false);
                inputs.put(Controls.FlightStick.Button9, false);
                inputs.put(Controls.FlightStick.Button10, false);
                inputs.put(Controls.FlightStick.Button11, false);
                inputs.put(Controls.FlightStick.Button12, false);

                inputs.put(Controls.FlightStick.TriggerToggle, false);
                inputs.put(Controls.FlightStick.Button2Toggle, false);
                inputs.put(Controls.FlightStick.Button3Toggle, false);
                inputs.put(Controls.FlightStick.Button4Toggle, false);
                inputs.put(Controls.FlightStick.Button5Toggle, false);
                inputs.put(Controls.FlightStick.Button6Toggle, false);
                inputs.put(Controls.FlightStick.Button7Toggle, false);
                inputs.put(Controls.FlightStick.Button8Toggle, false);
                inputs.put(Controls.FlightStick.Button9Toggle, false);
                inputs.put(Controls.FlightStick.Button10Toggle, false);
                inputs.put(Controls.FlightStick.Button11Toggle, false);
                inputs.put(Controls.FlightStick.Button12Toggle, false);

                inputs.put(Controls.FlightStick.DPad, -1);
                
                break;
            case XboxController:
                inputs.put(Controls.XboxController.AButton, false);
                inputs.put(Controls.XboxController.BButton, false);
                inputs.put(Controls.XboxController.XButton, false);
                inputs.put(Controls.XboxController.YButton, false);
                inputs.put(Controls.XboxController.RightStickButton, false);
                inputs.put(Controls.XboxController.LeftStickButton, false);
                inputs.put(Controls.XboxController.StartButton, false);
                inputs.put(Controls.XboxController.SelectButton, false);
                inputs.put(Controls.XboxController.RightBumper, false);
                inputs.put(Controls.XboxController.LeftBumper, false);

                inputs.put(Controls.XboxController.AButtonToggle, false);
                inputs.put(Controls.XboxController.BButtonToggle, false);
                inputs.put(Controls.XboxController.XButtonToggle, false);
                inputs.put(Controls.XboxController.YButtonToggle, false);
                inputs.put(Controls.XboxController.SelectButtonToggle, false);
                inputs.put(Controls.XboxController.StartButtonToggle, false);
                inputs.put(Controls.XboxController.RightBumperToggle, false);
                inputs.put(Controls.XboxController.LeftBumperToggle, false);
                inputs.put(Controls.XboxController.RightStickToggle, false);
                inputs.put(Controls.XboxController.LeftStickToggle, false);

                inputs.put(Controls.XboxController.LeftTriggerAxis, 0.0);
                inputs.put(Controls.XboxController.RightTriggerAxis, 0.0);
                inputs.put(Controls.XboxController.LeftX, 0.0);
                inputs.put(Controls.XboxController.LeftY, 0.0);
                inputs.put(Controls.XboxController.RightX, 0.0);
                inputs.put(Controls.XboxController.RightY, 0.0);

                inputs.put(Controls.XboxController.DPad, -1);

                break;
            default:
                //YOU SUCK, how the heck did this happen mate. Do better.
                System.out.println("You suck");
                break;
        }
    }
    
    public Object get(Object input) {
        return inputs.get(input);
    }

    public void rumble(RumbleType rumbleType, double strength) {
        controller.setRumble(rumbleType, strength);
    }
    
    public static class Controls {
        public enum FlightStick {
          //Axes
          /** double */
          AxisX,
          /** double */
          AxisY,
          /** double */
          AxisZ,
          /** double */
          Throttle,

          //Buttons
          /** boolean */
          Trigger,
          /** boolean */
          Button2,
          /** boolean */
          Button3,
          /** boolean */
          Button4,
          /** boolean */
          Button5,
          /** boolean */
          Button6,
          /** boolean */
          Button7,
          /** boolean */
          Button8,
          /** boolean */
          Button9,
          /** boolean */
          Button10,
          /** boolean */
          Button11,
          /** boolean */
          Button12,

          /** boolean */
          TriggerToggle,
          /** boolean */
          Button2Toggle,
          /** boolean */
          Button3Toggle,
          /** boolean */
          Button4Toggle,
          /** boolean */
          Button5Toggle,
          /** boolean */
          Button6Toggle,
          /** boolean */
          Button7Toggle,
          /** boolean */
          Button8Toggle,
          /** boolean */
          Button9Toggle,
          /** boolean */
          Button10Toggle,
          /** boolean */
          Button11Toggle,
          /** boolean */
          Button12Toggle,

          /** int */
          DPad
        }
        public enum XboxController {
          RightY,
          RightX,
          RightStickButton,
          RightStickToggle,
          LeftX,
          LeftY,
          LeftStickButton,
          LeftStickToggle,
          AButton,
          BButton,
          XButton,
          XButtonToggle,
          YButton,
          YButtonToggle,
          RightBumper,
          RightBumperToggle,
          LeftBumper,
          LeftBumperToggle,
          RightTriggerAxis,
          LeftTriggerAxis,
          DPad,
          StartButton,
          StartButtonToggle,
          SelectButton,
          SelectButtonToggle,
          AButtonToggle,
          BButtonToggle,
        }
      }

    public enum ControllerType {
        XboxController,
        FlightStick
    }
}