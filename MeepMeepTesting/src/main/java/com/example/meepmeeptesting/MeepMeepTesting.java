package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d initialPose = new Pose2d(61, 0, 0);
        Pose2d artifact1Pose = new Pose2d(36, 35, Math.toRadians(90));
        Pose2d artifact2Pose = new Pose2d(12, 35, Math.toRadians(90));
        Pose2d artifact3Pose = new Pose2d(-11, 35, Math.toRadians(90));
        Pose2d shootPose = new Pose2d(-20, 22, Math.toRadians(135));
        Pose2d interpolatePose = new Pose2d(12, 28, Math.toRadians(105));
        Pose2d interpolatePose2 = new Pose2d(-10, 28, Math.toRadians(90));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep) // BotBuilder... say that again...
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 10)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(initialPose)
                        .lineToLinearHeading(artifact1Pose)
                        .waitSeconds(1)
                        .forward(10)
                        .waitSeconds(0.5)
                        .waitSeconds(1)
                        .lineToLinearHeading(interpolatePose)
                        .lineToLinearHeading(shootPose)
                        .waitSeconds(3)
                        .splineToLinearHeading(artifact2Pose, artifact2Pose.getHeading())
                        .waitSeconds(1)
                        .forward(10)
                        .waitSeconds(0.5)
                        .lineToLinearHeading(interpolatePose2)
                        .lineToLinearHeading(shootPose)
                        .waitSeconds(3)
                        .splineToLinearHeading(artifact3Pose, artifact3Pose.getHeading())
                        .waitSeconds(1)
                        .forward(10)
                        .waitSeconds(0.5)
                        .lineToLinearHeading(shootPose)
                        .waitSeconds(3)
                        .build());

        Image background = null;
        try {
            background = ImageIO.read(new File("MeepMeepTesting/src/main/java/com/example/meepmeeptesting/decode.png"));
        } catch (IOException ignored) {}
        assert background != null;

        meepMeep.setBackground(background)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}