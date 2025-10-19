package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.debug.DriveDebug;

public class DriveSubsystem extends SubsystemBase {
    final MecanumDrive drive;
    double speed;
    public DriveSubsystem(HardwareMap hardwareMap, Pose2d defaultPose, double speed) {
        this.drive = new MecanumDrive(hardwareMap, defaultPose);
        this.speed = speed;
    }

    double rotationOf(double x, double y) {
        if(x == 0) {
            return Math.PI / 2;
        }
        return Math.toRadians(Math.signum(x) * 90) - Math.atan(y / x);
    }

    public DriveDebug drive(double leftX, double leftY, double rightX) { //THIS IS DEFAULT ONE
        double rotation = drive.localizer.getPose().heading.toDouble();
        Vector2d velocity = new Vector2d(
                leftY * Math.cos(-rotation) + leftX * Math.sin(-rotation),
                leftY * Math.sin(-rotation) - leftX * Math.cos(-rotation)
        );
        drive.setDrivePowers(new PoseVelocity2d(velocity.times(speed), -rightX));
        drive.updatePoseEstimate();
        return new DriveDebug(velocity.x, velocity.y, -rotation, drive.localizer.getPose().position.x, drive.localizer.getPose().position.y);
    }

    public DriveDebug drive(double leftX, double leftY, double rightX, double rightY, boolean useSnapRotation) {
        double rotation = drive.localizer.getPose().heading.toDouble();
        Vector2d velocity = new Vector2d(
                leftY * Math.cos(-rotation) + leftX * Math.sin(-rotation),
                leftY * Math.sin(-rotation) - leftX * Math.cos(-rotation)
        );

        double angular;
        if(!useSnapRotation) angular = -rightX;
        else angular = rotation - rotationOf(rightX, rightY);

        drive.setDrivePowers(new PoseVelocity2d(velocity.times(speed), angular));
        drive.updatePoseEstimate();
        return new DriveDebug(velocity.x, velocity.y, -rotation, drive.localizer.getPose().position.x, drive.localizer.getPose().position.y);
    }

    public void changeSpeed(double newSpeed) {
        speed = newSpeed;
    }
}
