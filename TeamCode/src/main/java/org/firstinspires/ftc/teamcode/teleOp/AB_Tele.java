package org.firstinspires.ftc.teamcode.teleOp;

import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp
public class AB_Tele extends LinearOpMode {
    Robot robot = new Robot();
    boolean shooterOn = true;
    double velocity = SHOOT_VELOCITY_NER_1, panel = PANEL_NER_1;
    boolean rumbled = false;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            if (getRuntime() >= 90.0 && !rumbled) {
                gamepad1.rumble(500);
                rumbled = true;
            }

            robot.drivetrain.driveCenter(gamepad1, 1);

            if (gamepad1.right_trigger > 0.1) {
                robot.shooter.reverseTriggerServo();
                robot.shooter.setTriggerMotor();
                robot.intake.intakeIn(gamepad1.right_trigger);
            } else if (gamepad1.left_trigger > 0.1) {
                robot.intake.intakeOut();
                robot.shooter.triggerPut();
            }

            if (gamepad2.xWasPressed()) {
                panel = PANEL_NER_1;
                velocity = SHOOT_VELOCITY_NER_1;
            } else if (gamepad2.yWasPressed()) {
                panel = PANEL_NER_2;
                velocity = SHOOT_VELOCITY_NER_2;
            } else if (gamepad2.bWasPressed()) {
                panel = PANEL_FAR;
                velocity = SHOOT_VELOCITY_FAR;
            }

            if (gamepad2.leftBumperWasPressed()) {
                shooterOn = !shooterOn;
            }
            if (shooterOn) {
                robot.shooter.setShooterVelocity(velocity);
            } else {
                robot.shooter.shooterStop();
            }

            robot.shooter.panelTo(panel);

            if (gamepad2.right_bumper) {
                robot.intake.intakeIn();
                robot.shooter.triggerFire();
            } else if (gamepad2.a) {
                robot.shooter.reverseTriggerServo();
                robot.intake.intakeIn();
            }

            if (!gamepad2.a && !gamepad2.right_bumper && gamepad1.right_trigger <= 0.1) {
                robot.intake.intakeStop();
                robot.shooter.triggerHold();
            }

            telemetry.addData("target velocity", velocity);
            telemetry.addData("left velocity", robot.shooter.getLeftVelocity());
            telemetry.addData("right velocity", robot.shooter.getRightVelocity());
            telemetry.addData("panel", panel);
            telemetry.update();
        }
    }
}
