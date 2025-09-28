package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.debug.TagInfo;
import org.firstinspires.ftc.teamcode.debug.VisionDebug;
import org.firstinspires.ftc.teamcode.vision.AprilTagUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VisionSubsystem extends SubsystemBase {
    //TODO: Make vision do something vision-y
    public VisionSubsystem(HardwareMap hardwareMap) {
        AprilTagUtil.initialize(hardwareMap);
    }

    public enum TagIDs {
        BLUEGOAL(20),
        REDGOAL(24),
        OBELISK1(21),
        OBELISK2(22),
        OBELISK3(23);


        private final int id;

        private TagIDs(int id) {
            this.id = id;
        }
    }

    public int checkObeliskState() {
        //if (cantSeeObelisk) return -1;
        //else return obeliskPosition12or3
        return 1;
    }

    public double getDistanceToTarget(boolean onBlue) {
        int desiredTagID;
        if(onBlue) desiredTagID = TagIDs.BLUEGOAL.id;
        else desiredTagID = TagIDs.REDGOAL.id;

        if(!isTagActive(desiredTagID)) return -1;

        return 1;
    }

    public boolean isTagActive(int tag) {
        List<Integer> idList = Arrays.stream(AprilTagUtil.getDetectedIDs()).boxed().collect(Collectors.toList()); // Java arrays pmo
        return idList.contains(tag);
    }

    public double getTagDistance(int tag) {
        AprilTagUtil.TagDistance rawDistance = AprilTagUtil.getTagDistance(tag);
        if(isInvalidDistance(rawDistance)) return -1;
        return rotationAdjustment(rawDistance);
    }

    public VisionDebug getVisionInfo() {
        AprilTagUtil.detectTags();
        TagInfo[] tagInfos = new TagInfo[AprilTagUtil.getAmountDetected()];
        int i = 0;
        for(int id : AprilTagUtil.getDetectedIDs()) {
            AprilTagUtil.TagDistance tagDistance = AprilTagUtil.getTagDistance(id);
            tagInfos[i] = new TagInfo(id, tagDistance.range, tagDistance.bearing);
        }
        return new VisionDebug(tagInfos);
    }

    double rotationAdjustment(AprilTagUtil.TagDistance distance) {
        Vector2d g = new Vector2d(-distance.range * Math.cos(distance.bearing), distance.range * Math.sin(distance.bearing) + Constants.Hardware.turretSpinnerRadius);
        Vector2d n = new Vector2d(-Constants.Hardware.turretSpinnerRadius * Math.cos(90 - distance.bearing), Constants.Hardware.turretSpinnerRadius * Math.sin(90 - distance.bearing));
        return Math.sqrt(Math.pow(Math.abs(g.y) - Math.abs(n.y), 2) + Math.pow(Math.abs(g.x) - Math.abs(n.x), 2));
    }

    public boolean isInvalidDistance(double distance) {
        return distance == -1;
    }

    public boolean isInvalidDistance(AprilTagUtil.TagDistance distance) {
        return AprilTagUtil.TagDistance.isInvalid(distance);
    }
}
