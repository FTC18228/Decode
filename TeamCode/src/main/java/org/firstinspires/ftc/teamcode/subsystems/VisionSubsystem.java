package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.vision.AprilTagUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VisionSubsystem extends SubsystemBase {
    //TODO: Make vision do something vision-y
    public VisionSubsystem() {

    }

    public enum TagIDs {
        BLUEGOAL(20),
        REDGOAL(24),
        OBELISK1(21),
        OBELISK2(22),
        OBELISK3(23);


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
        //if (onBlue) return distanceToBlueGoal;
        //else return distanceToRedGoal
        return 1;
    }

    public boolean isTagActive(int tag) {
        List<Integer> idList = Arrays.stream(AprilTagUtil.getDetectedIDs()).boxed().collect(Collectors.toList()); // Java arrays pmo
        return idList.contains(tag);
    }

    public boolean getTagDistance(int tag) {

    }
}
