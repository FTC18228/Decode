package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.TeleOpCommon;

@TeleOp(name="Blue side OpMode")
public class SoloOpMode extends CommandOpMode {
    @Override
    public void initialize() {
        TeleOpCommon.hardResetInstance();
        TeleOpCommon common = TeleOpCommon.getInstance(
                hardwareMap,
                new Pose2d(0, 0, 0),
                gamepad1,
                telemetry);
    }
}
