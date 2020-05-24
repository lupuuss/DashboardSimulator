package p.lodz.dashboardsimulator.modules.player;

import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.model.player.DirPlaylist;
import p.lodz.dashboardsimulator.model.player.Playlist;
import p.lodz.dashboardsimulator.model.player.PlaylistPlayer;

public class PlayerPresenter extends Presenter<PlayerView> {

    private PlaylistPlayer player;
    private Playlist playlist;
    private final String playlistDir = ".\\playlist";

    public PlayerPresenter(PlaylistPlayer player) {
        this.player = player;
    }

    @Override
    public void attach(PlayerView view) {
        super.attach(view);

        playlist = new DirPlaylist(playlistDir);

        player.setPlaylist(playlist);
    }

    public void closeView() {
        view.close();
    }

    @Override
    public void detach() {
        super.detach();


    }
}
