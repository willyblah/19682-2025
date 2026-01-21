package org.firstinspires.ftc.teamcode.teleOp;

import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp
public class Tele2 extends LinearOpMode {
    Robot robot = new Robot();
    boolean shooterOn = false;
    double velocity = 2000, panel = 0;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {
            robot.drivetrain.driveCenter(gamepad1, 1);

            if (gamepad1.right_trigger > 0) {
                robot.intake.intakeIn();
            } else if (gamepad1.left_trigger > 0) {
                robot.intake.intakeOut();
            } else {
                robot.intake.intakeStop();
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
                robot.shooter.triggerPut();
                robot.intake.intakeIn();
            } else {
                robot.intake.intakeStop();
                robot.shooter.triggerHold();
            }

            telemetry.addData("target velocity", velocity);
            telemetry.addData("current velocity", robot.shooter.getShooterVelocity());
            telemetry.addData("panel", panel);
            telemetry.update();
        }
    }
}
