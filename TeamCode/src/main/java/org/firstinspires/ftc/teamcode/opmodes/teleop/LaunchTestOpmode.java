package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.commands.TurretShootCommand;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import java.util.function.BooleanSupplier;

//@Disabled
@TeleOp(name="Launch Test", group="tests")
public class LaunchTestOpmode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        MotorEx motor = new MotorEx(hardwareMap, "motor");

        waitForStart();

        while(opModeIsActive()) {
            if(gamepad1.left_trigger >= 0.5 || gamepad1.right_trigger >= 0.5) {
                motor.set(1);
            }
            else {motor.set(0);}
            telemetry.addData("Motor tps: ", motor.getVelocity());
            telemetry.update();
        }
    }
}
