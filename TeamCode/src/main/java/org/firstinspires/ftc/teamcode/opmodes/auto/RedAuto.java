package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.commands.ActionCommand;
import org.firstinspires.ftc.teamcode.commands.TurretPose3Command;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.utils.TeleOpCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Autonomous(name="Red side auto")
@Disabled
public class RedAuto extends CommandOpMode {
    private static final Logger log = LoggerFactory.getLogger(RedAuto.class);

    @Override
    public void initialize() {
        Pose2d initialPose = new Pose2d(62, 0, Math.toRadians(180));
        Pose2d shootPose = new Pose2d(-20, 22, Math.toRadians(135));
        Pose2d boxPose = new Pose2d(38, -32, Math.toRadians(180));

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        TurretSubsystem turretSubsystem = new TurretSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);

        TrajectoryActionBuilder shootActionBuilder = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(shootPose.position, shootPose.heading);

        Action shootAction = shootActionBuilder.build();
        Action boxAction = shootActionBuilder.fresh()
                .strafeToLinearHeading(boxPose.position, boxPose.heading)
                .build();

        CommandScheduler.getInstance().schedule(
                new WaitUntilCommand(this::isStarted).andThen(
                        new SequentialCommandGroup(
                                new ActionCommand(shootAction, telemetry),
                                new InstantCommand(() -> {turretSubsystem.setPreset(.65, .75);}),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();}),
                                new WaitCommand(1000),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();}),
                                new WaitCommand(2500),
                                new InstantCommand(() -> {turretSubsystem.setPreset(.65, .75);}),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();}),
                                new WaitCommand(1000),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();}),
                                new WaitCommand(2500),
                                new InstantCommand(() -> {turretSubsystem.setPreset(.65, .75);}),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();}),
                                new WaitCommand(1000),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();}),
                                new WaitCommand(2500),
                                new ActionCommand(boxAction, telemetry)
                        )
                )
        );
    }
}
