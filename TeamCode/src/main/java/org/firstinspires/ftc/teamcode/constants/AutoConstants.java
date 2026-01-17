package org.firstinspires.ftc.teamcode.constants;

import com.pedropathing.geometry.Pose;

public class AutoConstants {

    // ------------ 红色方 ------------

    // 近端摆位点
    public static Pose RED_NER_START = new Pose(144 - 20, 122, Math.toRadians(135));

    // 第一组球收集位置
    public static Pose RED_NER_INTAKE_PRE_1 = new Pose(144 - 38, 82, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_1 = new Pose(144 - 10, 82, Math.toRadians(90));

    // 第二组球收集位置
    public static Pose RED_NER_INTAKE_PRE_2 = new Pose(144 - 38, 59, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_2 = new Pose(144 - 5, 59, Math.toRadians(90));

    // 第三组球收集位置
    public static Pose RED_NER_INTAKE_PRE_3 = new Pose(144 - 38, 35, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_3 = new Pose(144 - 10, 35, Math.toRadians(90));

    // 发射位置
    public static Pose RED_NER_SHOOT_1 = new Pose(144 - 47, 89, Math.toRadians(135));
    public static Pose RED_NER_SHOOT_2 = new Pose(144 - 47, 89, Math.toRadians(135));
    public static Pose RED_NER_SHOOT_3 = new Pose(144 - 45, 90, Math.toRadians(135));

    // 停车位置
    public static Pose RED_NER_PARK = new Pose(144 - 30, 78, Math.toRadians(90));

    // 远端摆位点
    public static Pose RED_FAR_START = new Pose(144 - 56, 7, Math.toRadians(180));
    public static Pose RED_FAR_SHOOT = new Pose(144 - 56, 18, Math.toRadians(157));

    // 远端重复收球位置
    public static Pose RED_FAR_INTAKE_1 = new Pose(144 - 13, 5, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_2 = new Pose(144 - 18, 7, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_3 = new Pose(144 - 13, 11, Math.toRadians(90));

    // 远点停车位置
    public static Pose RED_FAR_PARK = new Pose(144 - 30, 10, Math.toRadians(180));

    // ------------ 蓝色方 ------------

    public static Pose BLUE_NER_START = RED_NER_START.mirror();
    public static Pose BLUE_NER_INTAKE_PRE_1 = RED_NER_INTAKE_PRE_1.mirror();
    public static Pose BLUE_NER_INTAKE_1 = RED_NER_INTAKE_1.mirror();
    public static Pose BLUE_NER_INTAKE_PRE_2 = RED_NER_INTAKE_PRE_2.mirror();
    public static Pose BLUE_NER_INTAKE_2 = RED_NER_INTAKE_2.mirror();
    public static Pose BLUE_NER_INTAKE_PRE_3 = RED_NER_INTAKE_PRE_3.mirror();
    public static Pose BLUE_NER_INTAKE_3 = RED_NER_INTAKE_3.mirror();
    public static Pose BLUE_NER_SHOOT_1 = RED_NER_SHOOT_1.mirror();
    public static Pose BLUE_NER_SHOOT_2 = RED_NER_SHOOT_2.mirror();
    public static Pose BLUE_NER_SHOOT_3 = RED_NER_SHOOT_3.mirror();
    public static Pose BLUE_NER_PARK = RED_NER_PARK.mirror();
    public static Pose BLUE_FAR_START = RED_FAR_START.mirror();
    public static Pose BLUE_FAR_SHOOT = RED_FAR_SHOOT.mirror();
    public static Pose BLUE_FAR_INTAKE_1 = RED_FAR_INTAKE_1.mirror();
    public static Pose BLUE_FAR_INTAKE_2 = RED_FAR_INTAKE_2.mirror();
    public static Pose BLUE_FAR_INTAKE_3 = RED_FAR_INTAKE_3.mirror();
    public static Pose BLUE_FAR_PARK = RED_FAR_PARK.mirror();
}
