package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.auto.commands.FollowPathCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@Configurable
@Autonomous(name = "PedroTestRed | 6 AUTO")
public class PedroTestRed6 extends CommandOpMode {

    private Follower follower;

    // Subsystems
    private IntakeSubsystem intakeSubsystem;
    private TurretSubsystem turretSubsystem;
    private int launchTime = 650;
    private int recoveryTime = 999;

    // Starting pose (same as original)
    public static Pose startPose = new Pose(111.051, 134.237, Math.toRadians(0));

    // Paths
    private PathChain moveToLaunchPreload;
    private PathChain moveToIntakeSpikeLine;
    private PathChain intakeSpikeLine;
    private PathChain moveToLaunchSpikeLine;

    @Override
    public void initialize() {

        // ---- Subsystems ----
        intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);
        turretSubsystem = new TurretSubsystem(hardwareMap, telemetry, false);

        intakeSubsystem.register();
        turretSubsystem.register();


        // ---- Follower ----
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);


        // ---- PATH DEFINITIONS ----
        moveToLaunchPreload = follower
                .pathBuilder()
                .addPath(new BezierLine(
                        new Pose(111.051, 134.237),
                        new Pose(85.000, 134.237)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        moveToIntakeSpikeLine = follower
                .pathBuilder()
                .addPath(new BezierLine(
                        new Pose(85.000, 134.237),
                        new Pose(99.580, 86)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        intakeSpikeLine = follower
                .pathBuilder()
                .addPath(new BezierLine(
                        new Pose(99.580, 84.203),
                        new Pose(128.000, 86)))
                .setTangentHeadingInterpolation()
                .build();

        moveToLaunchSpikeLine = follower
                .pathBuilder()
                .addPath(new BezierLine(
                        new Pose(128.000, 84.203),
                        new Pose(85.000, 134.237)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();


        // ---- WAIT FOR START LOOP ----
        while (!isStarted() && !isStopRequested()) {
            telemetry.addLine("PedroTestRed Ready");
            telemetry.update();
        }


        // ---- MAIN AUTO SEQUENCE ----
        CommandScheduler.getInstance().schedule(

                new SequentialCommandGroup(

                        // PRELOAD
                        new InstantCommand(() -> {
                            turretSubsystem.setPreset(0.77, 0.67);
                            intakeSubsystem.intakeOn();
                        }),

                        new FollowPathCommand(follower, moveToLaunchPreload, true),
                        new WaitCommand(1000),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),



                        // INTAKE SPIKE LINE
                        new FollowPathCommand(follower, moveToIntakeSpikeLine, true),

                        new ParallelCommandGroup(
                                new FollowPathCommand(follower, intakeSpikeLine, true, 0.75),
                                new InstantCommand(() -> intakeSubsystem.intakeOn())
                        ),
                        new WaitCommand(750),

                        // RETURN TO LAUNCH POSITION
                        new FollowPathCommand(follower, moveToLaunchSpikeLine, true, 0.9),

                        new FollowPathCommand(follower, moveToLaunchPreload, true),
                        new WaitCommand(1000),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),
                        new WaitCommand(recoveryTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStartWheel()),
                        new WaitCommand(launchTime),
                        new InstantCommand(() -> intakeSubsystem.visionlessStopWheel()),

                        new WaitCommand(1500),

                        new InstantCommand(() -> intakeSubsystem.intakeOff()),
                        new InstantCommand(() -> turretSubsystem.setPreset(0.75, 0.0)),

                        new WaitCommand(200),

                        // DONE
                        new InstantCommand(() -> telemetry.addLine("Auto Complete"))
                )
        );
    }
}
