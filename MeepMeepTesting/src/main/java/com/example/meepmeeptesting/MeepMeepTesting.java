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

        Pose2d initialPose = new Pose2d(62, 0, Math.toRadians(180));
        Pose2d shootPose = new Pose2d(-20, 0, Math.toRadians(180));
        Pose2d boxPose = new Pose2d(38, 32, Math.toRadians(180));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep) // BotBuilder... say that again...
                .setConstraints(60, 60, Math.toRadians(0), Math.toRadians(0), 10)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(initialPose)
                        .waitSeconds(3)
                        .lineToLinearHeading(shootPose)
                       // .lineToLinearHeading(boxPose)
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