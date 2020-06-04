package p.lodz.dashboardsimulator.model.monitor.statistics;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.monitor.odometer.BasicOdometer;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Basic implementation of {@link StatisticsMonitor} that provides statistics
 * updated every {@link p.lodz.dashboardsimulator.model.engine.Engine} update.
 */
public class BasicStatisticsMonitor extends StatisticsMonitor {

    private long travelTime = 0;
    private double maxSpeed = 0;

    private long avgSpeedCount = 0;
    private double avgSpeed = 0;

    private long avgFuelCount = 0;
    private double avgFuel = 0;

    private double distance = 0;
    private AtomicReference<TravelStatistics> travelStatisticsAtomicReference = new AtomicReference<>(null);

    private final Serializer serializer;

    private final String serializationKey = "backup";
    private Observable<TravelStatistics> currentStats;

    /**
     * Empty BasicStatistcsMonitor with passed serializer. Before any action {@link BasicStatisticsMonitor#watch(Engine)} must be called.
     * @param serializer Implementation of serializer that is required to persist statistics.
     */
    public BasicStatisticsMonitor(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Loads persisted statistics and initializes engine subscription. After this method call engine is considered monitored.
     * @param engine Instance of {@link Engine} that will be monitored.
     */
    @Override
    public void watch(Engine engine) {

        TravelStatistics previousState = null;
        AvgCounters avgCounters = null;

        try {

            previousState = serializer.deserialize(serializationKey, TravelStatistics.class);
            avgCounters = serializer.deserialize(serializationKey, AvgCounters.class);

        } catch (DeserializationException e) {
            e.printStackTrace();
        }

        if (previousState != null && avgCounters != null) {
            travelTime = previousState.getTravelTime();
            maxSpeed = previousState.getMaxSpeed();
            avgSpeedCount = avgCounters.getAvgSpeedCounter();
            avgSpeed = previousState.getAvgSpeed();
            distance = previousState.getDistance();
        }

        super.watch(engine);

        currentStats = engineState
                .map(state -> {

                    avgSpeed = (avgSpeed * avgSpeedCount + state.getSpeed()) / ++avgSpeedCount;
                    avgFuel = (avgFuel * avgFuelCount + state.getFuelConsumption()) / ++avgFuelCount;

                    if (state.getSpeed() != 0.0) {

                        travelTime += state.getBetweenTicks();
                        distance += (state.getSpeed() / (60 * 60 * 1000)) * state.getBetweenTicks();
                    }

                    if (state.getSpeed() > maxSpeed){
                        maxSpeed = state.getSpeed();
                    }

                    TravelStatistics tmp = new TravelStatistics(
                            avgSpeed,
                            maxSpeed,
                            travelTime,
                            distance,
                            avgFuel
                    );

                    travelStatisticsAtomicReference.set(tmp);

                    return tmp;
                });
    }

    /**
     * Persists current statistics for next run.
     */
    @Override
    public void closeAndSave() {


        try {

            serializer.serialize(serializationKey, travelStatisticsAtomicReference.get());
            serializer.serialize(serializationKey, new AvgCounters(avgSpeedCount, avgFuelCount));

        } catch (SerializationException e) {
            System.out.println("Statistics data could not be saved!");
            e.printStackTrace();
        }
    }

    /**
     * Returns observable travel statistics.
     * @return Instance of {@link Observable} that provides update on every engine update {@link TravelStatistics}.
     */
    @Override
    public Observable<TravelStatistics> getCurrentStats() {
        return currentStats;
    }

    /**
     * Returns last calculated statistics
     * @return Last calculated statistics.
     */
    @Override
    public TravelStatistics getLastStatistics() {
        return travelStatisticsAtomicReference.get();
    }
}

class AvgCounters {

    private final long avgSpeedCounter;
    private final long avgFuelCounter;

    AvgCounters(long avgSpeedCounter, long avgFuelCounter) {
        this.avgSpeedCounter = avgSpeedCounter;
        this.avgFuelCounter = avgFuelCounter;
    }

    public long getAvgSpeedCounter() {
        return avgSpeedCounter;
    }

    public long getAvgFuelCounter() {
        return avgFuelCounter;
    }
}
