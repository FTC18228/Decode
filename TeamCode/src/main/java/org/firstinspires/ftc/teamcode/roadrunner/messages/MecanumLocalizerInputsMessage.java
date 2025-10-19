package org.firstinspires.ftc.teamcode.roadrunner.messages;

import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public final class MecanumLocalizerInputsMessage {
    public final long timestamp;
    public final PositionVelocityPair leftFront;
    public final PositionVelocityPair leftBack;
    public final PositionVelocityPair rightBack;
    public final PositionVelocityPair rightFront;
    public final double yaw;
    public final double pitch;
    public final double roll;

    public MecanumLocalizerInputsMessage(PositionVelocityPair leftFront, PositionVelocityPair leftBack, PositionVelocityPair rightBack, PositionVelocityPair rightFront, YawPitchRollAngles angles) {
        this.timestamp = System.nanoTime();
        this.leftFront = leftFront;
        this.leftBack = leftBack;
        this.rightBack = rightBack;
        this.rightFront = rightFront;
        {
            this.yaw = angles.getYaw(AngleUnit.RADIANS);
            this.pitch = angles.getPitch(AngleUnit.RADIANS);
            this.roll = angles.getRoll(AngleUnit.RADIANS);
        }
    }
}
