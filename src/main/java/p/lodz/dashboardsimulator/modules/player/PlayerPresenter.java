package p.lodz.dashboardsimulator.modules.player;

import io.reactivex.disposables.Disposable;
import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.model.player.DirPlaylist;
import p.lodz.dashboardsimulator.model.player.PlayerState;
import p.lodz.dashboardsimulator.model.player.Playlist;
import p.lodz.dashboardsimulator.model.player.PlaylistPlayer;

/**
 * Describes logic behind {@link PlayerView}. Communicates view with {@link PlaylistPlayer}.
 */
public class PlayerPresenter extends Presenter<PlayerView> {

    private PlaylistPlayer player;
    private Playlist playlist;
    private final String playlistDir = ".\\playlist";
    private Disposable subscription;

    /**
     * Injects player instance.
     * @param player Implementation of {@link PlaylistPlayer}
     */
    public PlayerPresenter(PlaylistPlayer player) {
        this.player = player;
    }

    /**
     * Attaches corresponding view to presenter.
     * @param view Instance of {@link PlayerView} that is bounded to this presenter.
     */
    @Override
    public void attach(PlayerView view) {
        super.attach(view);

        playlist = new DirPlaylist(playlistDir);

        player.setPlaylist(playlist);
        subscription = player.getPlayerState()
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

    /**
     * Detaches view from the presenter and unsubscribes player changes.
     */
    @Override
    public void detach() {

        subscription.dispose();

        player.stop();
        super.detach();
    }

    /**
     * Stops player.
     */
    public void stopSong() {
        player.stop();
    }

    /**
     * Skips current song.
     */
    public void skipSong() {
        player.next();
    }

    /**
     * Switches between play and pause, depending on the current state.
     * If the player is playing music, this method pauses the player. Otherwise, it plays the music.
     */
    public void playPause() {

        if (player.isPlaying()) {
            player.pause();
        } else {
            player.play();
        }
    }

    /**
     * Plays previous song.
     */
    public void previousSong() {
        player.prev();
    }
}
