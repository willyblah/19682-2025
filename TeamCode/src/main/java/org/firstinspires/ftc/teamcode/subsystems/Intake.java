package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.RobotConfig.*;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    DcMotorEx intake;
    CRServo lintake, rintake;

    public void init(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, INTAKE);
        lintake = hardwareMap.get(CRServo.class, INTAKE_SERVO_LEFT);
        rintake = hardwareMap.get(CRServo.class, INTAKE_SERVO_RIGHT);
        intakeStop();
    }

    public void intakeIn() {
        intake.setPower(1);
        lintake.setPower(1);
        rintake.setPower(-1);
    }

    public void intakeOut() {
        intake.setPower(-1);
        lintake.setPower(-1);
        rintake.setPower(1);
    }

    public void intakeStop() {
        intake.setPower(0);
        lintake.setPower(0);
        rintake.setPower(0);
    }
}
