package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class RobotConstants {
    public static volatile double teleOpTargetXR = 144 - 7;
    public static volatile double teleOpTargetYR = 140;

    public static volatile double teleOpTargetXB = 7;
    public static volatile double teleOpTargetYB = 140;

    public static volatile double teleOpTargetY;
    public static volatile double teleOpTargetX;

    public static volatile double autoEndX = 72;
    public static volatile double autoEndY = 72;
    public static volatile double autoEndH = Math.PI / 2.0;
    public static volatile boolean teleOpRev = Boolean.TRUE;
    public static volatile Pose MANUAL_POS;

    public static int SHOOT_VELOCITY_NER_1 = 1900;
    public static int SHOOT_VELOCITY_NER_2 = 2180;
    public static int SHOOT_VELOCITY_FAR = 2600;

    public static double PANEL_NER_1 = 0.68;
    public static double PANEL_NER_2 = 0.84;
    public static double PANEL_FAR = 0.9;

    public static double P = 35;
    public static double I = 0;
    public static double D = 0.5;
    public static double F = 12;
}
