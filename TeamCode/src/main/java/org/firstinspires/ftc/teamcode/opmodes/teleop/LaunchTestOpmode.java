package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Launch Test", group="tests")
public class LaunchTestOpmode extends CommandOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor turret = hardwareMap.get(DcMotor.class, "motor");

        waitForStart();

        while(opModeIsActive()) {
            if(gamepad1.left_trigger >= 0.5 || gamepad1.right_trigger >= 0.5) {
                turret.setPower(1);
            }
            else {turret.setPower(0);}
        }
    }

    @Override
    public void initialize() {

    }
}
