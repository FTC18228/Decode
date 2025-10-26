package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.IntakeDebugCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeKickCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeOffCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeOnCommand;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

@TeleOp(name="Intake Test", group="tests")
public class IntakeTestOpMode extends CommandOpMode {
    @Override
    public void initialize() {
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap);
        GamepadEx driver = new GamepadEx(gamepad1);

        intakeSubsystem.setDefaultCommand(new IntakeDebugCommand(intakeSubsystem, telemetry));

        driver.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(
                    new IntakeOnCommand(intakeSubsystem)
                )
                .whenReleased(
                    new IntakeOffCommand(intakeSubsystem)
                );

        driver.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(
                    new IntakeKickCommand(intakeSubsystem)
                );
    }
}
