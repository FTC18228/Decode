package org.firstinspires.ftc.teamcode.debug;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class VisionDebug implements DebugInfo<String, TagInfo> {
    Map<String, TagInfo> values;
    public VisionDebug(TagInfo... tagInfos) {
        String[] keys = new String[tagInfos.length];
        for(int i = 0; i < keys.length; i++) {
            keys[i] = tagInfos[i].getValue("id").toString();
        }
        setValues(keys, tagInfos);
    }
    @Override
    public void setValues(String[] keys, TagInfo[] values) {
        this.values = new HashMap<>();
        for(String key : keys) {
            this.values.put(key, values[Arrays.stream(keys).collect(Collectors.toList()).indexOf(key)]);
        }

    }
    @Override
    public Map<String, TagInfo> getInfo() {
        return values;
    }

    @Override
    public TagInfo getValue(String key) {
        return values.get(key);
    }

    @Override
    public void displayTelemetry(Telemetry telemetry) {
        telemetry.addLine("VISION DEBUG: ");
        telemetry.addData("Detected tags: ", values.size());
        for(String key : values.keySet()) {
            telemetry.addLine("Tag ID #" + key + ":");
            //noinspection DataFlowIssue
            values.get(key).displayTelemetry(telemetry, false);
            telemetry.addLine();
        }
        telemetry.update();
    }
}
