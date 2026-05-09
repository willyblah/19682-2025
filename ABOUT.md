# FTC Robot Code Guide

This guide explains how this robot code is organized and what each major part does. It is written for teammates who are new to FTC programming, so it starts with the basic FTC ideas before getting into our specific code.

## 1. What This Project Is

This is an FTC SDK Android Studio project. The robot controller phone or Control Hub runs this code during matches.

There are two major modules in the project:

- `FtcRobotController/`: the standard FTC app and SDK code. Most of this should not be edited.
- `TeamCode/`: our team robot code. This is where our opmodes, subsystems, constants, and autonomous routines live.

Most of the important code is here:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode
```

The main folders inside `teamcode` are:

```text
auto/          Autonomous opmodes
commands/      Reusable command classes, mostly for autonomous
constants/     Hardware names, field positions, shooter settings, tuning values
pedroPathing/  Pedro Pathing drivetrain/localization setup
subsystems/    Robot mechanisms: drivetrain, intake, shooter, follower
teleOp/        Driver-controlled opmodes
```

## 2. FTC Programming Basics

An FTC robot program is usually built around **opmodes**.

An opmode is a program that appears on the Driver Station. Before a match, drivers choose one opmode from the Driver Station menu and press Init, then Start.

There are two main kinds of opmodes:

- **TeleOp**: driver-controlled period. Gamepads control the robot.
- **Autonomous**: robot runs pre-programmed actions without driver control.

In code, FTC identifies opmodes using annotations:

```java
@TeleOp
public class AB_Tele extends LinearOpMode { ... }
```

```java
@Autonomous(name = "RED | Near | 15")
public class AutoRedNear15 extends OpMode { ... }
```

The text in `@Autonomous(name = "...")` is what appears on the Driver Station list.

## 3. Hardware Map Names

The robot hardware configuration on the Control Hub has names for motors, servos, and sensors. The Java code must use exactly the same names.

Our hardware names are stored in:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants/RobotConfig.java
```

Current names:

| Code Constant | Hardware Name | Meaning |
| --- | --- | --- |
| `LEFT_FRONT` | `lf` | left front drive motor |
| `LEFT_BACK` | `lb` | left back drive motor |
| `RIGHT_FRONT` | `rf` | right front drive motor |
| `RIGHT_BACK` | `rb` | right back drive motor |
| `PIN_POINT` | `pp` | goBILDA Pinpoint odometry |
| `LEFT_SHOOTER` | `ls` | left shooter flywheel motor |
| `RIGHT_SHOOTER` | `rs` | right shooter flywheel motor |
| `INTAKE` | `intake` | intake motor |
| `TRIGGER_SERVO` | `ts` | trigger/feed continuous rotation servo |
| `TRIGGER_MOTOR` | `tm` | trigger/feed motor |
| `SHOOTER_PANEL` | `panel` | shooter angle/panel servo |
| `LEFT_GATE` | `lg` | left gate servo |
| `RIGHT_GATE` | `rg` | right gate servo |

If a teammate changes a hardware name on the robot configuration screen, the matching name must also be changed in `RobotConfig.java`.

## 4. Robot Subsystems

The code uses a subsystem style. A subsystem is a Java class that controls one part of the robot.

The wrapper class is:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/Robot.java
```

It creates:

```java
public Drivetrain drivetrain = new Drivetrain();
public Intake intake = new Intake();
public Shooter shooter = new Shooter();
```

This lets opmodes use simple calls like:

```java
robot.drivetrain.driveCenterWithAutoAim(...);
robot.intake.intakeIn();
robot.shooter.triggerFire();
```

Instead of putting all motor and servo code directly in the opmode.

## 5. Drivetrain

File:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/Drivetrain.java
```

The drivetrain controls:

- four mecanum drive motors,
- goBILDA Pinpoint odometry,
- field-oriented driving,
- auto-aim rotation.

### Initialization

During `init(...)`, the drivetrain:

1. Gets the four drive motors from the hardware map.
2. Gets the Pinpoint sensor from the hardware map.
3. Reverses the left-side drive motors.
4. Sets all drive motors to brake when power is zero.
5. Configures Pinpoint offsets, encoder resolution, and encoder directions.
6. Resets Pinpoint position and IMU.

This part is important because if motor direction or odometry direction is wrong, the robot may drive or localize incorrectly.

### Mecanum Driving

The robot uses mecanum wheels, so it can:

- drive forward/backward,
- strafe left/right,
- rotate,
- combine those movements at the same time.

The drivetrain reads gamepad sticks:

- left stick: movement direction,
- right stick x-axis: rotation.

Then it calculates the power for each wheel.

### Field-Oriented Driving

Field-oriented driving means pushing the stick forward drives toward the field's forward direction, not necessarily the robot's front.

To do this, the code uses the robot heading from Pinpoint:

