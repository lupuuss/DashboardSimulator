package p.lodz.dashboardsimulator.model.player;

public class PlayerState {
    private String currentSongName;
    private boolean playing;

    public PlayerState(String currentSongName, boolean playing) {
        this.currentSongName = currentSongName;
        this.playing = playing;
    }

    public String getCurrentSongName() {
        return currentSongName;
    }

    public boolean isPlaying() {
        return playing;
    }
}
