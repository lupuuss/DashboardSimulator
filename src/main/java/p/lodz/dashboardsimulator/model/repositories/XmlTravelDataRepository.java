package p.lodz.dashboardsimulator.model.repositories;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class XmlTravelDataRepository implements TravelDataRepository {

    private Serializer serializer;
    private List<Integer> existingDataIds = new ArrayList<>();
    private final String idsSerializationKey = "XmlRepositoryIds";

    private int nextId = 0;

    public XmlTravelDataRepository(Serializer serializer) {
        this.serializer = serializer;

        try {

            int[] ids = serializer.deserialize(idsSerializationKey, int[].class);

            existingDataIds.addAll(Arrays.stream(ids).boxed().collect(Collectors.toList()));

        } catch (DeserializationException e) {

            e.printStackTrace();
            System.out.print("XmlTravelDataRepository data not found!");
        }

        if (!existingDataIds.isEmpty()) {
            nextId = existingDataIds.get(existingDataIds.size() - 1);
        }

    }

    private boolean addNewId(int id) {

        try {
            existingDataIds.add(id);
            serializer.serialize(
                    idsSerializationKey,
                    existingDataIds.stream().mapToInt(Integer::intValue).toArray()
            );

            return true;

        } catch (SerializationException e) {
            e.printStackTrace();
            existingDataIds.remove(id);
            return false;
        }
    }

    private boolean removeId(int id) {
        try {

            existingDataIds.remove(id);
            serializer.serialize(
                    idsSerializationKey,
                    existingDataIds.stream().mapToInt(Integer::intValue).toArray()
            );

            return true;

        } catch (SerializationException e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public Observable<Boolean> addTravelStatistics(TravelStatistics travelStatistics) {
        return Observable.create(emitter -> {

            String key = String.valueOf(nextId);

            try {

                if (!addNewId(nextId)) {
                    emitter.onError(new IllegalStateException("Id could not be reserved!"));
                    return;
                }

                SignedTravelStatistics signedTravelStatistics = new SignedTravelStatistics(
                        nextId,
                        travelStatistics.getAvgSpeed(),
                        travelStatistics.getMaxSpeed(),
                        travelStatistics.getTravelTime(),
                        travelStatistics.getDistance(),
                        travelStatistics.getAvgFuelConsumption(),
                        new Date()
                );

                serializer.serialize(key, signedTravelStatistics);

                nextId++;

                emitter.onNext(true);

            } catch (SerializationException e) {
                emitter.onError(e);
            } finally {
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<SignedTravelStatistics>> getAllTravelStatistics() {
        return Observable.create(emitter -> {

            List<SignedTravelStatistics> signedTravelStatistics = new ArrayList<>();

            for (Integer id : existingDataIds) {

                SignedTravelStatistics row = serializer.deserialize(String.valueOf(id), SignedTravelStatistics.class);

                signedTravelStatistics.add(row);
            }

            emitter.onNext(signedTravelStatistics);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Boolean> removeTravelStatistics(int id) {
        return Observable.create(emitter -> {

            if (!removeId(id)) {
                emitter.onError(new IllegalStateException("Id couldn't be removed!"));
                emitter.onComplete();
                return;
            }

            serializer.removeSerialization(String.valueOf(id), SignedTravelStatistics.class);
            emitter.onNext(true);
            emitter.onComplete();
        });
    }
}
