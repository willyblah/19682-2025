package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.RobotConfig.*;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Shooter {
    public DcMotorEx leftShooter, rightShooter;
    private DcMotorEx triggerMotor;
    private CRServo triggerServo;
    private Servo shooterPanel;
    private Servo leftGate, rightGate;
    private ScheduledExecutorService exec;
    public boolean busy = false;
    private static final int SHOOT_TIME = 320;
    private static final int WAIT_TIME = 300;

    public void init(HardwareMap hardwareMap) {
        leftShooter = hardwareMap.get(DcMotorEx.class, LEFT_SHOOTER);
        rightShooter = hardwareMap.get(DcMotorEx.class, RIGHT_SHOOTER);
        triggerMotor = hardwareMap.get(DcMotorEx.class, TRIGGER_MOTOR);

        shooterPanel = hardwareMap.get(Servo.class, SHOOTER_PANEL);
        triggerServo = hardwareMap.get(CRServo.class, TRIGGER_SERVO);

        leftGate = hardwareMap.get(Servo.class, LEFT_GATE);
        rightGate = hardwareMap.get(Servo.class, RIGHT_GATE);
        leftGate.setDirection(Servo.Direction.REVERSE);

        leftShooter.setDirection(DcMotorSimple.Direction.REVERSE);

        leftShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        leftShooter.setVelocityPIDFCoefficients(1, 1, 1, 1);
//        rightShooter.setVelocityPIDFCoefficients(1, 1, 1, 1);

        exec = Executors.newScheduledThreadPool(5);
    }

    public void setShooterVelocity(double velocity) {
        leftShooter.setVelocity(velocity);
        rightShooter.setVelocity(velocity);
    }

    public double getLeftVelocity() {
        return leftShooter.getVelocity();
    }

    public double getRightVelocity() {
        return rightShooter.getVelocity();
    }

    public void shooterStop() {
        leftShooter.setPower(0);
        rightShooter.setPower(0);
    }

    public void closeGate() {
        leftGate.setPosition(0.31);
        rightGate.setPosition(0.39);
    }

    public void openGate() {
        leftGate.setPosition(0.48);
        rightGate.setPosition(0.55);
    }

    public void triggerFire() {
        triggerServo.setPower(-1);
        triggerMotor.setPower(1);
        openGate();
    }

    private void triggerFireFar() {
        triggerServo.setPower(-0.7);
        triggerMotor.setPower(0.7);
    }

    private void triggerHoldFar() {
        triggerServo.setPower(0);
        triggerMotor.setPower(0);
    }

    public void slowFire(boolean stopIntake) {
        busy = true;
        openGate();
        triggerFireFar();
        exec.schedule(this::triggerHoldFar, SHOOT_TIME, TimeUnit.MILLISECONDS);
        exec.schedule(this::triggerFireFar, SHOOT_TIME + WAIT_TIME, TimeUnit.MILLISECONDS);
        exec.schedule(this::triggerHoldFar, SHOOT_TIME * 2 + WAIT_TIME, TimeUnit.MILLISECONDS);
        exec.schedule(this::triggerFireFar, SHOOT_TIME * 2 + WAIT_TIME * 2, TimeUnit.MILLISECONDS);
        exec.schedule(this::triggerHoldFar, SHOOT_TIME * 3 + WAIT_TIME * 2 + 50, TimeUnit.MILLISECONDS);
        exec.schedule(this::closeGate, SHOOT_TIME * 3 + WAIT_TIME * 2 + 52, TimeUnit.MILLISECONDS);
        exec.schedule(() -> busy = false, SHOOT_TIME * 3 + WAIT_TIME * 2 + 54, TimeUnit.MILLISECONDS);
    }

    public void triggerPut() {
        triggerServo.setPower(1);
        triggerMotor.setPower(-1);
    }

    public void triggerHold() {
        triggerServo.setPower(0);
        triggerMotor.setPower(0);
        closeGate();
    }

    public void setTriggerMotor() {
        triggerMotor.setPower(1);
    }

    public void setTriggerServo() {
        triggerServo.setPower(1);
    }

    public void reverseTriggerServo() {
        triggerServo.setPower(-1);
    }

    public void panelTo(double pos) {
        shooterPanel.setPosition(pos);
    }
}
