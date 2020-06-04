package p.lodz.dashboardsimulator.model.monitor.odometer;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;
import p.lodz.dashboardsimulator.utils.AtomicDouble;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Basic implementation of Odometer. It monitors every “tick” of an engine and calculates traveled distance.
 */
public class BasicOdometer extends Odometer {

    private AtomicDouble totalMileage;
    private List<AtomicDouble> resettableMileage;
    private int resettableCount;

    private Serializer serializer;

    private final String serializationKey = "backup";
    private Observable<Mileage> mileage;

    /**
     * Empty BasicOdometer with passed serializer. Before any action {@link BasicOdometer#watch(Engine)} must be called.
     * @param serializer Implementation of serializer that is required to persist mileage data.
     * @param resettableCount Amount of resettable mileages.
     */
    public BasicOdometer(Serializer serializer, int resettableCount) {
        this.serializer = serializer;
        this.resettableCount = resettableCount;
    }

    /**
     * Loads persisted mileage data and initializes engine subscription. After this method call engine is considered monitored.
     * @param engine Instance of {@link Engine} that will be monitored.
     */
    @Override
    public void watch(Engine engine) {

        Mileage mileageBackup = null;

        try {

            mileageBackup = serializer.deserialize(serializationKey, Mileage.class);

        } catch (DeserializationException e) {
            System.out.println("No previous state found!");
        }

        if (mileageBackup == null) {
            totalMileage = new AtomicDouble(0.0);
            resettableMileage = Stream
                    .generate(() -> new AtomicDouble(0.0))
                    .limit(resettableCount)
                    .collect(Collectors.toList());
        } else {
            totalMileage = new AtomicDouble(mileageBackup.getTotalMileage());
            resettableMileage = mileageBackup
                    .getResettableMileages()
                    .stream()
                    .map(AtomicDouble::new)
                    .collect(Collectors.toList());
        }

        super.watch(engine);

        mileage = engineState.map(state -> {

            double dist = (state.getSpeed() / (60 * 60 * 1000)) * state.getBetweenTicks();

            double total = totalMileage.addAndGet(dist);
            List<Double> resettable = resettableMileage
                    .stream()
                    .map(value -> value.addAndGet(dist))
                    .collect(Collectors.toList());

            return new Mileage(total, resettable);
        });
    }

    /**
     * As its mentioned in {@link Odometer#closeAndSave()}, it persists produced data.
     */
    @Override
    public void closeAndSave() {

        List<Double> resettable = resettableMileage
                .stream()
                .map(AtomicDouble::get)
                .collect(Collectors.toList());

        try {
            serializer.serialize(serializationKey, new Mileage(totalMileage.get(), resettable));
        } catch (SerializationException e) {
            System.out.println("Mileage couldn't be saved!");
            e.printStackTrace();
        }
    }

    /**
     * Returns amount of resettable mileages.
     * @return Amount of resettable mileages.
     */
    @Override
    public int getResettableCount() {
        return resettableMileage.size();
    }

    /**
     * Returns observable mileage.
     * @return Instance of {@link Observable} that allows to watch {@link Mileage}
     */
    @Override
    public Observable<Mileage> getMileage() {
        return mileage;
    }

    /**
     * Resets chosen mileage.
     * @param n Index of resettable odometer that we want to reset.
     */
    @Override
    public void resetMileage(int n) {
        resettableMileage.get(n).set(0.0);
    }
}
