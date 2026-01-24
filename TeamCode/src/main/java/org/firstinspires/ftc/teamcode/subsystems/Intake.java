package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.RobotConfig.*;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotorEx intake;

    public void init(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, INTAKE);
        intakeStop();
    }

    public void intakeIn() {
        intake.setPower(1);
    }

    public void intakeOut() {
        intake.setPower(-1);
    }

    public void intakeStop() {
        intake.setPower(0);
    }
}