```java
pinPoint.update();
pinPoint.getPosition().getHeading(...)
```

The joystick direction is rotated by the robot's current heading before motor powers are calculated.

### Auto-Aim

The most important teleop drive method is:

```java
driveCenterWithAutoAim(Gamepad gamepad1, double p, boolean autoAimActive, boolean reverse)
```

When auto-aim is off, it drives normally.

When auto-aim is on:

1. The code reads the robot's current position from Pinpoint.
2. It calculates the angle from the robot to the target goal.
3. It adds a 90 degree offset because the shooter is mounted sideways relative to the robot.
4. It calculates the heading error.
5. It uses a PD controller to rotate the robot toward the target.
6. It still allows a little manual right-stick rotation.

Auto-aim is considered complete when the robot is within 3 degrees of the target heading:

```java
private static final double AIM_THRESHOLD = 3.0;
```

The method returns an `AimResult`, which contains:

- whether auto-aim is active,
- whether aiming is complete,
- current heading error,
- target heading.

Teleop uses this for telemetry and controller rumble.

## 6. Intake

File:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/Intake.java
```

The intake subsystem is simple. It controls one motor.

Methods:

```java
intakeIn()       // full power inward
intakeInAuto()   // 0.9 power inward for autonomous
intakeIn(p)      // custom inward power
intakeOut()      // reverse
intakeStop()     // stop motor
```

Teleop and autonomous call these methods instead of directly setting motor power.

## 7. Shooter

File:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/Shooter.java
```

The shooter controls:

- two flywheel motors,
- one trigger/feed motor,
- one trigger/feed CR servo,
- shooter panel servo,
- left and right gate servos.

### Flywheels

The shooter uses motor velocity control:

```java
setShooterVelocity(double velocity)
```

This sets both shooter motors to a target encoder velocity using PIDF constants from `RobotConstants.java`.

The common shooter speeds are:

```java
SHOOT_VELOCITY_NER_1 = 1940;
SHOOT_VELOCITY_NER_2 = 2200;
SHOOT_VELOCITY_FAR = 2500;
```

`NER` appears to mean near shot.

### Shooter Panel

The panel servo changes the shooter angle:

```java
panelTo(double pos)
```

Common panel positions:

```java
PANEL_NER_1 = 0.68;
PANEL_NER_2 = 0.84;
PANEL_FAR = 0.9;
```

### Gates and Trigger

The gates control whether game pieces can enter the shooter.

Important methods:

```java
openGate()
closeGate()
triggerFire()
triggerFireFar()
triggerPut()
triggerHold()
triggerSlow()
```

`triggerFire()` opens the gates and runs the trigger/feed system forward.

`triggerHold()` stops the trigger/feed system and closes the gates.

`triggerPut()` reverses the trigger/feed system.

## 8. TeleOp Code

Teleop files are in:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/teleOp
```

Current teleop opmodes:

```text
A_Tele.java
A_RED_Tele.java
A_BLUE_Tele.java
AB_Tele.java
AB_RED_Tele.java
AB_BLUE_Tele.java
```

The main difference is which gamepad controls which functions and whether the opmode sets red or blue target coordinates.

### Basic TeleOp Loop

Teleop classes extend `LinearOpMode`. Their main method is:

```java
public void runOpMode()
```

The usual structure is:

```java
robot.init(hardwareMap);
waitForStart();

while (opModeIsActive()) {
    // read gamepads
    // drive robot
    // run intake/shooter
    // update telemetry
}
```

This loop runs over and over until the opmode stops.

### AB TeleOp Controls

`AB_Tele.java` is a good file to study because it separates driver and operator controls.

Driver, `gamepad1`:

| Control | Action |
| --- | --- |
| Left stick | drive translation |
| Right stick x | rotate |
| Left bumper | hold for auto-aim |
| Right trigger | slow feed/intake in |
| Left trigger | intake out and reverse trigger |
| D-pad up | reset Pinpoint pose to manual position |

Operator, `gamepad2`:

| Control | Action |
| --- | --- |
| X | near shot preset 1 |
| Y | near shot preset 2 |
| B | far shot preset |
| D-pad up/down | increase/decrease shooter velocity by 40 |
| D-pad left/right | decrease/increase panel position by 0.04 |
| Left bumper press | toggle shooter flywheels on/off |
| Right bumper | fire |
| A | reverse trigger servo while intaking |

### Telemetry

Teleop sends useful data to the Driver Station:

- target shooter velocity,
- actual left/right shooter velocity,
- panel servo position,
- whether auto-aim is on,
- robot X/Y position,
- heading error,
- target heading,
- aim status,
- target goal position.

This is useful for debugging during practice.

## 9. Autonomous Code

Autonomous files are in:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/auto
```

Current autonomous opmodes:

