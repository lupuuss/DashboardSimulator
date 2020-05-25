package p.lodz.dashboardsimulator.model.player;

/**
 * Represents current state of the {@link PlaylistPlayer}.
 */
public class PlayerState {
    private String currentSongName;
    private boolean playing;

    /**
     * Initializes object with given parameters.
     * @param currentSongName Current song played by the {@link PlaylistPlayer}
     * @param playing Determines if {@link PlaylistPlayer} is playing.
     */
    public PlayerState(String currentSongName, boolean playing) {
        this.currentSongName = currentSongName;
        this.playing = playing;
    }

    /**
     * Returns current song played by the {@link PlaylistPlayer}
     * @return Current song played by the {@link PlaylistPlayer}
     */
    public String getCurrentSongName() {
        return currentSongName;
    }

    /**
     * Returns true if player is playing music.
     * @return True if player is playing music.
     */
    public boolean isPlaying() {
        return playing;
    }
}
