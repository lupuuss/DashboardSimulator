package p.lodz.dashboardsimulator.utils;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDouble {
    private AtomicLong value;

    public AtomicDouble(double initialValue) {
        this.value = new AtomicLong(Double.doubleToLongBits(initialValue));
    }

    public double get() {
        return Double.longBitsToDouble(value.get());
    }

    public void set(double value) {
        this.value.set(Double.doubleToLongBits(value));
    }

    public double addAndGet(double add) {
        return this.value.updateAndGet(i -> Double.doubleToLongBits(Double.longBitsToDouble(i) + add));
    }
}
