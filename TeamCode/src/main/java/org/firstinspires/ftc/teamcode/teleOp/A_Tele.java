package org.firstinspires.ftc.teamcode.teleOp;

import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp
public class A_Tele extends LinearOpMode {
    Robot robot = new Robot();
    boolean shooterOn = true;
    double velocity = SHOOT_VELOCITY_NER_1, panel = PANEL_NER_1;
    double distance;
    int turretTargetHeading = 0;
    double targetATAN, turretCurrentHeading;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, autoEndY, 144 - autoEndX, AngleUnit.RADIANS, autoEndH + Math.PI / 2.0));

        waitForStart();

        robot.drivetrain.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, autoEndY, 144 - autoEndX, AngleUnit.RADIANS, autoEndH + Math.PI / 2.0));

        while (opModeIsActive()) {
            Pose2D current = robot.drivetrain.getPosition();
            turretCurrentHeading = current.getHeading(AngleUnit.DEGREES);
            targetATAN = Math.toDegrees(Math.atan2((teleOpTargetY - current.getY(DistanceUnit.INCH)), (teleOpTargetX - current.getX(DistanceUnit.INCH))));
            if (Math.abs(targetATAN - turretCurrentHeading) <= 90) {
                turretTargetHeading = (int) -(targetATAN - turretCurrentHeading);
            } else {
                turretTargetHeading = 0;
            }
            distance = Math.abs(Math.hypot(teleOpTargetY - current.getY(DistanceUnit.INCH), teleOpTargetX - current.getX(DistanceUnit.INCH)));

            robot.drivetrain.driveConstantOriented(gamepad1, gamepad1.dpad_left);

            if (gamepad1.right_trigger > 0) {
                robot.intake.intakeIn();
            } else if (gamepad1.left_trigger > 0) {
                robot.intake.intakeOut();
                robot.shooter.triggerHold();
            } else {
                robot.intake.intakeStop();
                robot.shooter.triggerHold();
            }

            // 近点 1 位置发射设置
            if (gamepad1.xWasPressed()) {
                panel = PANEL_NER_1;
                velocity = SHOOT_VELOCITY_NER_1;
            }
            // 近点 2 位置发射设置
            else if (gamepad1.yWasPressed()) {
                panel = PANEL_NER_2;
                velocity = SHOOT_VELOCITY_NER_2;
            } else if (gamepad1.bWasPressed()) {
                panel = PANEL_FAR;
                velocity = SHOOT_VELOCITY_FAR;
            }

            if (gamepad1.leftBumperWasPressed()) shooterOn = !shooterOn;
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
            telemetry.addData("distance", distance);
            telemetry.addData("targetATAN", targetATAN);
            telemetry.addData("turretCurrentHeading", turretCurrentHeading);
            telemetry.addData("turretTargetHeading", turretTargetHeading);
            telemetry.addData("current", robot.drivetrain.getPosition());

            telemetry.update();
        }
    }
}
