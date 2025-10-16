package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.vision.AprilTagUtil;

@TeleOp(name = "Vision Test", group = "tests")
public class VisionTestOpmode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        /*VisionSubsystem visionSubsystem = new VisionSubsystem(hardwareMap);
        waitForStart();
        while(!isStopRequested()) {
            visionSubsystem.getVisionInfo().displayTelemetry(telemetry);
        }*/
    }
}
