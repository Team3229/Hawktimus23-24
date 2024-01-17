// Author: 3229 Programming Subteam 2022-2023

/*
 * Capture Replay Class
 * Capture Replay (CR) is a system of auto where you record an auto sequence, and play it back during the match.
 * To setup CR for a new bot you must do the following
 * -Remove all old bots dropdowns and stage logic
 * -Create a dropdown for each "stage" of autos you would like to create
 * -Create as many options (just a string, used as filename) for each dropdown as you like.
 * -Implement them in both readDropdowns and setupDropdowns, following examples.
 * -Add cases within setupPlayback as you have stages in the auto accordingly
 * -Add logic chain for setupRecording, following its comment blobs instructions
 * 
 * To implement CR in Robot.java
 * -Instantiate CaptureReplay
 * -In autoInit call .closeFile THEN .setupPlayback
 * -In autoPeriodic set your inputs to the return value of .readFile, pass it to your input handler
 * -In testInit call .setupRecording AFTER making sure to select whatever settings you want with your dropdowns
 * -In testPeriodic pass your inputs to .record as you drive. It will automatically clear null frames before and after the auto ensuring no wasted miliseconds.
 * 
 * To SSH into rio incase it is needed
 * -Connect to robot
 * -In terminal run sshlvuser@10.32.29.2 (Change middle two numbers to your respective team name if appliccable)
 * -vi [filename] to open a file
 * -:x to exit vi
 */

package frc.robot.Autonomous;

import java.io.*;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Inputs.Controller;
import frc.robot.Inputs.Controller.ControllerType;

/**
 * Capture replay class, class for recording the controller inputs during test mode and playing them back in auto mode. Requires Inputs and ControllerInputs
 * @see Inputs
 * @see ControllerInputs 
 */
public class CaptureReplay {

	/** Array of the currently selected auto files, Increase length depending on how many auto stages you have. */
	private static String[] selectedAuto = {""};

	static final boolean WRITE = true;
	static final boolean READ = false;
	/** Path on the RIO to save the auto files, make sure a directory is created before using if applicable. */
	static final String basePath = "/home/lvuser/autoFiles/";

	/** Example dropdown for choosing an auto sequence, create multiple with better names as needed. */
	public final SendableChooser <String> DummyDropdown = new SendableChooser <> ();
	/** Example option for a dropdown, */
	public static final String DummyOption = "Dummy";

	//SPARKY HOTFIX OPTIONS
	//The following are options for the 2022-2023 Charged Up Season
	//Remove these for future auto use, and remove their implemeation.
	//When removing, make sure to switch from using OLDbasePath to basePath, and ssh into the rio to ensure the folder exists.
	//These are only here for backwards compatitability and do not use the new multi stage system
	//No time to remake them sadly, so they stay till 2023-2024 season :(
	public static final SendableChooser <String> SparkyAutos = new SendableChooser<>();
	static final String OLDbasePath = "/home/lvuser/";
	public static final String basicLeft = "bbl";
	public static final String basicMid = "bbm";
	public static final String basicRight = "bbr";

	/** No selection option, add to all dropdowns. */
	public static final String noSelection = "N/A";
	
	/** If Capture replay has finished all autos. */
	public static boolean autoFinished = false;

	private static File cmdFile;
	private static FileInputStream fReader;
	private static ObjectInputStream cmdRead;
	private static FileOutputStream fWriter;
	private static ObjectOutputStream cmdWrite;

	/** Have inputs started during recording */
	public static boolean inputsStarted = false;
	/** Number of cached null frames during recording */
	public static int cachedNulls = 0;

	/** The current step in the auto sequence */
	private static int autoStep = 1;
	/** The inputs instance */
	private static Controller[] controllers = {new Controller(ControllerType.FlightStick, 0)};

	public CaptureReplay() {}

	/**Reads the dropdowns and sets up selectedAuto for playback/recording autos. */
	private static void readDropdowns() {
		//Change this so it sets up selectedAuto for all of the autos
		//2022-2023 Charged Up Sparky auto options read
		selectedAuto[0] = SparkyAutos.getSelected();
	}
	/** Sets up the dropdowns and their options, Call this on robotInit */
	public static void setupDropdowns() {
		// EXAMPLE IMPLEMENTATION OF DROPDOWNS, DO NOT REMOVE
		// DummyDropdown.setDefaultOption("NO SELECTION", noSelection);
		// DummyDropdown.addOption("Dummy Option", DummyOption);
		// SmartDashboard.putData("DummyDropdown", DummyDropdown);

		//2022-2023 Charged Up Sparky auto options setup
		SparkyAutos.setDefaultOption("NO SELECTION", noSelection);
		SparkyAutos.addOption("Basic Left", basicLeft);
		SparkyAutos.addOption("Basic Right", basicMid);
		SparkyAutos.addOption("Basic Middle", basicRight);
		SmartDashboard.putData("Sparky Autos", SparkyAutos);
	}

