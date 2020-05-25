package p.lodz.dashboardsimulator.model.player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains list of song to play.
 */
public abstract class Playlist {

    /**
     * Returns list of files containing songs.
     * @return All files that contains song from this playlist.
     */
    abstract List<File> getSongs();

    /**
     * Similar to {@link Playlist#getSongs()} but the list is shuffled.
      * @return Shuffled list of all songs.
     */
    List<File> getAllShuffled() {

        List<File> copy = new ArrayList<>(getSongs());

        Collections.shuffle(copy);

        return copy;
    }
}
