

# FTC Team 19682 - 2025-2026 Season Code Repository

## Overview

This repository contains the complete robot code for FTC Team 19682 for the 2025-2026 season. The project is built using the FTC SDK and implements a sophisticated path-following autonomous system using the pedroPathing library.

## Robot Architecture

The robot uses a four-wheel mecanum drivetrain with GoBilda Pinpoint odometry for precise field-relative navigation. The codebase follows a subsystem-command pattern, organizing robot functionality into modular components:

- **Drivetrain**: Field-oriented mecanum drive with heading stabilization and auto-aim capabilities
- **Intake**: Motorized intake with dual continuous rotation servos for game piece manipulation
- **Shooter**: Dual-motor shooter with trigger mechanism and adjustable panel positioning
- **Follower**: PedroPathing-based path following system for autonomous navigation

## Autonomous System

The autonomous system supports multiple starting positions and strategies:

- **Blue Far (Position 9)**: Far side autonomous with shooting and multiple intake points
- **Blue Near (Position 12)**: Near side autonomous with optimized intake sequence
- **Red Far (Position 9)**: Far side red alliance autonomous
- **Red Near (Position 12)**: Near side red alliance autonomous

All autonomous routines utilize the Follower subsystem for smooth path execution with configurable hold-end behavior and breaking strength.

## TeleOp Programs

Two teleoperated programs are provided with identical functionality:

- **Tele1**: Standard driver-controlled operation with shooter velocity control and trigger management
- **Tele2**: Alternative teleop implementation

Key controls include shooter on/off toggle, velocity adjustment, and trigger fire/hold functions.

## Path Tuning

The Tuning OpMode provides comprehensive calibration tools for the path following system:

- **LocalizationTest**: Verify odometry accuracy
- **ForwardTuner**: Calibrate forward movement
- **LateralTuner**: Calibrate strafing
- **TurnTuner**: Tune turning accuracy
- **VelocityTuner**: Measure velocity profiles
- **AccelerationTuner**: Zero-power deceleration calibration
- **ShapeTuners**: Geometric path testing (line, circle, triangle)

## Project Structure

```
TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
├── auto/                    # Autonomous OpModes
│   ├── AutoBlueFar.java
│   ├── AutoBlueNear.java
│   ├── AutoRedFar.java
│   └── AutoRedNear.java
├── commands/               # Command implementations
│   └── DrivePointToPoint.java
├── constants/              # Configuration constants
│   ├── AutoConstants.java
│   ├── RobotConfig.java
│   └── RobotConstants.java
├── pedroPathing/          # Path following system
│   ├── Constants.java
│   └── Tuning.java
├── subsystems/            # Robot subsystems
│   ├── Drivetrain.java
│   ├── Follower.java
│   ├── Intake.java
│   ├── Robot.java
│   ├── Shooter.java
│   └── Drawing.java
└── teleOp/                # Teleoperated modes
    ├── Tele1.java
    └── Tele2.java
```

## Setup Requirements

- Android Studio (latest stable version)
- FTC SDK 9.2 or later
- Java 17 or later
- Android device with FTC Robot Controller app installed

## Installation

1. Clone this repository
2. Open in Android Studio as an existing Android project
3. Ensure proper SDK and Gradle configurations
4. Build and deploy to robot controller device

## Configuration

Robot hardware configuration is managed through RobotConfig.java with device names for:
- Drivetrain motors (left/right front and back)
- Pinpoint odometry sensor
- Shooter motors (left and right)
- Intake motor and servos
- Trigger motor and servo
- Shooter panel servo

Autonomous poses are defined in AutoConstants.java for each alliance and starting position, including intake approach positions, shooting locations, and parking waypoints.