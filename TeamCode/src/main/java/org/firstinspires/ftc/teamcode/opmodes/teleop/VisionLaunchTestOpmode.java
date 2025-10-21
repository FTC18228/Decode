package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.debug.TagInfo;
import org.firstinspires.ftc.teamcode.debug.VisionDebug;
import org.firstinspires.ftc.teamcode.debug.VisionTurretDebug;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.vision.AprilTagUtil;

import dalvik.system.DelegateLastClassLoader;

@TeleOp(name="Vision Launch Test", group="tests")
public class VisionLaunchTestOpmode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        VisionSubsystem visionSubsystem = new VisionSubsystem(hardwareMap, true);
        TurretSubsystem turretSubsystem = new TurretSubsystem(hardwareMap);
        DriveSubsystem driveSubsystem = new DriveSubsystem(hardwareMap, new Pose2d(0, 0, 0), 10);

        waitForStart();

        boolean aPressed = false;
        boolean xPressed = false;
        boolean yPressed = false;
        while(!isStopRequested()) {
            visionSubsystem.detectTags();

            telemetry.addData("Distance: ", visionSubsystem.getDistanceToTarget());
            telemetry.addData("Bearing: ", visionSubsystem.getBearingToTarget());
            telemetry.addData("Theta estimate: ", turretSubsystem.thetaEstimate(visionSubsystem.getDistanceToTarget() / 1000));
            turretSubsystem.getTurretInfo().displayTelemetry(telemetry, false);
            telemetry.update();

            if(gamepad1.a) {
                if(!aPressed) turretSubsystem.aimTurret(visionSubsystem.getDistanceToTarget() / 1000, visionSubsystem.getBearingToTarget());
                aPressed = true;
            }
            else {
                aPressed = false;
            }

            if(gamepad1.x) {
                if(!xPressed) turretSubsystem.fireTurret();
                xPressed = true;
            }
            else {
                xPressed = false;
            }
            if(gamepad1.y) {
                if(!yPressed) turretSubsystem.stopTurret();
                yPressed = true;
            }
            else {
                yPressed = false;
            }
        }
    }
}
