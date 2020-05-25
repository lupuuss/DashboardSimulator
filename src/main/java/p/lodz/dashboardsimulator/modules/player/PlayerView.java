package p.lodz.dashboardsimulator.modules.player;

import p.lodz.dashboardsimulator.base.View;

/**
 * Describes interactions with view shown to the user in player module.
 * Every {@link View} bounded to {@link PlayerPresenter} must implement this class.
 */
public interface PlayerView extends View<PlayerPresenter> {

    /**
     * Changes current song name on the view.
     * @param name Name of the current song.
     */
    void setCurrentSongName(String name);

    /**
     * Changes playing indicator on the view.
     * @param isPlaying Determines if music is playing or not.
     */
    void setPlayingState(boolean isPlaying);
}
