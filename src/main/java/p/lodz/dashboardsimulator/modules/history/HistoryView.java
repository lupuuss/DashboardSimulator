package p.lodz.dashboardsimulator.modules.history;

import p.lodz.dashboardsimulator.base.View;

/**
 * Describes interactions with view shown to the user in history module.
 * Every {@link View} bounded to {@link HistoryPresenter} must implement this interface.
 */
public interface HistoryView extends View<HistoryPresenter> {

    /**
     * Adds a {@link String} describing travel statistics to the view.
     * @param statsString {@link String} that describes travel statistics.
     */
    void addStatsToList(String statsString);

    /**
     * Removes the view that describes travel statistics with given index.
     * @param index Index of travel statistics to be removed from view.
     */
    void removeFromList(int index);
}
