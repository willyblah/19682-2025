package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.RobotConfig.*;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter {
    public DcMotorEx leftShooter, rightShooter;
    DcMotorEx triggerMotor;
    CRServo triggerServo;
    Servo shooterPanel;
    Servo leftGate, rightGate;

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
        triggerMotor.setPower(0.8);
    }

    public void setTriggerServo() {
        triggerServo.setPower(1);
    }

    public void panelTo(double pos) {
        shooterPanel.setPosition(pos);
    }
}
