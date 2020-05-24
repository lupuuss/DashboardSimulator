package p.lodz.dashboardsimulator.modules.player;

import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.model.player.DirPlaylist;
import p.lodz.dashboardsimulator.model.player.PlayerState;
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
        player.getPlayerState()
                .observeOn(currentScheduler)
                .subscribe(this::updateStateOnView);
    }

    private void updateStateOnView(PlayerState playerState) {

        if (playerState.getCurrentSongName() == null) {
            view.setCurrentSongName("-");
        } else {
            view.setCurrentSongName(playerState.getCurrentSongName());
        }
        view.setPlayingState(playerState.isPlaying());
    }

    public void closeView() {
        view.close();
    }

    @Override
    public void detach() {
        player.stop();
        super.detach();
    }

    public void stopSong() {
        player.stop();
    }

    public void skipSong() {
        player.next();
    }

    public void playPause() {

        if (player.isPlaying()) {
            player.pause();
        } else {
            player.play();
        }
    }

    public void previousSong() {
        player.prev();
    }
}
