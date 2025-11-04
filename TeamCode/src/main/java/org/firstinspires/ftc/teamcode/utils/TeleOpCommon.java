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
import org.firstinspires.ftc.teamcode.commands.TurretPose0Command;
import org.firstinspires.ftc.teamcode.commands.TurretPose1Command;
import org.firstinspires.ftc.teamcode.commands.TurretPose2Command;
import org.firstinspires.ftc.teamcode.commands.TurretPose3Command;
import org.firstinspires.ftc.teamcode.commands.TurretStopCommand;
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
        intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);
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

    public static synchronized void hardResetInstance() {
        instance = null;
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
                .whenActive(
                        new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();})
                )
                .whenInactive(
                        new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();})
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(
                        new OuttakeCommand(this.intakeSubsystem)
                )
                .whenReleased (
                        new IntakeOffCommand(this.intakeSubsystem)
                );


        this.driverGamepad.getGamepadButton(GamepadKeys.Button.Y)
                .toggleWhenPressed(
                        new InstantCommand(() -> {this.turretSubsystem.setPreset(.65, 0);}),
                        new TurretStopCommand(turretSubsystem)
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.X)
                .toggleWhenPressed(
                        new InstantCommand(() -> {this.turretSubsystem.setPreset(.65, .85);}),
                        new TurretStopCommand(turretSubsystem)
                );

        new Trigger(() -> {return this.driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) >= 0.5;})
                .whenActive(
                        new OuttakeCommand(this.intakeSubsystem)
        )
                .whenInactive (
                        new IntakeOffCommand(this.intakeSubsystem)
                );

        new Trigger(() -> {return this.driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) >= 0.5;})
                .whenActive(
                        new IntakeOnCommand(this.intakeSubsystem)
                )
                .whenInactive(
                        new IntakeOffCommand(this.intakeSubsystem)
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(
                    new InstantCommand(() -> {driveSubsystem.resetState();})
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(
                        new InstantCommand(() -> {this.turretSubsystem.setPreset(.65, 1);})//,
                        //new TurretStopCommand(turretSubsystem)
                );

        this.driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(
                        new InstantCommand(() -> {this.turretSubsystem.setPreset(.65, .92);})//,
                       // new TurretStopCommand(turretSubsystem)
                );
        this.driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(
                        new InstantCommand(() -> {this.turretSubsystem.setPreset(.65, .85);})//,
                        //new TurretStopCommand(turretSubsystem)
                );
        this.driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(
                        new InstantCommand(() -> {this.turretSubsystem.setPreset(.65, .75);})//,
                        //new TurretStopCommand(turretSubsystem)
                );
    }
}
