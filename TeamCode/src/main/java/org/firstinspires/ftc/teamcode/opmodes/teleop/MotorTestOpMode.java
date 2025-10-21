package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Motor Test", group="tests")
public class MotorTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor[] motors = {
                hardwareMap.get(DcMotor.class, "leftFront"),
                hardwareMap.get(DcMotor.class, "leftBack"),
                hardwareMap.get(DcMotor.class, "rightBack"),
                hardwareMap.get(DcMotor.class, "rightFront"),
                hardwareMap.get(DcMotor.class, "launch"),
                hardwareMap.get(DcMotor.class, "spinner")
        };
        Servo[] servos = {hardwareMap.get(Servo.class, "hood")};

        DcMotor selectMotor = motors[0];
        Servo selectServo = servos[0];
        boolean isServo = false;
        String selectedName = selectMotor.getDeviceName();
        int motorDeviceNum = 0;
        int servoDeviceNum = 0;

        waitForStart();
        while(!isStopRequested()) {
            if(isServo) selectedName = selectServo.getDeviceName();
            else selectedName = selectServo.getDeviceName();

            telemetry.addData("Currently selected device: ", selectedName);
            telemetry.addLine(" ");
            telemetry.addData("Selected type: ", isServo? "Servo" : "Motor");
            telemetry.addData("Selected data: ", isServo? selectServo.getPosition() : selectMotor.getPower());
            telemetry.addData("Selected device num: ", isServo? servoDeviceNum : motorDeviceNum);
            telemetry.update();

            if(gamepad1.aWasPressed()) {
                isServo = !isServo;
            }
            if(gamepad1.bWasPressed()) {
                if(isServo) {
                    servoDeviceNum++;
                    if(servoDeviceNum >= servos.length) servoDeviceNum = 0;
                    selectServo = servos[servoDeviceNum];
                }
                else {
                    motorDeviceNum++;
                    if(motorDeviceNum >= motors.length) motorDeviceNum = 0;
                    selectMotor = motors[motorDeviceNum];
                }
            }

            if(gamepad1.xWasPressed()) {
                if(isServo) selectServo.setPosition(1); else selectMotor.setPower(1);
            }

            if(gamepad1.yWasPressed()) {
                if(isServo) selectServo.setPosition(0); else selectMotor.setPower(0);
            }
        }
    }
}
