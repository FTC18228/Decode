package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.VisionSubsystem;

@TeleOp(name = "Turret Test", group = "tests")
public class TurretTestOpmode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TurretSubsystem turretSubsystem = new TurretSubsystem(hardwareMap);
        turretSubsystem.moveHood(0);

        boolean lastA = false;
        boolean lastB = false;
        double degrees = 0;
        waitForStart();
        while(!isStopRequested()) {
            turretSubsystem.getTurretInfo().displayTelemetry(telemetry);
            if(gamepad1.a && !lastA) {
                degrees += 10;
                turretSubsystem.moveHood(degrees);
                lastA = true;
            }
            else if(!gamepad1.a) lastA = false;
            if(gamepad1.b && !lastB) {
                degrees -= 10;
                turretSubsystem.moveHood(degrees);
                lastB = true;
            }
            else if(!gamepad1.b) lastB = false;

            if(gamepad1.x) {
                turretSubsystem.fireTurret();
            }
            if(gamepad1.y) {
                turretSubsystem.stopTurret();
            }
        }
    }
}
