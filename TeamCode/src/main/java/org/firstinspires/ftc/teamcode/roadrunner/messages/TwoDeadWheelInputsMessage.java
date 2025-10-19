package org.firstinspires.ftc.teamcode.roadrunner.messages;

import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public final class TwoDeadWheelInputsMessage {
    public final long timestamp;
    public final PositionVelocityPair par;
    public final PositionVelocityPair perp;
    public final double yaw;
    public final double pitch;
    public final double roll;
    public final double xRotationRate;
    public final double yRotationRate;
    public final double zRotationRate;

    public TwoDeadWheelInputsMessage(PositionVelocityPair par, PositionVelocityPair perp, YawPitchRollAngles angles, AngularVelocity angularVelocity) {
        this.timestamp = System.nanoTime();
        this.par = par;
        this.perp = perp;
        {
            this.yaw = angles.getYaw(AngleUnit.RADIANS);
            this.pitch = angles.getPitch(AngleUnit.RADIANS);
            this.roll = angles.getRoll(AngleUnit.RADIANS);
        }
        {
            this.xRotationRate = angularVelocity.xRotationRate;
            this.yRotationRate = angularVelocity.yRotationRate;
            this.zRotationRate = angularVelocity.zRotationRate;
        }
    }
}
