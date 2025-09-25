package org.firstinspires.ftc.teamcode.debug;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;

public interface DebugInfo<K, V> {
    public Map<K, V> getInfo();
    public V getValue(K key);
    public void displayTelemetry(Telemetry telemetry);
    public void setValues(K[] keys, V[] values);
}
