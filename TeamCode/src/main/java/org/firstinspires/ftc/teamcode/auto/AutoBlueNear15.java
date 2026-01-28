package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.constants.AutoConstants.*;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.bylazar.configurables.annotations.IgnoreConfigurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.commands.DrivePointToPoint;
import org.firstinspires.ftc.teamcode.subsystems.Drawing;
import org.firstinspires.ftc.teamcode.subsystems.Follower;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous(name = "BLUE | Near | 15")
public class AutoBlueNear15 extends OpMode {
    private static Follower follower;
    @IgnoreConfigurable
    static TelemetryManager telemetryM;
    private final Robot robot = new Robot();

    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        // 初始化跟随系统
        follower = new Follower(hardwareMap, telemetryM);
        follower.setStartingPose(BLUE_NER_START);
        // 初始化机器人系统
        robot.autoInit(hardwareMap);
        Drawing.init();
    }

    public static void drawOnlyCurrent() {
        try {
            // 获取跟随机器人的当前位姿并绘制
            Drawing.drawRobot(follower.getPose());
            // 发送绘制数据包
            Drawing.sendPacket();
        } catch (Exception e) {
            // 捕获所有异常并包装为运行时异常抛出
            throw new RuntimeException("Drawing failed " + e);
        }
    }

    @Override
    public void init_loop() {
        robot.shooter.panelTo(PANEL_NER_1);
        follower.follower.update();
        drawOnlyCurrent();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.follower.update();
        Drawing.drawDebug(follower.follower);
    }

    @Override
    public void start() {
        // 激活所有PIDF控制器
        follower.follower.activateAllPIDFs();
        // 使用命令调度器安排一系列顺序执行的命令组
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        // 发射预载球
                        new InstantCommand(() -> robot.shooter.setShooterVelocity(SHOOT_VELOCITY_NER_1)),
                        new DrivePointToPoint(follower, BLUE_NER_START, BLUE_NER_SHOOT_1),
                        new InstantCommand(() -> robot.intake.intakeIn()),
                        new WaitCommand(300),
                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new WaitCommand(200),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(900),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        // 收集第一组球
                        new InstantCommand(() -> robot.shooter.setTriggerMotor()),
                        new InstantCommand(() -> robot.shooter.reverseTriggerServo()),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_1, BLUE_NER_INTAKE_PRE_1),
                        new DrivePointToPoint(follower, BLUE_NER_INTAKE_PRE_1, BLUE_NER_INTAKE_1, 0.8),
                        new InstantCommand(() -> robot.shooter.triggerHold()),
                        new InstantCommand(() -> robot.shooter.setTriggerServo()),

                        // 发射第一组球
                        new DrivePointToPoint(follower, BLUE_NER_INTAKE_1, BLUE_NER_SHOOT_1),
                        new InstantCommand(() -> robot.shooter.openGate()),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(900),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        // 收集第二组球
                        new InstantCommand(() -> robot.shooter.setTriggerMotor()),
                        new InstantCommand(() -> robot.shooter.reverseTriggerServo()),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_1, BLUE_NER_INTAKE_PRE_2),
                        new DrivePointToPoint(follower, BLUE_NER_INTAKE_PRE_2, BLUE_NER_INTAKE_2, 0.8),
                        new InstantCommand(() -> robot.shooter.triggerHold()),
                        new InstantCommand(() -> robot.shooter.setTriggerServo()),

                        // 发射第二组球
                        new DrivePointToPoint(follower, BLUE_NER_INTAKE_2, BLUE_NER_GATE_MID, BLUE_NER_SHOOT_2),
                        new InstantCommand(() -> robot.shooter.openGate()),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(900),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        // 吸闸里的球
                        new InstantCommand(() -> robot.intake.intakeStop()),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_2, BLUE_NER_GATE),
                        new WaitCommand(200),
                        new InstantCommand(() -> robot.intake.intakeIn()),
                        new InstantCommand(() -> robot.shooter.setTriggerMotor()),
                        new InstantCommand(() -> robot.shooter.reverseTriggerServo()),
                        new DrivePointToPoint(follower, BLUE_NER_GATE, BLUE_NER_SUCK_MID, BLUE_NER_SUCK_1),
                        new WaitCommand(300),
                        new DrivePointToPoint(follower, BLUE_NER_SUCK_1, BLUE_NER_SUCK_2),
                        new DrivePointToPoint(follower, BLUE_NER_SUCK_2, BLUE_NER_SUCK_3),
                        new WaitCommand(300),

                        // 发闸里的球
                        new DrivePointToPoint(follower, BLUE_NER_SUCK_3, BLUE_NER_SHOOT_2),
                        new InstantCommand(() -> robot.shooter.openGate()),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(900),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        // 收集第三组球
                        new InstantCommand(() -> robot.shooter.setTriggerMotor()),
                        new InstantCommand(() -> robot.shooter.reverseTriggerServo()),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_2, BLUE_NER_INTAKE_PRE_3),
                        new DrivePointToPoint(follower, BLUE_NER_INTAKE_PRE_3, BLUE_NER_INTAKE_3, 0.8),
                        new InstantCommand(() -> robot.shooter.triggerHold()),
                        new InstantCommand(() -> robot.shooter.setTriggerServo()),

                        // 发射第三组球
                        new DrivePointToPoint(follower, BLUE_NER_INTAKE_3, BLUE_NER_SHOOT_2),
                        new InstantCommand(() -> robot.shooter.openGate()),
                        new WaitCommand(100),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(900),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        new InstantCommand(() -> robot.intake.intakeStop()),
                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_2, BLUE_NER_PARK),
                        new InstantCommand(this::stop)
                )
        );
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
    }
}
