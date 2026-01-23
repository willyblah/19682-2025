package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class AutoConstants {

    // 近端摆位点
    public static Pose RED_NER_START = new Pose(124.5, 123.5, Math.toRadians(128));

    // 第一组球收集位置
    public static Pose RED_NER_INTAKE_PRE_1 = new Pose(101, 83, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_1 = new Pose(129, 83, Math.toRadians(90));

    // 第二组球收集位置
    public static Pose RED_NER_INTAKE_MID_2 = new Pose(96, 80);
    public static Pose RED_NER_INTAKE_PRE_2 = new Pose(101, 60, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_2 = new Pose(135, 60, Math.toRadians(90));

    // 第三组球收集位置
    public static Pose RED_NER_INTAKE_PRE_3 = new Pose(101, 36, Math.toRadians(90));
    public static Pose RED_NER_INTAKE_3 = new Pose(135, 36, Math.toRadians(90));

    // 发射位置
    public static Pose RED_NER_SHOOT_MID = new Pose(113, 57);
    public static Pose RED_NER_SHOOT = new Pose(97, 89, Math.toRadians(140));

    // 开闸位置
    public static Pose RED_NER_GATE = new Pose(131, 72, Math.toRadians(180));

    // 停车位置
    public static Pose RED_NER_PARK = new Pose(115, 78, Math.toRadians(90));

    // 远端摆位点
    public static Pose RED_FAR_START = new Pose(88, 8, Math.toRadians(180));

    // 远端发射位
    public static Pose RED_FAR_SHOOT = new Pose(87, 17, Math.toRadians(157));

    // 远端重复收球位置
    public static Pose RED_FAR_INTAKE_1 = new Pose(132, 6, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_2 = new Pose(126, 8, Math.toRadians(90));
    public static Pose RED_FAR_INTAKE_3 = new Pose(132, 12, Math.toRadians(90));

    // 远点停车位置
    public static Pose RED_FAR_PARK = new Pose(108, 10, Math.toRadians(180));


    public static Pose BLUE_NER_START = RED_NER_START.mirror();
    public static Pose BLUE_NER_INTAKE_PRE_1 = RED_NER_INTAKE_PRE_1.mirror();
    public static Pose BLUE_NER_INTAKE_1 = RED_NER_INTAKE_1.mirror();
    public static Pose BLUE_NER_INTAKE_PRE_2 = RED_NER_INTAKE_PRE_2.mirror();
    public static Pose BLUE_NER_INTAKE_MID_2 = RED_NER_INTAKE_MID_2.mirror();
    public static Pose BLUE_NER_INTAKE_2 = RED_NER_INTAKE_2.mirror();
    public static Pose BLUE_NER_INTAKE_PRE_3 = RED_NER_INTAKE_PRE_3.mirror();
    public static Pose BLUE_NER_INTAKE_3 = RED_NER_INTAKE_3.mirror();
    public static Pose BLUE_NER_SHOOT_MID = RED_NER_SHOOT_MID.mirror();
    public static Pose BLUE_NER_SHOOT = RED_NER_SHOOT.mirror();
    public static Pose BLUE_NER_GATE = RED_NER_GATE.mirror();
    public static Pose BLUE_NER_PARK = RED_NER_PARK.mirror();
    public static Pose BLUE_FAR_START = RED_FAR_START.mirror();
    public static Pose BLUE_FAR_SHOOT = RED_FAR_SHOOT.mirror();
    public static Pose BLUE_FAR_INTAKE_1 = RED_FAR_INTAKE_1.mirror();
    public static Pose BLUE_FAR_INTAKE_2 = RED_FAR_INTAKE_2.mirror();
    public static Pose BLUE_FAR_INTAKE_3 = RED_FAR_INTAKE_3.mirror();
    public static Pose BLUE_FAR_PARK = RED_FAR_PARK.mirror();
}
