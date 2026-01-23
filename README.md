# FTC Team 19682 - 2025-2026 Season Code

## Overview

This repository contains the complete robot code for FTC Team 19682 for the 2025-2026 season. The project is built using the FTC SDK and implements a sophisticated path-following autonomous system using the Pedro Pathing library.

## Robot Architecture

The robot uses a four-wheel mecanum drivetrain with GoBilda Pinpoint odometry for precise field-relative navigation. The codebase follows a subsystem-command pattern, organizing robot functionality into modular components:

- **Drivetrain**: Field-oriented mecanum drive with heading stabilization and auto-aim capabilities
- **Intake**: Motorized intake with dual continuous rotation servos for artifact manipulation
- **Shooter**: Flywheel shooter with trigger mechanism and adjustable panel positioning
- **Follower**: PedroPathing-based path following system for autonomous navigation

## Autonomous System

The autonomous system supports multiple positions and strategies:

- Near (12 artifacts)
- Far (9 artifacts)

All autonomous programs utilize FTCLib’s command scheduler.

## TeleOp Programs

- **Tele1**: Single controller, robot-oriented driving, auto aim
- **Tele2**: Double controller, field-oriented driving, no auto aim

Key controls include shooter on/off toggle, velocity adjustment, and trigger fire/hold functions.