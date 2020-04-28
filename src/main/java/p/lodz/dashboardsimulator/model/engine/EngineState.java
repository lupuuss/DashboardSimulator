package p.lodz.dashboardsimulator.model.engine;

public class EngineState {

    private double speed;
    private long betweenTicks;

    public EngineState(double speed, long betweenTicks) {
        this.speed = speed;
        this.betweenTicks = betweenTicks;
    }

    public double getSpeed() {
        return speed;
    }

    public long getBetweenTicks() {
        return betweenTicks;
    }
}
