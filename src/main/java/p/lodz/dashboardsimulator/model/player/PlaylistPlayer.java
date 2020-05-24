package p.lodz.dashboardsimulator.model.player;

import io.reactivex.Observable;

public interface PlaylistPlayer {

    void setPlaylist(Playlist playlist);

    Observable<String> getCurrentSongName();

    void play();

    void stop();

    void pause();

    void next();

    void prev();
}
