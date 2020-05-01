package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.utils.AtomicDouble;

import java.util.concurrent.atomic.AtomicLong;

public class BasicStatisticsMonitor extends StatisticsMonitor {

    private AtomicLong travelTime = new AtomicLong(0);
    private AtomicDouble maxSpeed = new AtomicDouble(0.0);

    private AtomicLong avgCount = new AtomicLong(0);
    private AtomicDouble avgSpeed = new AtomicDouble(0.0);

    private AtomicDouble distance = new AtomicDouble(0.0);

    @Override
    public Observable<EngineStatistics> getCurrentStats() {
        return engineState
                .map(state -> {

                    long n = avgCount.getAndIncrement();

                    avgSpeed.set((avgSpeed.get() * n + state.getSpeed()) / (n + 1));

                    if (state.getSpeed() != 0.0) {
                        travelTime.addAndGet(state.getBetweenTicks());

                        double dist = (state.getSpeed() / (60 * 60 * 1000)) * state.getBetweenTicks();

                        distance.addAndGet(dist);
                    }

                    if (state.getSpeed() > maxSpeed.get()){
                        maxSpeed.set(state.getSpeed());
                    }

                    return new EngineStatistics(
                            avgSpeed.get(),
                            maxSpeed.get(),
                            travelTime.get(),
                            distance.get(),
                            0 // TODO
                    );
                });
    }

}
