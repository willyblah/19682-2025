package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.RobotConfig.*;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Drivetrain {

    public static class AimResult {
        public boolean isAiming;
        public boolean aimCompleted;
        public double headingError;
        public double targetHeading;

        public AimResult(boolean isAiming, boolean aimCompleted, double headingError, double targetHeading) {
            this.isAiming = isAiming;
            this.aimCompleted = aimCompleted;
            this.headingError = headingError;
            this.targetHeading = targetHeading;
        }
    }

    // 瞄准相关常量
    private static final double AIM_THRESHOLD = 3.0; // 瞄准完成阈值（度）
    private static final double AIM_SMOOTH_FACTOR = 0.1; // 瞄准平滑因子
    private DcMotorEx leftFront = null;
    private DcMotorEx leftBack = null;
    private DcMotorEx rightFront = null;
    private DcMotorEx rightBack = null;
    public GoBildaPinpointDriver pinPoint;
    private double theta, power, turn, realTheta;

    // PID coefficients for heading control
    private final double headingKp = 0.04;
    private final double headingKd = 0.004;

    // PID state variables
    private double headingLastError = 0.0;
    private long headingLastTime = 0;
    double fixedFieldHeading = 0.0;

    public void init(HardwareMap hardwareMap) {
        pinPoint = hardwareMap.get(GoBildaPinpointDriver.class, PIN_POINT);
        leftFront = hardwareMap.get(DcMotorEx.class, LEFT_FRONT);
        leftBack = hardwareMap.get(DcMotorEx.class, LEFT_BACK);
        rightFront = hardwareMap.get(DcMotorEx.class, RIGHT_FRONT);
        rightBack = hardwareMap.get(DcMotorEx.class, RIGHT_BACK);

        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftBack.setDirection(DcMotorEx.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        pinPoint.setOffsets(-45.5, -50, DistanceUnit.MM);
        pinPoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinPoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);

        pinPoint.resetPosAndIMU();
    }

    public void drive(Gamepad gamepad, double powerScale) {
        double x = -gamepad.left_stick_y, y = -gamepad.left_stick_x, rx = gamepad.right_stick_x * 0.85;
        leftFront.setPower((y + x + rx) * powerScale);
        leftBack.setPower((y - x + rx) * powerScale);
        rightFront.setPower((y - x - rx) * powerScale);
        rightBack.setPower((y + x - rx) * powerScale);
    }

    public double getHeading() {
        return pinPoint.getPosition().getHeading(AngleUnit.DEGREES);
    }

    public void driveFieldOriented(Gamepad gamepad, double p) {
        double y = -gamepad.left_stick_y, x = gamepad.left_stick_x, rx = gamepad.right_stick_x * 0.85;
        pinPoint.update();
        theta = Math.atan2(y, x) * 180 / Math.PI;
        power = Math.hypot(x, y);
        turn = rx;

        realTheta = (360 - pinPoint.getPosition().getHeading(AngleUnit.DEGREES)) + theta;

        double sin = Math.sin((realTheta * (Math.PI / 180)) - (Math.PI / 4));
        double cos = Math.cos((realTheta * (Math.PI / 180)) - (Math.PI / 4));
        double maxSinCos = Math.max(Math.abs(sin), Math.abs(cos));

        double leftFrontPower = (power * cos / maxSinCos + turn);
        double rightFrontPower = (power * sin / maxSinCos - turn);
        double leftBackPower = (power * sin / maxSinCos + turn);
        double rightBackPower = (power * cos / maxSinCos - turn);

        leftFront.setPower(leftFrontPower * p);
        rightFront.setPower(rightFrontPower * p);
        leftBack.setPower(leftBackPower * p);
        rightBack.setPower(rightBackPower * p);
    }

    public void driveConstantOriented(Gamepad gamepad, boolean chassisAutoAim) {
        double x = -gamepad.left_stick_y, y = -gamepad.left_stick_x, rx = gamepad.right_stick_x * 0.65;
        pinPoint.update();

        theta = Math.atan2(y, x) * 180 / Math.PI;
        power = Math.hypot(x, y);
        turn = rx; // Fix without testing: autoaim now stopped blocking rightstick

        if (chassisAutoAim) {
            Pose2D current = pinPoint.getPosition();
            double currentHeading, targetHeading, headingError;
            currentHeading = current.getHeading(AngleUnit.DEGREES);
            targetHeading = Math.toDegrees(Math.atan2(teleOpTargetY - current.getY(DistanceUnit.INCH), teleOpTargetX - current.getX(DistanceUnit.INCH))) + Math.PI / 2.0;

            headingError = (currentHeading - targetHeading + 180) % 360 - 180; // normalize heading to -180 ~ +180

            // calculate PID for heading control
            long currentTime = System.nanoTime();
            double deltaTime = (headingLastTime == 0) ? 0.02 : (currentTime - headingLastTime) / 1e9; // convert to seconds

            headingLastTime = currentTime;
            double proportional = headingKp * headingError;
            double derivative = headingKd * ((headingError - headingLastError) / deltaTime);
            headingLastError = headingError;

            turn += (proportional + derivative);
        }

        realTheta = fixedFieldHeading + theta;
        realTheta = realTheta % 360;
        if (realTheta < 0) realTheta += 360;

        double sin = Math.sin((realTheta * (Math.PI / 180)) - (Math.PI / 4));
        double cos = Math.cos((realTheta * (Math.PI / 180)) - (Math.PI / 4));
        double maxSinCos = Math.max(Math.abs(sin), Math.abs(cos));

        double leftFrontPower = (power * cos / maxSinCos + turn);
        double rightFrontPower = (power * sin / maxSinCos - turn);
        double leftBackPower = (power * sin / maxSinCos + turn);
        double rightBackPower = (power * cos / maxSinCos - turn);

        if (gamepad.right_bumper) {
            leftFront.setPower(leftFrontPower * 0.3);
            rightFront.setPower(rightFrontPower * 0.3);
            leftBack.setPower(leftBackPower * 0.3);
            rightBack.setPower(rightBackPower * 0.3);
        } else {
            leftFront.setPower(leftFrontPower * 0.8);
            rightFront.setPower(rightFrontPower * 0.8);
            leftBack.setPower(leftBackPower * 0.8);
            rightBack.setPower(rightBackPower * 0.8);
        }

    }

    public void driveCenter(Gamepad gamepad, double p) {
        double y = -gamepad.left_stick_y, x = gamepad.left_stick_x, rx = gamepad.right_stick_x * 0.85;
        pinPoint.update();
        theta = Math.atan2(y, x) * 180 / Math.PI;
        power = Math.hypot(x, y);
        turn = rx / 0.6;

        realTheta = (360 - pinPoint.getPosition().getHeading(AngleUnit.DEGREES)) + theta;

        double sin = Math.sin((realTheta * (Math.PI / 180)) - (Math.PI / 4));
        double cos = Math.cos((realTheta * (Math.PI / 180)) - (Math.PI / 4));
        double maxSinCos = Math.max(Math.abs(sin), Math.abs(cos));

        double leftFrontPower = (power * cos / maxSinCos + turn);
        double rightFrontPower = (power * sin / maxSinCos - turn);
        double leftBackPower = (power * sin / maxSinCos + turn);
        double rightBackPower = (power * cos / maxSinCos - turn);

        leftFront.setPower(leftFrontPower * p / .9);
        rightFront.setPower(rightFrontPower * p / .9);
        leftBack.setPower(leftBackPower * p / .9);
        rightBack.setPower(rightBackPower * p / .9);
    }

    public AimResult driveCenterWithAutoAim(Gamepad gamepad1, double p, boolean autoAimActive, boolean reverse) {
        double y = -gamepad1.left_stick_y, x = gamepad1.left_stick_x, rx = gamepad1.right_stick_x * 0.85;
        if (reverse){
            y=-y;
            x=-x;
        }
        pinPoint.update();
        theta = Math.atan2(y, x) * 180 / Math.PI;
        power = Math.hypot(x, y);

        // 初始化瞄准结果
        boolean aimCompleted = false;
        double headingError = 0.0;
        double targetHeading = 0.0;

        if (autoAimActive) {
            // 自动瞄准模式
            Pose2D current = pinPoint.getPosition();
            double currentHeading = current.getHeading(AngleUnit.DEGREES);

            // 计算到目标的角度
            // 1. 计算炮台应该朝向的角度（直接 atan2，不需要 +180）
            double desiredShooterHeading = Math.toDegrees(Math.atan2(teleOpTargetY - current.getY(DistanceUnit.INCH),
                    teleOpTargetX - current.getX(DistanceUnit.INCH)));

            // 2. 计算机器人应该朝向的角度（炮台偏移补偿）
            targetHeading = desiredShooterHeading + 90; // 炮台在右侧，所以机器人要多转90度

            // 3. 标准化到 0-360 度
            targetHeading = (targetHeading + 360) % 360;

            // 标准化角度到 -180 到 180 度
            headingError = currentHeading - targetHeading;
            while (headingError > 180) headingError -= 360;
            while (headingError <= -180) headingError += 360;

            // 检查瞄准是否完成
            aimCompleted = Math.abs(headingError) < AIM_THRESHOLD;

            // 使用PID控制进行自动转向
            long currentTime = System.nanoTime();
            double deltaTime = (headingLastTime == 0) ? 0.02 : (currentTime - headingLastTime) / 1e9;
            headingLastTime = currentTime;

            double proportional = headingKp * headingError;
            double derivative = headingKd * ((headingError - headingLastError) / deltaTime);
            headingLastError = headingError;

            // 自动瞄准时的转向控制，手动转向输入减弱
            turn = (proportional + derivative) + (rx * 0.2); // 保留少量手动控制
        } else {
            // 普通driveCenter模式
            turn = rx / 0.6;
            // 重置PID状态
            headingLastError = 0.0;
            headingLastTime = 0;
        }

        realTheta = (360 - pinPoint.getPosition().getHeading(AngleUnit.DEGREES)) + theta;

        double sin = Math.sin((realTheta * (Math.PI / 180)) - (Math.PI / 4));
        double cos = Math.cos((realTheta * (Math.PI / 180)) - (Math.PI / 4));
        double maxSinCos = Math.max(Math.abs(sin), Math.abs(cos));

        double leftFrontPower = (power * cos / maxSinCos + turn);
        double rightFrontPower = (power * sin / maxSinCos - turn);
        double leftBackPower = (power * sin / maxSinCos + turn);
        double rightBackPower = (power * cos / maxSinCos - turn);

        leftFront.setPower(leftFrontPower * p / .9);
        rightFront.setPower(rightFrontPower * p / .9);
        leftBack.setPower(leftBackPower * p / .9);
        rightBack.setPower(rightBackPower * p / .9);

        // 返回瞄准结果
        return new AimResult(autoAimActive, aimCompleted, headingError, targetHeading);
    }

    public Pose2D getPosition() {
        pinPoint.update();
        return pinPoint.getPosition();
    }
}
