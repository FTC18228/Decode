package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@TeleOp(name="Spinner Test", group="tests")
public class SpinnerTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TurretSubsystem turretSubsystem = new TurretSubsystem(hardwareMap);
        int bearing = 0;

        waitForStart();
        while(!isStopRequested()) {
            if(gamepad1.aWasPressed() && bearing < 180) {
                bearing++;
                turretSubsystem.spinner.setTargetPosition((int) Math.round(bearing * turretSubsystem.spinner.getCPR() * turretSubsystem.motorDegreesToReal));
            }
            if(gamepad1.bWasPressed() && bearing > 0) {
                bearing--;
                turretSubsystem.spinner.setTargetPosition((int) Math.round(bearing * turretSubsystem.spinner.getCPR() * turretSubsystem.motorDegreesToReal));
            }

            if(!turretSubsystem.spinner.atTargetPosition() && gamepad1.x) {
                turretSubsystem.spinner.set(0.3);
            }
            else {
                turretSubsystem.spinner.stopMotor();
            }

            telemetry.addData("Target physical: ", bearing * turretSubsystem.motorDegreesToReal);
            telemetry.addData("Target + 1deg: ", (bearing + 1) * turretSubsystem.motorDegreesToReal);
            telemetry.addData("Target - 1deg: ", (bearing - 1) * turretSubsystem.motorDegreesToReal);
            telemetry.addData("Position: ", turretSubsystem.spinner.getCurrentPosition());
            telemetry.addData("Bearing: ", bearing);
            telemetry.update();
        }
    }
}
