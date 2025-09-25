package org.firstinspires.ftc.teamcode.debug;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PinpointDebug implements DebugInfo<String, Pose2d> {
    Map<String, Pose2d> values;
    public PinpointDebug(Pose2d txWorld, Pose2d txRobot) {
        String[] keys = {"txWorld", "txRobot"};
        Pose2d[] vals = {txWorld, txRobot};
        setValues(keys, vals);
    }
    @Override
    public void setValues(String[] keys, Pose2d[] values) {
        this.values = new HashMap<>();
        for(String key : keys) {
            this.values.put(key, values[Arrays.stream(keys).collect(Collectors.toList()).indexOf(key)]);
        }

    }
    @Override
    public Map<String, Pose2d> getInfo() {
        return values;
    }

    @Override
    public Pose2d getValue(String key) {
        return values.get(key);
    }

    @Override
    public void displayTelemetry(Telemetry telemetry) {
        for(String key : values.keySet()) {
            telemetry.addData(key, values.get(key));
        }
        telemetry.update();
    }
}
