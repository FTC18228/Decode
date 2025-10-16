package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@TeleOp(name="Motor Test", group="tests")
public class MotorTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motor = hardwareMap.get(DcMotor.class, "motor");
        Servo servo = hardwareMap.get(Servo.class, "servo");

        waitForStart();
        motor.setPower(1);
        while(!isStopRequested()) {
            servo.setPosition(1);
            ElapsedTime time = new ElapsedTime();
            while(time.milliseconds() < 1000) {}
            servo.setPosition(0);
            time.reset();
            while(time.milliseconds() < 1000) {}
        }
    }
}
