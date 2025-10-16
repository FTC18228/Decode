package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.debug.TagInfo;
import org.firstinspires.ftc.teamcode.debug.VisionDebug;
import org.firstinspires.ftc.teamcode.debug.VisionTurretDebug;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.vision.AprilTagUtil;

@TeleOp(name="Vision Launch Test", group="tests")
public class VisionLaunchTestOpmode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        VisionSubsystem visionSubsystem = new VisionSubsystem(hardwareMap, true);
        TurretSubsystem turretSubsystem = new TurretSubsystem(hardwareMap);

        waitForStart();
        while(!isStopRequested()) {
            VisionDebug visionDebug = visionSubsystem.getVisionInfo();
            if(visionSubsystem.isTagActive(20)) {
                TagInfo tagInfo = visionDebug.getValue("20.0");
                VisionTurretDebug debug = new VisionTurretDebug(
                        turretSubsystem.hoodPosition(),
                        turretSubsystem.hoodDegrees(),
                        turretSubsystem.thetaEstimate(tagInfo.getValue("distance") / 1000),
                        tagInfo.getValue("id"),
                        tagInfo.getValue("distance") / 1000,
                        tagInfo.getValue("bearing")
                );
                debug.displayTelemetry(telemetry, true);
            }
        }
    }
}
