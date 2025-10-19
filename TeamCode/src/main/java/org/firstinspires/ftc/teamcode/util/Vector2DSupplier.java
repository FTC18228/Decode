package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.roadrunner.Vector2d;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class Vector2DSupplier implements Supplier {
    final DoubleSupplier xSupplier;
    final DoubleSupplier ySupplier;
    public Vector2DSupplier(DoubleSupplier x, DoubleSupplier y) {
        xSupplier = x;
        ySupplier = y;
    }
    @Override
    public Vector2d get() {
        return new Vector2d(xSupplier.getAsDouble(), ySupplier.getAsDouble());
    }
}
