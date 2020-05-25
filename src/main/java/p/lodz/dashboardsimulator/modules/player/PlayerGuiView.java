package p.lodz.dashboardsimulator.modules.player;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.base.JavaFxView;

/**
 * Implements {@link PlayerView} using JavaFx. It displays standard music player to the user.
 * Shows current song name and allows to navigate between songs (previous/next), pause and stop music.
 */
public class PlayerGuiView extends JavaFxView<PlayerPresenter> implements PlayerView {

    @FXML private Polygon playMusicButton;
    @FXML private Pane pauseMusicButton;
    @FXML private Label currentSongName;

    private PlayerPresenter presenter;

    /**
     * Initializes {@link PlayerPresenter} using {@link PlayerInjector} and attaches itself to the presenter.
     * @param injector Expected to be instance of {@link PlayerInjector} that provides {@link p.lodz.dashboardsimulator.model.player.PlaylistPlayer} for presenter.
     */
    @Override
    public void start(Injector injector) {

        PlayerInjector playerInjector = (PlayerInjector) injector;

        presenter = new PlayerPresenter(
                playerInjector.getPlaylistPlayer()
        );

        presenter.attach(this);
    }

    /**
     * Notifies presenter about close event.
     */
    @Override
    public void notifyCloseEvent() {
        presenter.closeView();
    }

    /**
     * Detaches itself from the presenter and closes the view.
     */
    @Override
    public void close() {
        presenter.detach();
        super.close();
    }

    /**
     * Sets current song name on {@link Label} with id = currentSongName.
     * @param name Name of the current song.
     */
    @Override
    public void setCurrentSongName(String name) {
        currentSongName.setText(name);
    }

    /**
     * Hides play button with id = playMusicButton and shows pauseMusicButton if music is playing.
     * Otherwise, reverses the process.
     * @param isPlaying Determines if music is playing or not.
     */
    @Override
    public void setPlayingState(boolean isPlaying) {

        playMusicButton.setVisible(!isPlaying);
        pauseMusicButton.setVisible(isPlaying);
    }

    @FXML
    private void notifyPresenterSkipSong() {
        presenter.skipSong();
    }

    @FXML
    private void notifyPresenterStop() {
        presenter.stopSong();
    }

    @FXML
    private void notifyPresenterPlayPause() {
        presenter.playPause();
    }

    @FXML
    private void notifyPresenterPreviousSong() {
        presenter.previousSong();
    }
}
