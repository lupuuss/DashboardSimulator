package p.lodz.dashboardsimulator.model.player;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
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
    private Subject<PlayerState> playerStateSubject = PublishSubject.create();
    private String currentSongName;
    private boolean playing = false;

    @Override
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        songs = playlist.getAllShuffled();
        currentSong = songs.listIterator();
    }

    @Override
    public Observable<PlayerState> getPlayerState() {
        return playerStateSubject;
    }

    void restartPlaylist() {
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

    @Override
    public void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer = null;
        }
        playing = false;
        publishState();
    }

    @Override
    public void pause() {
        if (currentPlayer != null) {
            currentPlayer.pause();
        }
        playing = false;
        publishState();
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
        currentPlayer.play();
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }
}
