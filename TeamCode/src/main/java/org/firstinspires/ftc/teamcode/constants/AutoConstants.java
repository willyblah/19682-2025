package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class AutoConstants {

    public static Pose RED_MANUAL_POS = new Pose(8, 8, Math.toRadians(90));
    public static Pose BLUE_MANUAL_POS = new Pose(144 - 8, 8, Math.toRadians(270));

    // 近端摆位点
    public static Pose RED_NER_START = new Pose(123.5, 121.5, Math.toRadians(135));

    // 第一组球收集位置
    public static Pose RED_NER_INTAKE_PRE_1 = new Pose(95, 83, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_1 = new Pose(130, 83, Math.toRadians(90));

    // 第二组球收集位置
    public static Pose RED_NER_INTAKE_PRE_2 = new Pose(96, 59, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_2 = new Pose(139, 59, Math.toRadians(90));

    // 第三组球收集位置
    public static Pose RED_NER_INTAKE_PRE_3 = new Pose(96, 35, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_3 = new Pose(139, 35, Math.toRadians(90));

    // 发射位置
    public static Pose RED_NER_SHOOT_1 = new Pose(99, 89, Math.toRadians(145));
    public static Pose RED_NER_SHOOT_2 = new Pose(94, 89, Math.toRadians(135));

    // 开闸位置
    public static Pose RED_NER_GATE_MID_1 = new Pose(118, 63);
    public static Pose RED_NER_GATE_1 = new Pose(130, 70, Math.toRadians(90));
    public static Pose RED_NER_GATE_MID_2 = new Pose(121, 75);
    public static Pose RED_NER_GATE_2 = new Pose(130, 72, Math.toRadians(90));
    public static Pose RED_NER_SUCK_MID = new Pose(120, 63);
    public static Pose RED_NER_SUCK_1 = new Pose(134, 57, Math.toRadians(115));
    public static Pose RED_NER_SUCK_2 = new Pose(131, 55, Math.toRadians(115));
    public static Pose RED_NER_SUCK_3 = new Pose(134, 52, Math.toRadians(115));

    // 停车位置
    public static Pose RED_NER_PARK = new Pose(115, 78, Math.toRadians(90));

    // 远端摆位点
    public static Pose RED_FAR_START = new Pose(88, 7, Math.toRadians(180));

    // 远端发射位
    public static Pose RED_FAR_SHOOT = new Pose(84, 20, Math.toRadians(155));

    // 远端重复收球位置
    public static Pose RED_FAR_INTAKE_1 = new Pose(131, 7, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_2 = new Pose(122, 9, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_3 = new Pose(131, 11, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_4 = new Pose(131, 10, Math.toRadians(90));
    public static Pose RED_FAR_SUCK = new Pose(133, 15, Math.toRadians(150));

    // 远点停车位置
    public static Pose RED_FAR_PARK = new Pose(110, 12, Math.toRadians(90));


    public static Pose BLUE_NER_START = new Pose(144 - 123.5, 121.5, Math.toRadians(225));
    public static Pose BLUE_NER_INTAKE_PRE_1 = new Pose(144 - 95, 83, Math.toRadians(270));
    public static Pose BLUE_NER_INTAKE_1 = new Pose(144 - 130, 83, Math.toRadians(270));
    public static Pose BLUE_NER_INTAKE_PRE_2 = new Pose(144 - 96, 59, Math.toRadians(270));
    public static Pose BLUE_NER_INTAKE_2 = new Pose(144 - 139, 59, Math.toRadians(270));
    public static Pose BLUE_NER_INTAKE_PRE_3 = new Pose(144 - 96, 35, Math.toRadians(270));
    public static Pose BLUE_NER_INTAKE_3 = new Pose(144 - 139, 35, Math.toRadians(270));
    public static Pose BLUE_NER_SHOOT_1 = new Pose(144 - 99, 89, Math.toRadians(215));
    public static Pose BLUE_NER_SHOOT_2 = new Pose(144 - 94, 89, Math.toRadians(225));
    public static Pose BLUE_NER_GATE_MID_1 = new Pose(144 - 118, 63);
    public static Pose BLUE_NER_GATE_1 = new Pose(144 - 130, 70, Math.toRadians(270));
    public static Pose BLUE_NER_GATE_MID_2 = new Pose(144 - 121, 75);
    public static Pose BLUE_NER_GATE_2 = new Pose(144 - 130, 72, Math.toRadians(270));
    public static Pose BLUE_NER_SUCK_MID = new Pose(144 - 120, 63);
    public static Pose BLUE_NER_SUCK_1 = new Pose(144 - 134, 57, Math.toRadians(245));
    public static Pose BLUE_NER_SUCK_2 = new Pose(144 - 131, 55, Math.toRadians(245));
    public static Pose BLUE_NER_SUCK_3 = new Pose(144 - 134, 52, Math.toRadians(245));
    public static Pose BLUE_NER_PARK = new Pose(144 - 115, 78, Math.toRadians(270));
    public static Pose BLUE_FAR_START = new Pose(144 - 88, 7, Math.toRadians(180));
    public static Pose BLUE_FAR_SHOOT = new Pose(144 - 84, 20, Math.toRadians(205));
    public static Pose BLUE_FAR_INTAKE_1 = new Pose(144 - 131, 7, Math.toRadians(270));
    public static Pose BLUE_FAR_INTAKE_2 = new Pose(144 - 122, 9, Math.toRadians(270));
    public static Pose BLUE_FAR_INTAKE_3 = new Pose(144 - 131, 11, Math.toRadians(270));
    public static Pose BLUE_FAR_INTAKE_4 = new Pose(144 - 131, 10, Math.toRadians(270));
    public static Pose BLUE_FAR_SUCK = new Pose(144 - 133, 15, Math.toRadians(210));
    public static Pose BLUE_FAR_PARK = new Pose(144 - 110, 12, Math.toRadians(270));
}
