package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.constants.AutoConstants.*;
import static org.firstinspires.ftc.teamcode.constants.RobotConstants.*;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
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

@Autonomous(name = "BLUE | Near | 12")
public class AutoBlueNear extends OpMode {
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

    public static void draw() {
        Drawing.drawDebug(follower.follower);
    }

    @Override
    public void init_loop() {
        robot.shooter.panelTo(0);
        follower.follower.update();
        drawOnlyCurrent();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        follower.follower.update();
        draw();
    }

    @Override
    public void start() {
        robot.shooter.setShooterVelocity(SHOOT_VELOCITY_NER_1);
        // 激活所有PIDF控制器
        follower.follower.activateAllPIDFs();
        // 使用命令调度器安排一系列顺序执行的命令组
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        // 第一轮发射
                        new DrivePointToPoint(follower, BLUE_NER_START, BLUE_NER_SHOOT_1).setHoldEnd(false),
                        new InstantCommand(() -> robot.intake.intakeIn()),
                        new WaitCommand(300),

                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new InstantCommand(() -> robot.shooter.panelTo(PANEL_NER_1)),
                        new WaitCommand(500),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(1400),
                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        new WaitCommand(500),

                        // 收集第一组球
                        new InstantCommand(() -> robot.intake.intakeIn()),
                        new InstantCommand(() -> robot.shooter.setTriggerMotor()),
                        new InstantCommand(() -> robot.shooter.setTriggerServo()),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_1, BLUE_NER_INTAKE_PRE_1).setHoldEnd(false),
                        new WaitCommand(300),
                        new ParallelCommandGroup(
                                new DrivePointToPoint(follower, BLUE_NER_INTAKE_PRE_1, BLUE_NER_INTAKE_1).setHoldEnd(false),
                                new SequentialCommandGroup(
                                        new WaitCommand(1000),
                                        new InstantCommand(() -> robot.shooter.triggerHold()),
                                        new InstantCommand(() -> robot.shooter.setTriggerServo())
                                )
                        ),
                        new WaitCommand(500),

                        // 发射第一组球
                        new ParallelCommandGroup(
                                new DrivePointToPoint(follower, BLUE_NER_INTAKE_1, BLUE_NER_SHOOT_1).setHoldEnd(false),
                                new InstantCommand(() -> robot.shooter.panelTo(PANEL_NER_1))
                        ),
                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new InstantCommand(() -> robot.shooter.setShooterVelocity(SHOOT_VELOCITY_NER_1)),
                        new WaitCommand(500),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(1400),

                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        new WaitCommand(500),

                        // 收集第二组球
                        new InstantCommand(() -> robot.intake.intakeIn()),
                        new InstantCommand(() -> robot.shooter.setTriggerMotor()),
                        new InstantCommand(() -> robot.shooter.setTriggerServo()),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_1, BLUE_NER_INTAKE_PRE_2).setHoldEnd(false),
                        new WaitCommand(500),
                        new ParallelCommandGroup(
                                new DrivePointToPoint(follower, BLUE_NER_INTAKE_PRE_2, BLUE_NER_INTAKE_2).setHoldEnd(false),
                                new SequentialCommandGroup(
                                        new WaitCommand(1000),
                                        new InstantCommand(() -> robot.shooter.triggerHold()),
                                        new InstantCommand(() -> robot.shooter.setTriggerServo())
                                )
                        ),
                        new WaitCommand(300),

                        // 发射第二组球
                        new ParallelCommandGroup(
                                new DrivePointToPoint(follower, BLUE_NER_INTAKE_2, BLUE_NER_SHOOT_2).setHoldEnd(false),
                                new InstantCommand(() -> robot.shooter.panelTo(PANEL_NER_1))
                        ),
                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new InstantCommand(() -> robot.shooter.setShooterVelocity(SHOOT_VELOCITY_NER_1)),
                        new WaitCommand(500),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(1400),

                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        new WaitCommand(500),

                        // 收集第三组球
                        new InstantCommand(() -> robot.intake.intakeIn()),
                        new InstantCommand(() -> robot.shooter.setTriggerMotor()),
                        new InstantCommand(() -> robot.shooter.setTriggerServo()),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_2, BLUE_NER_INTAKE_PRE_3).setHoldEnd(false),
                        new WaitCommand(500),
                        new ParallelCommandGroup(
                                new DrivePointToPoint(follower, BLUE_NER_INTAKE_PRE_3, BLUE_NER_INTAKE_3).setHoldEnd(false),
                                new SequentialCommandGroup(
                                        new WaitCommand(1000),
                                        new InstantCommand(() -> robot.shooter.triggerHold()),
                                        new InstantCommand(() -> robot.shooter.setTriggerServo())
                                )
                        ),
                        new WaitCommand(300),

                        // 发射第三组球
                        new ParallelCommandGroup(
                                new DrivePointToPoint(follower, BLUE_NER_INTAKE_3, BLUE_NER_SHOOT_3).setHoldEnd(false),
                                new InstantCommand(() -> robot.shooter.panelTo(PANEL_NER_1))
                        ),
                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new InstantCommand(() -> robot.shooter.setShooterVelocity(SHOOT_VELOCITY_NER_1)),
                        new WaitCommand(500),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(1400),

                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        new InstantCommand(() -> robot.intake.intakeStop()),

                        new WaitCommand(200),
                        new DrivePointToPoint(follower, BLUE_NER_SHOOT_3, BLUE_NER_PARK).setHoldEnd(false),

                        new InstantCommand(this::stop)
                )
        );
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
    }
}
