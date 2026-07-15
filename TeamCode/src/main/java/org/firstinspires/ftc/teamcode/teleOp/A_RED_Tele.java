package org.firstinspires.ftc.teamcode.teleOp;

import static org.firstinspires.ftc.teamcode.constants.AutoConstants.*;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain.AimResult;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp
public class A_RED_Tele extends LinearOpMode {
    Robot robot = new Robot();
    boolean shooterOn = false;
    double cVelocity = 0, cAngle = 0;
    double velocity = SHOOT_VELOCITY_NER_1 + cVelocity, panel = PANEL_NER_1 + cAngle;
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
            AimResult aimResult = robot.drivetrain.driveCenterWithAutoAim(gamepad1, 0.8, autoAimActive, false);

            if (aimResult.aimCompleted && !lastAimCompleted && !hasRumbledForAim) {
                hasRumbledForAim = true;
            } else if (!autoAimActive) {
                hasRumbledForAim = false;
            }
            lastAimCompleted = aimResult.aimCompleted;

            if (gamepad1.right_trigger > 0.1) {
                robot.shooter.closeGate();
                robot.shooter.triggerSlow();
                robot.intake.intakeIn(gamepad1.right_trigger);
                intaking = true;
            } else if (gamepad1.left_trigger > 0.1) {
                robot.intake.intakeOut();
                robot.shooter.triggerPut();
            }
            else if (gamepad1.right_trigger <= 0.1){
                intaking = false;
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

            if (gamepad1.touchpadWasPressed()) {
                robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, RED_MANUAL_POS.getX(), RED_MANUAL_POS.getY(), AngleUnit.RADIANS, RED_MANUAL_POS.getHeading()));
            }

            if (gamepad1.dpadUpWasPressed()) {
                cVelocity += 40;
            } else if (gamepad1.dpadDownWasPressed()) {
                cVelocity -= 40;
            } else if (gamepad1.dpadLeftWasPressed()) {
                cAngle -= 0.04;
            } else if (gamepad1.dpadRightWasPressed()) {
                cAngle += 0.04;
            }

            if (gamepad1.aWasPressed()) {
                shooterOn = !shooterOn;
                robot.shooter.openGate();
            }
            if (shooterOn) {
                robot.shooter.setShooterVelocity(velocity + cVelocity);
                if (!intaking){
                    robot.shooter.openGate();
                }
            } else {
                robot.shooter.shooterStop();
            }

            robot.shooter.panelTo(panel);

            if (gamepad1.right_bumper) {
                if (velocity <= SHOOT_VELOCITY_NER_1){
                    robot.intake.intakeIn(0.6);
                }
                else if (velocity <= SHOOT_VELOCITY_NER_2){
                    robot.intake.intakeIn(0.8);
                }
                else{
                    robot.intake.intakeIn(1);
                }
                if (velocity == SHOOT_VELOCITY_FAR) robot.shooter.triggerFireFar();
                else robot.shooter.triggerFire();
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
            telemetry.addData("微调角度", cAngle);
            telemetry.addData("微调飞轮转速", cVelocity);

            telemetry.update();
        }
    }
}
