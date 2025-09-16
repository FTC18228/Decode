package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Launch Test", group="tests")
public class LaunchTestOpmode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor leftTurret = hardwareMap.get(DcMotor.class, "turretLeft");
        DcMotor rightTurret = hardwareMap.get(DcMotor.class, "turretRight");

        waitForStart();

        while(opModeIsActive()) {
            if(gamepad1.left_trigger >= 0.5) {
                leftTurret.setPower(-   1);
            }
            else {leftTurret.setPower(0);}
            if(gamepad1.right_trigger >= 0.5) {
                rightTurret.setPower(1);
            }
            else {rightTurret.setPower(0);}
        }
    }
}
