package org.firstinspires.ftc.teamcode.teleOp;

import static org.firstinspires.ftc.teamcode.constants.AutoConstants.*;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain.AimResult;

@TeleOp
public class AB_RED_Tele extends LinearOpMode {
    Robot robot = new Robot();
    boolean shooterOn = false;
    double cVelocity = 0, cAngle = 0;
    double velocity = SHOOT_VELOCITY_NER_1, panel = PANEL_NER_1;
    boolean rumbled = false;

    boolean intaking = false;
    boolean lastAimCompleted = false, hasRumbledForAim = false;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, 8, 8, AngleUnit.RADIANS, Math.PI / 2.0));

        waitForStart();
        robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, 8, 8, AngleUnit.RADIANS, Math.PI / 2.0));

        teleOpTargetX = teleOpTargetXR;
        teleOpTargetY = teleOpTargetYR;

        while (opModeIsActive()) {
            if (getRuntime() >= 95.0 && !rumbled) {
                gamepad1.rumble(500);
                rumbled = true;
            }

            boolean autoAimActive = gamepad1.left_bumper;
            AimResult aimResult = robot.drivetrain.driveCenterWithAutoAim(gamepad1, 1, autoAimActive, false);

            if (aimResult.aimCompleted && !lastAimCompleted && !hasRumbledForAim) {
                gamepad2.rumble(200);
                hasRumbledForAim = true;
                robot.shooter.openGate();
            } else if (!autoAimActive) {
                hasRumbledForAim = false;
            }
            lastAimCompleted = aimResult.aimCompleted;

            if (gamepad1.right_trigger > 0.1) {
                robot.shooter.closeGate();
                intaking = true;
//                robot.shooter.triggerSlow();
                robot.intake.intakeIn(gamepad1.right_trigger);
            } else if (gamepad1.left_trigger > 0.1) {
                robot.intake.intakeOut();
                robot.shooter.triggerPut();
            }
            else if (gamepad1.right_trigger <= 0.1) {
                robot.intake.intakeStop();
                intaking = false;
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

            if (gamepad1.dpad_up) {
                robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, RED_MANUAL_POS.getX(), RED_MANUAL_POS.getY(), AngleUnit.RADIANS, RED_MANUAL_POS.getHeading()));
            }

            if (gamepad2.dpadUpWasPressed()) {
                cVelocity += 40;
                if (cVelocity > 320){
                    cVelocity = 320;
                }
            } else if (gamepad2.dpadDownWasPressed()) {
                cVelocity -= 40;
                if (cVelocity < -320){
                    cVelocity = -320;
                }
            } else if (gamepad2.dpadLeftWasPressed()) {
                cAngle -= 0.04;
            } else if (gamepad2.dpadRightWasPressed()) {
                cAngle += 0.04;
            }

            if (gamepad2.leftBumperWasPressed()) {
                shooterOn = !shooterOn;
            }
            if (shooterOn) {
                robot.shooter.setShooterVelocity(velocity + cVelocity);
                if (!intaking){
                    robot.shooter.openGate();
                }
            } else {
                robot.shooter.shooterStop();
            }

            robot.shooter.panelTo(panel + cAngle);

            if (gamepad2.right_bumper) {
                if (velocity + cVelocity <= SHOOT_VELOCITY_NER_1){
                    robot.intake.intakeIn(0.6);
                }
                else if (velocity + cVelocity <= SHOOT_VELOCITY_NER_2){
                    robot.intake.intakeIn(0.8);
                }
                else{
                    robot.intake.intakeIn(1);
                }
                if (velocity == SHOOT_VELOCITY_FAR) robot.shooter.triggerFireFar();
                else robot.shooter.triggerFire();
            }

            if (!gamepad2.right_bumper && !(gamepad1.right_trigger > 0.1)
                    && !(gamepad1.left_trigger > 0.1)) {
                robot.intake.intakeStop();
                robot.shooter.triggerHold();
            }

            double leftVelocity = robot.shooter.getLeftVelocity();
            double rightVelocity = robot.shooter.getRightVelocity();

            telemetry.addData("target velocity", velocity);
            telemetry.addData("left velocity", leftVelocity);
            telemetry.addData("right velocity", rightVelocity);
            telemetry.addData("stable", abs((leftVelocity + rightVelocity) / 2 - velocity) < 60);
            telemetry.addData("panel", panel);

            telemetry.addData("自动瞄准", aimResult.isAiming ? "开启" : "关闭");
            telemetry.addData("Robot X/Y", String.format("%.1f/%.1f", robot.drivetrain.getPosition().getX(DistanceUnit.INCH), robot.drivetrain.getPosition().getY(DistanceUnit.INCH)));
            telemetry.addData("角度误差", "%.1f°", aimResult.headingError);
            telemetry.addData("目标角度", "%.1f°", aimResult.targetHeading);
            telemetry.addData("瞄准状态", aimResult.aimCompleted ? "已完成 ✓" : "瞄准中...");
            telemetry.addData("目标位置", "(%.1f, %.1f)", teleOpTargetX, teleOpTargetY);
            telemetry.addData("微调角度", cAngle);
            telemetry.addData("微调飞轮转速", cVelocity);

            telemetry.update();
        }
    }
}
