package p.lodz.dashboardsimulator.modules.history;

import p.lodz.dashboardsimulator.base.View;

public interface HistoryView extends View<HistoryPresenter> {

    void addStatsToList(String statsString);

    void removeFromList(int index);
}
