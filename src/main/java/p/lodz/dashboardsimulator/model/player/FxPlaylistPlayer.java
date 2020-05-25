package p.lodz.dashboardsimulator.model.player;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;
import java.util.ListIterator;

/**
 * Implements {@link PlaylistPlayer} using MediaPlayer form JavaFx library.
 */
public class FxPlaylistPlayer implements PlaylistPlayer {

    private List<File> songs;
    private ListIterator<File> currentSong;
    private Playlist playlist;

    private MediaPlayer currentPlayer;
    private Subject<PlayerState> playerStateSubject = PublishSubject.create();
    private String currentSongName;
    private boolean playing = false;

    /**
     * Sets current playlist.
     * @param playlist Instance of {@link Playlist} that contains list of files to play.
     */
    @Override
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        songs = playlist.getAllShuffled();
        currentSong = songs.listIterator();
    }

    /**
     * Returns {@link Observable} that allows to watch current player state.
     * @return {@link Observable} that allows to watch current player state.
     */
    @Override
    public Observable<PlayerState> getPlayerState() {
        return playerStateSubject;
    }

    private void restartPlaylist() {
        currentSong = songs.listIterator();
    }

    private void assertPlaylist() {
        if (playlist == null) {
            throw new IllegalStateException("Playlist not set!");
        }
    }

    private void publishState() {
        playerStateSubject.onNext(new PlayerState(currentSongName, playing));
    }

    private void playNew(File file) {

        if (currentPlayer != null) {
            currentPlayer.setOnEndOfMedia(null);
        }

        currentPlayer = new MediaPlayer(new Media(file.toURI().toString()));
        currentSongName = file.getName();
        playing = true;
        currentPlayer.play();
        currentPlayer.setOnEndOfMedia(this::next);
        publishState();
    }

    /**
     * Plays current song from the list.
     */
    @Override
    public void play() {

        assertPlaylist();

        if (currentPlayer == null && !currentSong.hasNext()) {

            restartPlaylist();
            playNew(currentSong.next());

        } else if (currentPlayer == null) {

            playNew(currentSong.next());

        } else {
            currentPlayer.play();
            playing = true;
            publishState();
        }

    }

    /**
     * Stops the music and restarts the playlist.
     */
    @Override
    public void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer = null;
        }
        playing = false;
        publishState();
    }

    /**
     * Pause the music.
     */
    @Override
    public void pause() {
        if (currentPlayer != null) {
            currentPlayer.pause();
        }
        playing = false;
        publishState();
    }

    /**
     * Skips current song. On the end of the playlist goes to the first song.
     */
    @Override
    public void next() {
        stop();
        play();
    }


    /**
     * Goes to previous song. On the beginning of the playlist goes to the last song.
     */
    @Override
    public void prev() {
        stop();

        if (!currentSong.hasPrevious()) {

            currentSong = songs.listIterator(songs.size() - 1);
        }

        playNew(currentSong.previous());
        currentPlayer.play();
    }

    /**
     * Returns true if song is being played.
     * @return True if song is being played.
     */
    @Override
    public boolean isPlaying() {
        return playing;
    }
}
