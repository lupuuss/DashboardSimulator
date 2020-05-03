package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;
import p.lodz.dashboardsimulator.utils.AtomicDouble;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicMileageMonitor extends MileageMonitor {

    private AtomicDouble totalMileage;
    private List<AtomicDouble> resettableMileage;
    private int resettableCount;

    private Serializer serializer;

    private final String serializationKey = "backup";

    public BasicMileageMonitor(Serializer serializer, int resettableCount) {
        this.serializer = serializer;
        this.resettableCount = resettableCount;
    }

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
    }

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

    @Override
    public int getResettableCount() {
        return resettableMileage.size();
    }

    @Override
    public Observable<Mileage> getMileage() {
        return engineState.map(state -> {

            double dist = (state.getSpeed() / (60 * 60 * 1000)) * state.getBetweenTicks();

            double total = totalMileage.addAndGet(dist);
            List<Double> resettable = resettableMileage
                    .stream()
                    .map(value -> value.addAndGet(dist))
                    .collect(Collectors.toList());

            return new Mileage(total, resettable);
        });
    }

    @Override
    public void resetMileage(int n) {
        resettableMileage.get(n).set(0.0);
    }
}
