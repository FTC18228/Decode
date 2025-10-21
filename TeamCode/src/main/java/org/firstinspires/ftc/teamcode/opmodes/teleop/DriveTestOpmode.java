package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.DefaultDrive;
import org.firstinspires.ftc.teamcode.commands.IntakeOnCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

@TeleOp(name="Drive Test", group="tests")
public class DriveTestOpmode extends CommandOpMode {
    @Override
    public void initialize() {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap, new Pose2d(0, 0, 0), 5);
        GamepadEx driverGamepad = new GamepadEx(gamepad1);

        drive.setDefaultCommand(new DefaultDrive(
                drive,
                () -> {return driverGamepad.getLeftX();},
                () -> {return driverGamepad.getLeftY();},
                () -> {return driverGamepad.getRightX();},
                telemetry
        ));
    }
}
