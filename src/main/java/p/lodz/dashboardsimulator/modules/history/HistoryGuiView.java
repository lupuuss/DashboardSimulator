package p.lodz.dashboardsimulator.modules.history;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import p.lodz.dashboardsimulator.base.GlobalInjector;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.base.JavaFxView;

import java.util.function.Consumer;

/**
 * Implements {@link HistoryView} using JavaFX. Displays a {@link ListView} that contains travels statistics history.
 * Button is placed next to every data row that allows to remove it.
 */
public class HistoryGuiView extends JavaFxView<HistoryPresenter> implements HistoryView {

    @FXML private ListView<String> statsListView;

    private ObservableList<String> statsList = FXCollections.observableArrayList();

    private HistoryPresenter presenter;

    /**
     * Initializes presenter using {@link GlobalInjector} passed as parameter.
     * @param injector Implementation of injector that provides necessary components for presenter.
     */
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

    /**
     * Closes the view and detaches the presenter.
     */
    @Override
    public void close() {
        presenter.detach();
        super.close();
    }

    /**
     * Notifes presenter about user intent to close the view.
     */
    @Override
    public void notifyCloseEvent() {
        presenter.closeView();
    }

    /**
     * Adds passed travel statistics {@link String} to {@link ListView}.
     * @param statsString {@link String} that describes travel statistics.
     */
    @Override
    public void addStatsToList(String statsString) {
        statsList.add(statsString);
    }

    /**
     * Removes travel statistics with given index from {@link ListView}
     * @param index Index of travel statistics to be removed from view.
     */
    @Override
    public void removeFromList(int index) {
        statsList.remove(index);
    }
}
