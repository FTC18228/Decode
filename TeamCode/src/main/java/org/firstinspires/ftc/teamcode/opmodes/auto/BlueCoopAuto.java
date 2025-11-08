package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Autonomous(name="14380 coop auto")
public class BlueCoopAuto extends CommandOpMode {
    private static final Logger log = LoggerFactory.getLogger(BlueCoopAuto.class);

    public void backwardsSet(MecanumDrive drive) {
        double speed = -0.4;
        drive.leftFront.setPower(speed);
        drive.rightFront.setPower(speed);
        drive.leftBack.setPower(speed);
        drive.rightBack.setPower(speed);
    }
    public void zeroSet(MecanumDrive drive) {
        drive.leftFront.setPower(0);
        drive.rightFront.setPower(0);
        drive.leftBack.setPower(0);
        drive.rightBack.setPower(0);
    }

    public void rturn(MecanumDrive drive) {
        double speed = 0.6;
        drive.leftFront.setPower(speed);
        drive.leftBack.setPower(speed);
        drive.rightFront.setPower(-speed);
        drive.rightBack.setPower(-speed);
    }

    @Override
    public void initialize() {
        Pose2d initialPose = new Pose2d(62, 0, Math.toRadians(180));
        Pose2d shootPose = new Pose2d(70, 0, Math.toRadians(180));
        Pose2d boxPose = new Pose2d(38, 32, Math.toRadians(180));

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        TurretSubsystem turretSubsystem = new TurretSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);

        turretSubsystem.setPreset(1, 0);

        TrajectoryActionBuilder shootActionBuilder = drive.actionBuilder(initialPose)
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(shootPose, Math.toRadians(180))
                .endTrajectory();

        TrajectoryActionBuilder boxActionBuilder = shootActionBuilder.fresh()
                .setTangent(Math.toRadians(-45))
                .splineToLinearHeading(boxPose, Math.toRadians(180))
                .endTrajectory();

        CommandScheduler.getInstance().schedule(
                new WaitUntilCommand(this::isStarted).andThen(
                        new SequentialCommandGroup(
                                new InstantCommand(() -> {intakeSubsystem.intakeOn();}),
                                new InstantCommand(() -> {backwardsSet(drive);}),
                                new WaitCommand(1500),
                                new InstantCommand(() -> {zeroSet(drive);}),
                                new InstantCommand(() -> {turretSubsystem.setPreset(0.75, .75);}),
                                new WaitCommand(2000),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();}),
                                new WaitCommand(750),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();}),
                                new WaitCommand(1000),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();}),
                                new WaitCommand(750),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();}),
                                new WaitCommand(1000),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();}),
                                new WaitCommand(750),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();}),
                                new WaitCommand(1000),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();}),
                                new WaitCommand(750),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();}),
                                new WaitCommand(1000),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStartWheel();}),
                                new WaitCommand(750),
                                new InstantCommand(() -> {intakeSubsystem.visionlessStopWheel();}),
                                new WaitCommand(1000),
                                new InstantCommand(() -> {intakeSubsystem.intakeOff();})
                        )
                )
        );
    }

}
