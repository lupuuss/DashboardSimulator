package p.lodz.dashboardsimulator.modules.player;

import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.model.player.FxPlaylistPlayer;
import p.lodz.dashboardsimulator.model.player.PlaylistPlayer;

public class PlayerInjector implements Injector {

    private PlaylistPlayer playlistPlayer;

    @Override
    public void init(Injector parentInjector) {

        playlistPlayer = new FxPlaylistPlayer();
    }

    public PlaylistPlayer getPlaylistPlayer() {
        return playlistPlayer;
    }
}
