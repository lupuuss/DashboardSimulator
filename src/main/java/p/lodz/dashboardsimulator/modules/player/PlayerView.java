package p.lodz.dashboardsimulator.modules.player;

import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.base.View;

public interface PlayerView extends View<PlayerPresenter> {

    void setCurrentSongName(String name);

    void setPlayingState(boolean isPlaying);
}
