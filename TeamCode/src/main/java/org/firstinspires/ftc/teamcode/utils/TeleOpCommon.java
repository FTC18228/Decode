package org.firstinspires.ftc.teamcode.utils;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.DefaultDrive;
import org.firstinspires.ftc.teamcode.commands.IntakeOffCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeOnCommand;
import org.firstinspires.ftc.teamcode.commands.OuttakeCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

public class TeleOpCommon {
    static TeleOpCommon instance;
    public IntakeSubsystem intakeSubsystem;
    public DriveSubsystem driveSubsystem;
    public TurretSubsystem turretSubsystem;
    public GamepadEx driverGamepad;
    Telemetry telemetry;
    private TeleOpCommon(HardwareMap hardwareMap, Pose2d defaultPose, Gamepad gamepad1, Telemetry telemetry) {
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap, defaultPose, 10);
        turretSubsystem = new TurretSubsystem(hardwareMap);
        driverGamepad = new GamepadEx(gamepad1);
        this.telemetry = telemetry;

        this.resetInstanceState();
    }

    public static synchronized TeleOpCommon getInstance(HardwareMap hardwareMap, Pose2d defaultPose, Gamepad gamepad1, Telemetry telemetry) {
        if(instance == null) instance = new TeleOpCommon(hardwareMap, defaultPose, gamepad1, telemetry);
        return instance;
    }

    public static synchronized TeleOpCommon getInstance() {
        if(instance == null) throw new IllegalStateException("getInstance without args called before with args to create it");
        return instance;
    }

    public void resetInstanceState() {
        driveSubsystem.resetState();
        driverGamepad.gamepad.reset();

        this.driveSubsystem.setDefaultCommand(new DefaultDrive(
                this.driveSubsystem,
                () -> {return -this.driverGamepad.getLeftX();},
                () -> {return -this.driverGamepad.getLeftY();},
                () -> {return this.driverGamepad.getRightX();},
                telemetry
        ));

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(
                        new IntakeOnCommand(this.intakeSubsystem)
                )
                .whenReleased(
                        new IntakeOffCommand(this.intakeSubsystem)
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(
                        new OuttakeCommand(this.intakeSubsystem)
                )
                .whenReleased (
                        new IntakeOffCommand(this.intakeSubsystem)
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(
                        new InstantCommand(() -> {this.intakeSubsystem.visionlessOpenGate();})
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(
                        new InstantCommand(() -> {this.intakeSubsystem.visionlessCloseGate();})
                );

        new Trigger(() -> {return this.driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) >= 0.5;})
                .whenActive(
                        new InstantCommand(() -> {this.turretSubsystem.fireTurret();})
                )
                .whenInactive(
                        new InstantCommand(() -> {this.turretSubsystem.stopTurret();})
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(
                    new InstantCommand(() -> {this.resetInstanceState();})
                );
    }
}
