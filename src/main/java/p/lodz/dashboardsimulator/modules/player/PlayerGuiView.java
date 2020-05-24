package p.lodz.dashboardsimulator.modules.player;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.base.JavaFxView;

/**
 * Implements {@link PlayerView} using JavaFx
 */
public class PlayerGuiView extends JavaFxView<PlayerPresenter> implements PlayerView {

    @FXML private Polygon playMusicButton;
    @FXML private Pane pauseMusicButton;
    @FXML private Label currentSongName;

    private PlayerPresenter presenter;

    @Override
    public void start(Injector injector) {

        PlayerInjector playerInjector = (PlayerInjector) injector;

        presenter = new PlayerPresenter(
                playerInjector.getPlaylistPlayer()
        );

        presenter.attach(this);
    }

    @Override
    public void notifyCloseEvent() {
        presenter.closeView();
    }

    @Override
    public void close() {
        presenter.detach();
        super.close();
    }

    @Override
    public void setCurrentSongName(String name) {
        currentSongName.setText(name);
    }

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
