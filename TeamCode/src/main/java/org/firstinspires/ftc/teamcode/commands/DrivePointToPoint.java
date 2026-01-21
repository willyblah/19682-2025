package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;

import org.firstinspires.ftc.teamcode.subsystems.Follower;

public class DrivePointToPoint extends CommandBase {
    private Follower follower;
    private Pose originalPos, targetPos, midPos1, midPos2;
    private int numberOfPoints;
    private boolean holdEnd = false;
    private double breaking = 1.0;

    public DrivePointToPoint(Follower follower, Pose start, Pose mid1, Pose mid2, Pose end) {
        originalPos = new Pose(start.getX(), start.getY(), start.getHeading());
        targetPos = new Pose(end.getX(), end.getY(), end.getHeading());
        midPos1 = new Pose(mid1.getX(), mid1.getY());
        midPos2 = new Pose(mid2.getX(), mid2.getY());
        this.follower = follower;
        numberOfPoints = 4;
    }

    public DrivePointToPoint(Follower follower, Pose start, Pose mid1, Pose end) {
        originalPos = new Pose(start.getX(), start.getY(), start.getHeading());
        targetPos = new Pose(end.getX(), end.getY(), end.getHeading());
        midPos1 = new Pose(mid1.getX(), mid1.getY());
        this.follower = follower;
        numberOfPoints = 3;
    }

    public DrivePointToPoint(Follower follower, Pose start, Pose end) {
        originalPos = new Pose(start.getX(), start.getY(), start.getHeading());
        targetPos = new Pose(end.getX(), end.getY(), end.getHeading());
        this.follower = follower;
        numberOfPoints = 2;
    }

    public DrivePointToPoint setHoldEnd(boolean holdEnd) {
        this.holdEnd = holdEnd;
        return this;
    }

    public DrivePointToPoint setBreaking(double breakingStrength) {
        this.breaking = breakingStrength;
        return this;
    }

    @Override
    public void initialize() {
        this.follower.breakFollowing();
        PathBuilder builder = new PathBuilder(this.follower.follower);
        if (numberOfPoints == 4) {
            builder.addPath(
                            new BezierCurve(
                                    new Pose(originalPos.getX(), originalPos.getY()),
                                    new Pose(midPos1.getX(), midPos1.getY()),
                                    new Pose(midPos2.getX(), midPos2.getY()),
                                    new Pose(targetPos.getX(), targetPos.getY())
                            )
                    )
                    .setLinearHeadingInterpolation(originalPos.getHeading(), targetPos.getHeading())
                    .setBrakingStrength(this.breaking);
        } else if (numberOfPoints == 3) {
            builder.addPath(
                            new BezierCurve(
                                    new Pose(originalPos.getX(), originalPos.getY()),
                                    new Pose(midPos1.getX(), midPos1.getY()),
                                    new Pose(targetPos.getX(), targetPos.getY())
                            )
                    )
                    .setLinearHeadingInterpolation(originalPos.getHeading(), targetPos.getHeading())
                    .setBrakingStrength(this.breaking);
        } else {
            builder.addPath(
                            new BezierLine(
                                    new Pose(originalPos.getX(), originalPos.getY()),
                                    new Pose(targetPos.getX(), targetPos.getY())
                            )
                    )
                    .setLinearHeadingInterpolation(originalPos.getHeading(), targetPos.getHeading())
                    .setBrakingStrength(this.breaking);
        }
        this.follower.breakFollowing();
        this.follower.followPath(builder.build(), this.holdEnd);
    }

    @Override
    public boolean isFinished() {
        return !follower.isBusy() || follower.isRobotStuck();
    }

    @Override
    public void end(boolean interrupted) {
    }
}
