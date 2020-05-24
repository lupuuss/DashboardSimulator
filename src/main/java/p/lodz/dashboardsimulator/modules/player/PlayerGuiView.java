package p.lodz.dashboardsimulator.modules.player;

import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.base.JavaFxView;

public class PlayerGuiView extends JavaFxView<PlayerPresenter> implements PlayerView {

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
}
