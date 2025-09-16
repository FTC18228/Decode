package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.commands.TurretShootCommand;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import java.util.function.BooleanSupplier;

@TeleOp(name="Launch Test", group="tests")
public class LaunchTestOpmode extends CommandOpMode {

    @Override
    public void initialize() {
        GamepadEx gamepad = new GamepadEx(gamepad1);
        TurretSubsystem turretSubsystem = new TurretSubsystem(hardwareMap);

        new Trigger(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5;
            }
        }).whenActive(
                new InstantCommand(turretSubsystem::fireTurret)
        ).whenInactive(
                new InstantCommand(turretSubsystem::stopTurret)
        );

        new Trigger(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5;
            }
        }).whenActive(
                new InstantCommand(turretSubsystem::fireTurret)
        ).whenInactive(
                new InstantCommand(turretSubsystem::stopTurret)
        );
    }
}
