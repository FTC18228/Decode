package org.firstinspires.ftc.teamcode.debug;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;

public interface DebugInfo<K, V> {
    Map<K, V> getInfo();
    V getValue(K key);
    void displayTelemetry(Telemetry telemetry);
    void setValues(K[] keys, V[] values);
}