	/**
	 * Sets up capture replay for the playback of an auto sequence
	 */
	public static void setupPlayback() {
		readDropdowns();
		switch(autoStep) {
			//Add more cases between 1 and 2, incrementing by one for each stage for autos, changing the paths as necessary.
			//As an example stage two it would be selectedAuto[0] + selectedAuto[1] not just selectedAuto[0] or whatever, because were merging fnames to match recorded status
			//Make sure to end with a case that ends the auto! Else everything shatters!
			case 1:
				cmdFile = new File(OLDbasePath + selectedAuto[0] + ".aut");
				break;
			case 2:
				//End of auto.
				autoFinished = true;
				return;
        }
		try {
			fReader = new FileInputStream(cmdFile);
			cmdRead = new ObjectInputStream(fReader);
		} catch(IOException err) {
			System.out.println("Error opening auto file for read: " + err.toString());
		}
	}

	/**
	 * Reads the next frame from the selected auto file and handles continuing the sequence if we reach the end of the currently selected file
	 * @return (Inputs) The inputs instance of the next frame
	 */
	public static Controller[] readFile() {
		// System.out.println("Reading auto file...");
		for(int i = 0; i < controllers.length; i++) {
			controllers[i].nullControls();
		}

		try {
			controllers =  (Controller[]) cmdRead.readObject();
		} catch (IOException err) {
			// if were finished, check what stage we were in and see if theres another file we need to setup and begin playback for
			closeFile();
			++autoStep;
			setupPlayback();
		} catch (ClassNotFoundException cerr) {
			System.out.println("Could not read controller inputs object from auto file: " + cerr.toString());
		}
		return controllers;
	}

	/**
	 * Sets up capture replay for recording a new file or rewriting an old one
	 * @param inputFileName (String[]) An array of the file identifiers, selected from the dropdowns
	 */
	public static void setupRecording() {
		readDropdowns();
		inputsStarted = false;
		//EXPLANATION COMMENT BLOB BECAUSE THIS HAS IMPORTANT LOGIC
		//You want to create a system to combine the sections into names according to what was selected
		//As in lets say you have 3 stages to this auto
		//If you want to record stage two, well that depends on what stage 1 was right?
		//So you select stage1, assuming prerecorded, then select whatever stage two choice you are recording
		//Record the stage two auto (Remember to start the robot at its ending location from the stage 1 or it all breaks!)
		//So logic for three stages would be something like
		// if 1 selected, nothing else, record 1
		// elif 1 and 2 selected, record the file for the string name basePath + selectedAuto[0] + selectedAuto[1] + .aut
		// elif 1 2 and 3 selected, record the file for that specific stage three auto
		//I hope to all future readers this makes sense, because its a pain if you mess this up lol -NS

		//This is just a hotfix because it needs to work with sparky, remove when setting up next bots auto.
		cmdFile = new File(OLDbasePath + selectedAuto[0] + ".aut");
		
		try {
			fWriter = new FileOutputStream(cmdFile);
			cmdWrite = new ObjectOutputStream(fWriter);
		} catch(IOException err) {
			System.out.println("Error opening auto file for write: " + err.toString());
		}
	}

	/**
	 * Records the current frame of inputs to the selected file, does not record blank frames at the starts and ends of an auto sequence.
	 * @param inputs (Inputs) The inputs for the current frame, to be recorded.
	 */
	public static void record(Controller[] inputs) {
		System.out.println("Writing auto file...");
		Controller[] dummyInputs = inputs;
		for(int i = 0; i < dummyInputs.length; i++) {
			dummyInputs[i].nullControls();
		}
		if (inputsStarted) {
			// Recording input, means there has already been at least a single input this recording. 
			if (controllers.equals(dummyInputs)) {
				// If there is no input, remember that there was a frame of no inputs.
				++cachedNulls;
			} else if (cachedNulls > 0) {
				// If there is an input, and we have nulls to cache, record them and then record the input we just recieved.
				for (int x = 0; x <= cachedNulls; ++x) {
					try {
						System.out.println("Writing null frame...");
						cmdWrite.writeObject(dummyInputs);
					} catch(IOException err) {
						System.out.println("Error writing null frame: " + err.toString());
					}
				}
				//Record the inputs frame that caused this to happen now
				try {
					System.out.println("Writing frame...");
					cmdWrite.writeObject(inputs);
				} catch(IOException err) {
					System.out.println("Error writing frame: " + err.toString());
				}
				//Reset cached nulls
				cachedNulls = 0;
			} else {
				// Have an input, and no nulls to cache, write a regular frame of inputs.
				try {
					System.out.println("Writing auto file...");
					cmdWrite.writeObject(inputs);
				} catch(IOException err) {
					System.out.println("Error writing auto file: " + err.toString());
				}
			}
		} else if (inputs.equals(dummyInputs)) {
			// Waiting for not null input to start recording
			System.out.println("Waiting for controller input...");
		} else {
			// Start recording input
			//This should only run once, the first frame of actual input that is not null.
			System.out.println("Started recording");
			inputsStarted = true;
			try {
				System.out.println("Writing auto file...");
				cmdWrite.writeObject(inputs);
			} catch(IOException err) {
				System.out.println("Error writing auto file: " + err.toString());
			}
		}
	}

	/** Closes the currently selected file */
	public static void closeFile() {
		System.out.println("Auto file closed.");
		if (fReader != null) {
			try {
				fReader.close();      
			} catch (IOException err) {
				System.out.println("Error closing auto file: " + err.toString());
			}
		}
		if (fWriter != null) {
			try {
				fWriter.close();
			} catch(IOException err) {
				System.out.println("Error closing auto file: " + err.toString());
			}
		}
	}
}