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

@Autonomous(name = "RED | Near | 12")
public class AutoRedNear extends OpMode {
    private static Follower follower;
    @IgnoreConfigurable
    static TelemetryManager telemetryM;
    private final Robot robot = new Robot();

    @Override
    public void init() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        // 初始化跟随系统
        follower = new Follower(hardwareMap, telemetryM);
        follower.setStartingPose(RED_NER_START);
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
                        new DrivePointToPoint(follower, RED_NER_START, RED_NER_SHOOT),
                        new InstantCommand(() -> robot.intake.intakeIn()),
                        new WaitCommand(300),
                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new WaitCommand(200),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(1100),
                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        // 收集第一组球
                        new DrivePointToPoint(follower, RED_NER_SHOOT, RED_NER_INTAKE_PRE_1),
                        new DrivePointToPoint(follower, RED_NER_INTAKE_PRE_1, RED_NER_INTAKE_1),
                        new WaitCommand(200),

                        // 发射第一组球
                        new InstantCommand(() -> robot.shooter.setShooterVelocity(SHOOT_VELOCITY_NER_1)),
                        new DrivePointToPoint(follower, RED_NER_INTAKE_1, RED_NER_SHOOT),
                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new WaitCommand(300),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(1100),
                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        // 开闸
                        new DrivePointToPoint(follower, RED_NER_SHOOT, RED_NER_GATE),
                        new WaitCommand(300),

                        // 收集第二组球
                        new DrivePointToPoint(follower, RED_NER_GATE, RED_NER_INTAKE_MID_2, RED_NER_INTAKE_PRE_2),
                        new DrivePointToPoint(follower, RED_NER_INTAKE_PRE_2, RED_NER_INTAKE_2),
                        new WaitCommand(200),

                        // 发射第二组球
                        new InstantCommand(() -> robot.shooter.setShooterVelocity(SHOOT_VELOCITY_NER_1)),
                        new DrivePointToPoint(follower, RED_NER_INTAKE_2, RED_NER_SHOOT_MID, RED_NER_SHOOT),
                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new WaitCommand(300),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(1100),
                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        // 收集第三组球
                        new DrivePointToPoint(follower, RED_NER_SHOOT, RED_NER_INTAKE_PRE_3),
                        new DrivePointToPoint(follower, RED_NER_INTAKE_PRE_3, RED_NER_INTAKE_3),
                        new WaitCommand(200),

                        // 发射第三组球
                        new InstantCommand(() -> robot.shooter.setShooterVelocity(SHOOT_VELOCITY_NER_1)),
                        new DrivePointToPoint(follower, RED_NER_INTAKE_3, RED_NER_SHOOT),
                        new InstantCommand(() -> robot.shooter.triggerPut()),
                        new WaitCommand(300),
                        new InstantCommand(() -> robot.shooter.triggerFire()),
                        new WaitCommand(1100),
                        new InstantCommand(() -> robot.shooter.shooterStop()),
                        new InstantCommand(() -> robot.shooter.triggerHold()),

                        new InstantCommand(() -> robot.intake.intakeStop()),
                        new DrivePointToPoint(follower, RED_NER_SHOOT, RED_NER_PARK),
                        new InstantCommand(this::stop)
                )
        );
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
    }
}
