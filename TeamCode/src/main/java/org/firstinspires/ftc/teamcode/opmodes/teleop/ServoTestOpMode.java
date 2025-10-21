package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.Constants;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@TeleOp(name="Servo Test", group="tests")
public class ServoTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo = hardwareMap.get(Servo.class, Constants.Hardware.turretHoodName);
        TurretSubsystem turretSubsystem = new TurretSubsystem(hardwareMap);
        servo.setPosition(0);
        double position = 0;
        boolean anyPressed = false;

        waitForStart();
        while(!isStopRequested()) {
            if(gamepad1.a && servo.getPosition() < 1 && !anyPressed) {
                servo.setPosition(position + 0.05);
                position += 0.05;
                anyPressed = true;
            }
            else if(gamepad1.b && servo.getPosition() > 0 && !anyPressed) {
                servo.setPosition(position - 0.05);
                position -= 0.05;
                anyPressed = true;
            }
            else if(!gamepad1.a && !gamepad1.b) anyPressed = false;
            if(gamepad1.x) {
                turretSubsystem.fireTurret();
            }
            if(gamepad1.y) {
                turretSubsystem.stopTurret();
            }
            telemetry.addData("Position: ", position);
            telemetry.update();
        }
    }
}
