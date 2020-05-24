package p.lodz.dashboardsimulator.modules.player;

import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.model.player.FxPlaylistPlayer;
import p.lodz.dashboardsimulator.model.player.PlaylistPlayer;

/**
 * Contains model classes for player module.
 */
public class PlayerInjector implements Injector {

    private PlaylistPlayer playlistPlayer;

    /**
     * Initializes player with {@link FxPlaylistPlayer}.
     * @param parentInjector Parent injector expected to be null as this injector doesn't share any instances with other injectos.
     */
    @Override
    public void init(Injector parentInjector) {

        playlistPlayer = new FxPlaylistPlayer();
    }

    /**
     * Returns {@link PlaylistPlayer} implemented with {@link FxPlaylistPlayer}.
     * @return Instance of {@link FxPlaylistPlayer}.
     */
    public PlaylistPlayer getPlaylistPlayer() {
        return playlistPlayer;
    }
}
