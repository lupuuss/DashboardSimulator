package p.lodz.dashboardsimulator.model.player;

import io.reactivex.Observable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;
import java.util.ListIterator;

public class FxPlaylistPlayer implements PlaylistPlayer {

    private List<File> songs;
    private ListIterator<File> currentSong;
    private Playlist playlist;

    private MediaPlayer currentPlayer;

    @Override
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        songs = playlist.getAllShuffled();
        currentSong = songs.listIterator();
    }

    @Override
    public Observable<String> getCurrentSongName() {
        return null;
    }

    void initNewPlayer(File song) {

        currentPlayer = new MediaPlayer(new Media(song.getAbsolutePath()));
    }

    void restartPlaylist() {
        currentSong = songs.listIterator();
    }

    private void assertPlaylist() {
        if (playlist == null) {
            throw new IllegalStateException("Playlist not set!");
        }
    }

    private void playNew(File file) {

        currentPlayer = new MediaPlayer(new Media(file.getAbsolutePath()));
    }

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
        }

    }

    @Override
    public void stop() {

        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer = null;
        }
    }

    @Override
    public void pause() {

        if (currentPlayer != null) {
            currentPlayer.pause();
        }
    }

    @Override
    public void next() {
        stop();

        play();
    }

    @Override
    public void prev() {
        stop();

        if (!currentSong.hasPrevious()) {

            currentSong = songs.listIterator(songs.size() - 1);
        }

        playNew(currentSong.previous());
    }
}
