package org.firstinspires.ftc.teamcode.debug;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TagInfo implements DebugInfo<String, Double>{
    Map<String, Double> values;
    public TagInfo(int id, double distance, double bearing, double fixedDistance) {
        String[] keys = {"id", "distance", "bearing", "fixed distance", "distance difference"};
        Double[] vals = {(double) id, distance, bearing, fixedDistance, distance - fixedDistance};
        setValues(keys, vals);
    }

    @Override
    public Map<String, Double> getInfo() {
        return values;
    }

    @Override
    public Double getValue(String key) {
        return values.get(key);
    }

    @Override
    public void displayTelemetry(Telemetry telemetry) {
        for(String key : values.keySet()) {
            telemetry.addData(key, values.get(key));
        }
        telemetry.update();
    }

    public void displayTelemetry(Telemetry telemetry, boolean updateTelemetry) {
        for(String key : values.keySet()) {
            telemetry.addData(key, values.get(key));
        }
        if(updateTelemetry) telemetry.update();
    }

    @Override
    public void setValues(String[] keys, Double[] values) {
        this.values = new HashMap<>();
        for(String key : keys) {
            this.values.put(key, values[Arrays.stream(keys).collect(Collectors.toList()).indexOf(key)]);
        }
    }
}
