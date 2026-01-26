package org.firstinspires.ftc.teamcode.teleOp;

import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp
public class Test extends LinearOpMode {
    Robot robot = new Robot();
    boolean shooterOn = true;
    double velocity = SHOOT_VELOCITY_NER_1, panel = PANEL_NER_1;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {

            if (gamepad1.right_trigger > 0.2) {
                robot.shooter.reverseTriggerServo();
                robot.intake.intakeIn();
            } else if (gamepad1.left_trigger > 0.2) {
                robot.intake.intakeOut();
                robot.shooter.triggerHold();
            } else {
                robot.intake.intakeStop();
                robot.shooter.triggerHold();
            }

            if (gamepad1.dpadUpWasPressed()) {
                panel += 0.03;
            } else if (gamepad1.dpadDownWasPressed()) {
                panel -= 0.03;
            } else if (gamepad1.dpadLeftWasPressed()) {
                velocity -= 50;
            } else if (gamepad1.dpadRightWasPressed()) {
                velocity += 50;
            }

            if (gamepad1.leftBumperWasPressed()) {
                shooterOn = !shooterOn;
            }
            if (shooterOn) {
                robot.shooter.setShooterVelocity(velocity);
            } else {
                robot.shooter.shooterStop();
            }

            robot.shooter.panelTo(panel);

            if (gamepad1.right_bumper) {
                robot.intake.intakeIn();
                robot.shooter.triggerFire();
            } else {
                robot.intake.intakeStop();
                robot.shooter.triggerHold();
            }

            telemetry.addData("panel", panel);
            telemetry.addData("target velocity", velocity);
            telemetry.addData("left velocity", robot.shooter.getLeftVelocity());
            telemetry.addData("right velocity", robot.shooter.getRightVelocity());
            telemetry.update();
        }
    }
}