```text
AutoRedNear12.java
AutoRedNear15.java
AutoRedFar9.java
AutoBlueNear12.java
AutoBlueNear15.java
AutoBlueFar9.java
```

The names describe:

- alliance color: red or blue,
- start side: near or far,
- expected number of points/pieces/score target in the routine name.

### Autonomous Style

Autonomous uses two libraries:

- **Pedro Pathing** for following paths on the field.
- **FTCLib Command Scheduler** for sequencing robot actions.

An autonomous routine usually does this:

1. Create a `Follower`.
2. Set the starting pose.
3. Initialize intake and shooter.
4. During Init, draw the robot/path for telemetry.
5. On Start, schedule a `SequentialCommandGroup`.
6. The scheduler runs commands in order.

Example pattern:

```java
CommandScheduler.getInstance().schedule(
    new SequentialCommandGroup(
        new InstantCommand(() -> robot.shooter.setShooterVelocity(...)),
        new DrivePointToPoint(follower, START, SHOOT),
        new InstantCommand(() -> robot.shooter.triggerFire()),
        new WaitCommand(850),
        new InstantCommand(() -> robot.shooter.triggerHold())
    )
);
```

### Movement Commands

Autonomous movement uses:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/commands/DrivePointToPoint.java
```

This command tells Pedro Pathing to drive from one pose to another.

It can create:

- a straight line path from start to end,
- a curved path with one midpoint,
- a curved path with two midpoints.

Examples:

```java
new DrivePointToPoint(follower, RED_NER_START, RED_NER_SHOOT_1)
```

Straight path.

```java
new DrivePointToPoint(follower, RED_NER_INTAKE_2, RED_NER_GATE_MID_1, RED_NER_SHOOT_2)
```

Curved path through a midpoint.

```java
new DrivePointToPoint(follower, start, mid1, mid2, end)
```

Curved path through two midpoints.

### Field Positions

Autonomous field positions are stored in:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants/AutoConstants.java
```

These are `Pose` objects:

```java
new Pose(x, y, heading)
```

The field is treated as a coordinate plane, usually in inches.

Red positions are defined directly. Blue positions are often mirrored using:

```java
144 - redX
```

because an FTC field is 144 inches wide.

### Example: Red Near 15

`AutoRedNear15.java` does roughly this:

1. Start at `RED_NER_START`.
2. Spin up shooter.
3. Drive to first shooting pose.
4. Fire preload.
5. Drive to collect the first group.
6. Return and shoot.
7. Drive to collect the second group.
8. Return and shoot.
9. Move to gate/suck positions to collect extra pieces.
10. Return and shoot.
11. Collect third group.
12. Return and shoot.
13. Stop intake and shooter.
14. Park at `RED_NER_PARK`.

The exact timing is controlled with `WaitCommand(...)` calls between mechanism actions.

## 10. How Autonomous Hands Off to TeleOp

One important detail: autonomous sets several values that teleop later uses.

These values live in:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants/RobotConstants.java
```

Important handoff variables:

```java
autoEndX
autoEndY
autoEndH
teleOpTargetX
teleOpTargetY
teleOpRev
MANUAL_POS
```

At the start of autonomous, each auto routine sets these based on alliance and path.

Then teleop does:

```java
robot.drivetrain.pinPoint.setPosition(
    new Pose2D(DistanceUnit.INCH, autoEndX, autoEndY, AngleUnit.RADIANS, autoEndH)
);
```

That means teleop begins with the robot position where autonomous expects it to have ended.

This is important for auto-aim. If the robot pose is wrong, auto-aim will point to the wrong angle.

## 11. Pedro Pathing Setup

Pedro Pathing constants are in:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/pedroPathing/Constants.java
```

This file configures:

- robot mass,
- drive motor names,
- motor directions,
- maximum power,
- x/y velocity estimates,
- PIDF values for path following,
- Pinpoint localizer settings,
- odometry pod offsets.

Most teammates should not change this file casually. Small changes here can affect every autonomous path.

## 12. Constants and Tuning

There are three important constants files.

### `RobotConfig.java`

Hardware names.

Change this only when the robot configuration names change.

### `RobotConstants.java`

Teleop target positions, shooter velocities, panel positions, and shooter PIDF values.

This is the file to check when tuning:

- shooter velocity,
- panel angle,
- teleop target goal position,
- auto-to-teleop handoff pose.

### `AutoConstants.java`

Autonomous path positions.

This is the file to check when tuning:

- starting positions,
- shooting positions,
- intake positions,
- gate/suck positions,
- parking positions.

## 13. What To Edit For Common Tasks

### Change a Driver Control

Edit files in:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/teleOp
```

Look for `gamepad1` or `gamepad2`.

Example:

```java
if (gamepad2.right_bumper) {
    robot.intake.intakeIn();
    robot.shooter.triggerFire();
}
```

### Change Shooter Speed

Edit:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants/RobotConstants.java
```

