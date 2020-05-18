package p.lodz.dashboardsimulator.modules.history;

import io.reactivex.disposables.Disposable;
import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.repositories.SignedTravelStatistics;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;

import java.sql.SQLException;
import java.util.List;

public class HistoryPresenter extends Presenter<HistoryView> {

    private TravelDataRepository travelDataRepository;

    private Disposable dataDisposable;

    private List<SignedTravelStatistics> currentStats = null;

    public HistoryPresenter(TravelDataRepository travelDataRepository) {

        this.travelDataRepository = travelDataRepository;
    }

    @Override
    public void attach(HistoryView view) {
        super.attach(view);

        dataDisposable = travelDataRepository.getAllTravelStatistics()
                .observeOn(currentScheduler)
                .subscribe(this::displayStatistics, this::displayError);
    }

    private void displayStatistics(List<SignedTravelStatistics> signedTravelStatistics) {

        currentStats = signedTravelStatistics;

        for (SignedTravelStatistics stat : currentStats) {
            view.addStatsToList(
                    "Avg Speed: " + stat.getAvgSpeed() + "km/h"
                    + " | Max speed: " + stat.getMaxSpeed() + "km/h"
                    + " | Distance: " + stat.getDistance() + "km"
                    + " | Time: " + stat.getTravelTime() + "ms"
                    + " | Avg fuel consumption: " + stat.getAvgFuelConsumption() + "L/KM"
            );
        }
    }

    private void displayError(Throwable throwable) {

        if (throwable instanceof SQLException) {

            view.showMessage("Reading records failed! \n\n" + throwable, View.MessageType.ERROR);
        } else {
            view.showMessage("Unexpected error occurred! \n\n" + throwable, View.MessageType.ERROR);
        }

        throwable.printStackTrace();
    }

    @Override
    public void detach() {
        super.detach();

        if (dataDisposable != null) {
            dataDisposable.dispose();
        }
    }

    public void closeView() {
        view.close();
    }
}
