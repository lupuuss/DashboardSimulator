package p.lodz.dashboardsimulator.modules.history;

import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import p.lodz.dashboardsimulator.base.GlobalInjector;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.base.JavaFxView;

import java.util.function.Consumer;

public class HistoryGuiView extends JavaFxView<HistoryPresenter> implements HistoryView {

    @FXML private ListView<String> statsListView;

    private ObservableList<String> statsList = FXCollections.observableArrayList();

    private HistoryPresenter presenter;

    @Override
    public void start(Injector injector) {

        GlobalInjector globalInjector = (GlobalInjector) injector;

        presenter = new HistoryPresenter(globalInjector.getTravelDataRepository());

        Consumer<Integer> onClick = (index) -> {
            presenter.removeStats(index);
        };

        statsListView.setCellFactory((listView) -> new HistoryCell(onClick));
        statsListView.setItems(statsList);

        presenter.attach(this);
    }

    @Override
    public void close() {
        presenter.detach();
    }

    @Override
    public Scheduler getViewScheduler() {
        return JavaFxScheduler.platform();
    }

    @Override
    public void notifyCloseEvent() {
        presenter.closeView();
    }

    @Override
    public void addStatsToList(String statsString) {
        statsList.add(statsString);
    }

    @Override
    public void removeFromList(int index) {
        statsList.remove(index);
    }
}
