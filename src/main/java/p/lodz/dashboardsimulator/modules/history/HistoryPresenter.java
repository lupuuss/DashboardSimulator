package p.lodz.dashboardsimulator.modules.history;

import io.reactivex.disposables.Disposable;
import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.repositories.SignedTravelStatistics;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;
import p.lodz.dashboardsimulator.utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes logic behind {@link HistoryView}. Communicates view with {@link TravelDataRepository} using RxJava.
 */
public class HistoryPresenter extends Presenter<HistoryView> {

    private final String speedMetric = "km/h";
    private final String distanceMetric = "km";
    private final String fuelConsumptionMetric = "L/100km";
    private final int decimalPlaces = 2;

    private TravelDataRepository travelDataRepository;

    private List<Disposable> subscriptions = new ArrayList<>();

    private List<SignedTravelStatistics> currentStats = null;

    public HistoryPresenter(TravelDataRepository travelDataRepository) {

        this.travelDataRepository = travelDataRepository;
    }

    /**
     * Bounds view to the presenter. Performs a query to the database using {@link TravelDataRepository},
     * that receives all travel statistics.
     * @param view Instance of {@link View} that is bounded to this presenter.
     */
    @Override
    public void attach(HistoryView view) {
        super.attach(view);

        Disposable dataDisposable = travelDataRepository.getAllTravelStatistics()
                .observeOn(currentScheduler)
                .subscribe(this::displayStatistics, this::displayError);

        subscriptions.add(dataDisposable);
    }

    private void displayStatistics(List<SignedTravelStatistics> signedTravelStatistics) {

        currentStats = signedTravelStatistics;

        for (SignedTravelStatistics stat : currentStats) {
            view.addStatsToList(
                    "Avg Speed: " + Utils.round(stat.getAvgSpeed(), decimalPlaces) + " " + speedMetric
                    + " | Max speed: " + Utils.round(stat.getMaxSpeed(), decimalPlaces) + " " + speedMetric
                    + " | Distance: " + Utils.round(stat.getDistance(), decimalPlaces) + " " + distanceMetric
                    + " | Time: " + Utils.formatDuration(stat.getTravelTime())
                    + " | Avg fuel consumption: " + Utils.round(stat.getAvgFuelConsumption(), decimalPlaces) + " " + fuelConsumptionMetric
                    + " | Date: " + stat.getDate()
            );
        }
    }

    private void displayError(Throwable throwable) {

        if (throwable instanceof SQLException) {

            view.showMessage("SQL operation failed! \n\n" + throwable, View.MessageType.ERROR);
        } else {
            view.showMessage("Unexpected error occurred! \n\n" + throwable, View.MessageType.ERROR);
        }

        throwable.printStackTrace();
    }

    /**
     * Unsubscribes pending queries and detaches the view.
     */
    @Override
    public void detach() {
        super.detach();

        subscriptions.forEach(Disposable::dispose);
    }

    /**
     * Closes the view.
     */
    public void closeView() {
        view.close();
    }

    /**
     * Removes travel statistics with given index form database. If sucessful, statistics are also removed from view.
     * @param index Index of the stats to be removed from the view.
     */
    public void removeStats(int index) {

        Disposable removeDisposable = travelDataRepository
                .removeTravelStatistics(currentStats.get(index).getId())
                .observeOn(currentScheduler)
                .subscribe(
                        result -> {

                            if (result) {
                                currentStats.remove(index);
                                view.removeFromList(index);
                            } else {
                                view.showMessage("Record couldn't be removed!", View.MessageType.ERROR);
                            }

                            },
                        this::displayError
                );
        subscriptions.add(removeDisposable);
    }
}
