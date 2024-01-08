# SwerveDrive Base
This WPILIB project includes a **fully-featured** swerve drivetrain base complete with full **JavaDoc** documentation, extended **customizability**, and a **robot relative mode**.

# Getting Started
In order to prepare this project for your next FRC robot, there are a couple of things you will need to configure yourself. 
>This program was modeled on the [SwerveDriveSpecialties MK4 Swerve Module](https://www.swervedrivespecialties.com/products/mk4-swerve-module) with REV Robotics NEO Pinions and the L1 gear ratio. If these are not the modules you are using, you may have to change additional things in order to make this project work for you. Skip to [Additional Configuration](#additional-configuration) below to see more details.

>This program is also only configured for a square chassis, although it would not be in any way difficult to change it to work for another shape.

## Constants
Below is a list of constants and their locations in the project that will most likely need to be configured for your own uses:

- `Rotation2d[] moduleOffsets`
	- Located in **SwerveKinematics.java** at **line 21**
	- This array holds the **encoder offsets** for each module's CANCoder. While your swerve modules are being assembled, it is **very unlikely** that the enoder's magnet's poles will be **positioned properly**. In order to account for this, you will have to **hold the wheel in the zeroed position**, and then **find the recorded angle**. This angle you can then use to **offset the encoder to 0 degrees**. Repeat for each module.
- `double robotWidth = 0.762`
	- Located in **SwerveKinematics.java** at **line 46**
	- This variable holds the width of the robot in meters. (Defaulted to 0.762 meters, which is the maximum chassis perimeter for the 2022-23 FRC season.)
- `boolean brakeMode = true`
	- Located in **SwerveKinematics.java** at **line 54**
	- This boolean defines whether or not to enable brake mode on the chassis's drive motors.
- `SwerveModule constructors`
	- Located in **SwerveKinematics.java** at **lines 62-65**
	- Here you simply need to configure the CAN IDs for each modules' motors and encoders.
## Finishing Up
Because of many differences between controllers used for FRC, the full implementation in Robot.java is not complete. You will need to configure your own controller interface and integrate it with the SwerveKinematics.drive() method. It is already stated in teleopPeriodic, but you will need to give input for X, Y, and Z movement. These should be values between -1 and 1.
>For the featured robot relative mode, you will need to set **SwerveKinematics.realtiveMode** to true and false according to  controller input.
# Additional Configuration
If you are simply using a different SDS gear ratio, then here is a table with the values you should insert into the variables in SwerveModule.java:

| SDS Ratio Table | L1   | L2   | L3   | L4   |
| --------------- | ---- | ---- | ---- | ---- |
| angleGearRatio  | 12.8 | 12.8 | 12.8 | 12.8 |
| driveGearRatio  | 8.14 | 6.75 | 6.12 | 5.14 |

# Credits
This project was created by the Software Subteam of FRC Team 3229 Hawktimus Prime, using libraries provided by Worcester Polytechnic Institute, Cross The Road Electronics, and REV Robotics.

Special thanks to Sebastian Roman, a 3229 alumni, who inspired us to create as many resources for other teams as possible.

Happy coding!
