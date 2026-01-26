package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class AutoConstants {

    // 近端摆位点
    public static Pose RED_NER_START = new Pose(123, 121, Math.toRadians(135));

    // 第一组球收集位置
    public static Pose RED_NER_INTAKE_PRE_1 = new Pose(95, 82, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_1 = new Pose(130, 83, Math.toRadians(90));

    // 第二组球收集位置
    public static Pose RED_NER_INTAKE_PRE_2 = new Pose(95, 59, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_2 = new Pose(134, 59, Math.toRadians(90));

    // 第三组球收集位置
    public static Pose RED_NER_INTAKE_PRE_3 = new Pose(95, 35, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_3 = new Pose(134, 35, Math.toRadians(90));

    // 发射位置
    public static Pose RED_NER_SHOOT_1 = new Pose(100, 89, Math.toRadians(145));
    public static Pose RED_NER_SHOOT_2 = new Pose(94, 89, Math.toRadians(135));

    // 开闸位置
    public static Pose RED_NER_GATE_MID = new Pose(118, 63);
    public static Pose RED_NER_GATE = new Pose(130, 70, Math.toRadians(90));
    public static Pose RED_NER_SUCK_MID = new Pose(120, 63);
    public static Pose RED_NER_SUCK_1 = new Pose(135, 57, Math.toRadians(110));
    public static Pose RED_NER_SUCK_2 = new Pose(131, 55, Math.toRadians(110));
    public static Pose RED_NER_SUCK_3 = new Pose(135, 52, Math.toRadians(110));

    // 停车位置
    public static Pose RED_NER_PARK = new Pose(115, 78, Math.toRadians(90));

    // 远端摆位点
    public static Pose RED_FAR_START = new Pose(88, 7, Math.toRadians(180));

    // 远端发射位
    public static Pose RED_FAR_SHOOT = new Pose(84, 20, Math.toRadians(155));

    // 远端重复收球位置
    public static Pose RED_FAR_INTAKE_1 = new Pose(133, 5, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_2 = new Pose(124, 8, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_3 = new Pose(133, 11, Math.toRadians(90));

    // 远点停车位置
    public static Pose RED_FAR_PARK = new Pose(110, 10, Math.toRadians(180));
}