Change:

```java
SHOOT_VELOCITY_NER_1
SHOOT_VELOCITY_NER_2
SHOOT_VELOCITY_FAR
```

### Change Shooter Angle

Edit:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants/RobotConstants.java
```

Change:

```java
PANEL_NER_1
PANEL_NER_2
PANEL_FAR
```

### Change Autonomous Path Positions

Edit:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants/AutoConstants.java
```

Then update the matching auto file if the sequence also needs to change.

### Change Motor or Servo Names

Edit:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants/RobotConfig.java
```

Also make sure the Driver Station robot configuration uses the same names.

### Change Auto-Aim Target

Edit:

```text
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/constants/RobotConstants.java
```

Target variables:

```java
teleOpTargetXR
teleOpTargetYR
teleOpTargetXB
teleOpTargetYB
```

Red and blue teleop/autonomous files choose which target to use.

## 14. Important Things To Be Careful With

### Gamepad `WasPressed` vs Direct Button Checks

The code uses both styles:

```java
gamepad2.xWasPressed()
```

and:

```java
gamepad2.right_bumper
```

`xWasPressed()` is true only once when the button is first pressed. This is good for toggles and presets.

`right_bumper` is true the whole time the button is held. This is good for actions like firing while held.

Use the right style for the behavior you want.

### Shooter Toggle

In `AB_Tele`, the shooter is toggled with:

```java
gamepad2.leftBumperWasPressed()
```

That is correct because one press should turn the shooter on or off.

Do not use direct `gamepad2.left_bumper` for a toggle unless you also add edge detection, or it may toggle many times per second.

### Odometry Pose Must Be Correct

Auto-aim depends on Pinpoint position. If the pose is wrong, the heading to the target will be wrong.

Things that can make pose wrong:

- starting the robot in the wrong spot,
- wrong `AutoConstants` start pose,
- wrong Pinpoint pod offsets,
- wrong encoder directions,
- robot gets hit or slips,
- teleop starts without a good `autoEndX/Y/H`.

The d-pad up reset in teleop can set the robot back to `MANUAL_POS`.

### Motor Directions Matter

Left drive motors are reversed in drivetrain setup. The left shooter motor is reversed in shooter setup.

If a motor is changed physically or wired differently, check direction before changing path or drive math.

### Autonomous Timing Matters

`WaitCommand(...)` values are in milliseconds.

Example:

```java
new WaitCommand(850)
```

means wait 0.85 seconds.

These waits control how long the robot feeds balls, waits for mechanisms, or pauses before the next path.

## 15. How To Read The Code When Debugging

When something does not work, follow the call path from the opmode down to the subsystem.

Example: shooter does not fire in teleop.

1. Open `AB_Tele.java`.
2. Find the button that should fire:

   ```java
   if (gamepad2.right_bumper) {
       robot.intake.intakeIn();
       robot.shooter.triggerFire();
   }
   ```

3. Open `Shooter.java`.
4. Find `triggerFire()`:

   ```java
   triggerServo.setPower(-1);
   triggerMotor.setPower(1);
   openGate();
   ```

5. Check whether the trigger servo, trigger motor, and gate servos move.
6. If one does not move, check hardware name, wiring, direction, or servo position.

Example: robot drives wrong during auto.

1. Open the selected auto file.
2. Find the `DrivePointToPoint` command that is running.
3. Check the poses in `AutoConstants.java`.
4. Check Pedro setup in `pedroPathing/Constants.java`.
5. Check Pinpoint readings in telemetry.

## 16. Suggested Learning Order

For a new teammate, read the code in this order:

1. `RobotConfig.java` to learn hardware names.
2. `Robot.java` to see the main subsystem layout.
3. `Intake.java` because it is the simplest subsystem.
4. `Shooter.java` to learn motor/servo helper methods.
5. `AB_Tele.java` to see how gamepad buttons call subsystem methods.
6. `Drivetrain.java` to understand drive math and auto-aim.
7. `AutoConstants.java` to understand field poses.
8. `DrivePointToPoint.java` to understand autonomous movement commands.
9. One auto file, such as `AutoRedNear15.java`, to see a full autonomous routine.

## 17. Mental Model

Think of the code in layers:

```text
Driver Station selects an opmode
        |
        v
TeleOp or Autonomous file runs
        |
        v
OpMode calls subsystem methods
        |
        v
Subsystems control motors, servos, and sensors
        |
        v
Robot moves, intakes, aims, and shoots
```

Most changes should happen at the highest layer possible.

If you are changing driver controls, edit teleop.

If you are changing a mechanism behavior, edit the subsystem.

If you are changing tuning values, edit constants.

If you are changing autonomous strategy, edit the autonomous sequence and path constants.

