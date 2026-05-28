package org.firstinspires.ftc.teamcode.teleOp;

import static org.firstinspires.ftc.teamcode.constants.AutoConstants.BLUE_MANUAL_POS;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.MANUAL_POS;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.PANEL_FAR;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.PANEL_NER_1;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.PANEL_NER_2;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.SHOOT_VELOCITY_FAR;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.SHOOT_VELOCITY_NER_1;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.SHOOT_VELOCITY_NER_2;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.autoEndH;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.autoEndX;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.autoEndY;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.teleOpRev;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.teleOpTargetX;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.teleOpTargetXB;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.teleOpTargetY;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.teleOpTargetYB;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain.AimResult;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp
public class A_BLUE_Tele extends LinearOpMode {
    Robot robot = new Robot();
    boolean shooterOn = true;
    double velocity = SHOOT_VELOCITY_NER_1, panel = PANEL_NER_1;
    boolean rumbled = false;

    boolean lastAimCompleted = false, hasRumbledForAim = false;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, 144 - 8, 8, AngleUnit.RADIANS, BLUE_MANUAL_POS.getHeading()));

        waitForStart();
        robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, 144 - 8, 8, AngleUnit.RADIANS, BLUE_MANUAL_POS.getHeading()));

        teleOpTargetX = teleOpTargetXB;
        teleOpTargetY = teleOpTargetYB;

        while (opModeIsActive()) {
            if (getRuntime() >= 95.0 && !rumbled) {
                gamepad1.rumble(500);
                rumbled = true;
            }

            boolean autoAimActive = gamepad1.left_bumper;
            AimResult aimResult = robot.drivetrain.driveCenterWithAutoAim(gamepad1, 1, autoAimActive, true);

            if (aimResult.aimCompleted && !lastAimCompleted && !hasRumbledForAim) {
                gamepad2.rumble(200);
                hasRumbledForAim = true;
            } else if (!autoAimActive) {
                hasRumbledForAim = false;
            }
            lastAimCompleted = aimResult.aimCompleted;

            if (gamepad1.right_trigger > 0.1) {
                robot.shooter.triggerSlow();
                robot.intake.intakeIn(gamepad1.right_trigger);
            } else if (gamepad1.left_trigger > 0.1) {
                robot.intake.intakeOut();
                robot.shooter.triggerPut();
            }

            if (gamepad1.xWasPressed()) {
                panel = PANEL_NER_1;
                velocity = SHOOT_VELOCITY_NER_1;
            } else if (gamepad1.yWasPressed()) {
                panel = PANEL_NER_2;
                velocity = SHOOT_VELOCITY_NER_2;
            } else if (gamepad1.bWasPressed()) {
                panel = PANEL_FAR;
                velocity = SHOOT_VELOCITY_FAR;
            }

            if (gamepad1.dpad_up) {
                robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, BLUE_MANUAL_POS.getX(), BLUE_MANUAL_POS.getY(), AngleUnit.RADIANS, BLUE_MANUAL_POS.getHeading()));
            }

            if (gamepad2.dpadUpWasPressed()) {
                velocity += 40;
            } else if (gamepad2.dpadDownWasPressed()) {
                velocity -= 40;
            } else if (gamepad2.dpadLeftWasPressed()) {
                panel -= 0.04;
            } else if (gamepad2.dpadRightWasPressed()) {
                panel += 0.04;
            }

            if (gamepad1.a) {
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
                if (velocity == SHOOT_VELOCITY_FAR) robot.shooter.triggerFireFar();
                else robot.shooter.triggerFire();
            } else if (gamepad2.a) {
                robot.shooter.reverseTriggerServo();
                robot.intake.intakeIn();
            }

            if (!gamepad1.right_bumper && !(gamepad1.right_trigger > 0.1)) {
                if (!(gamepad1.left_trigger > 0.1)) {
                    robot.intake.intakeStop();
                    robot.shooter.triggerHold();
                }
            }

            telemetry.addData("target velocity", velocity);
            telemetry.addData("left velocity", robot.shooter.getLeftVelocity());
            telemetry.addData("right velocity", robot.shooter.getRightVelocity());
            telemetry.addData("panel", panel);

            telemetry.addData("自动瞄准", aimResult.isAiming ? "开启" : "关闭");
            telemetry.addData("Robot X/Y", String.format("%.1f/%.1f", robot.drivetrain.getPosition().getX(DistanceUnit.INCH), robot.drivetrain.getPosition().getY(DistanceUnit.INCH)));
            telemetry.addData("角度误差", "%.1f°", aimResult.headingError);
            telemetry.addData("目标角度", "%.1f°", aimResult.targetHeading);
            telemetry.addData("瞄准状态", aimResult.aimCompleted ? "已完成 ✓" : "瞄准中...");
            telemetry.addData("目标位置", "(%.1f, %.1f)", teleOpTargetX, teleOpTargetY);

            telemetry.update();
        }
    }
}
