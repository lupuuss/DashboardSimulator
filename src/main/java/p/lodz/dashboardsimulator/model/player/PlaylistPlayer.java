package p.lodz.dashboardsimulator.model.player;

import io.reactivex.Observable;

/**
 * Describe a player that plays whole playlist and allows to navigate between song (previous/next).
 */
public interface PlaylistPlayer {

    /**
     * Sets current playlist.
     * @param playlist Instance of {@link Playlist} that contains list of files to play.
     */
    void setPlaylist(Playlist playlist);

    /**
     * Returns {@link Observable} that allows to watch current player state.
     * @return {@link Observable} that allows to watch current player state.
     */
    Observable<PlayerState> getPlayerState();

    /**
     * Plays current song from the list.
     */
    void play();

    /**
     * Stops the music and restarts the playlist.
     */
    void stop();

    /**
     * Pause the music.
     */
    void pause();

    /**
     * Skips current song. On the end of the playlist goes to the first song.
     */
    void next();

    /**
     * Goes to previous song. On the beginning of the playlist goes to the last song.
     */
    void prev();

    /**
     * Returns true if song is being played.
     * @return True if song is being played.
     */
    boolean isPlaying();
}
