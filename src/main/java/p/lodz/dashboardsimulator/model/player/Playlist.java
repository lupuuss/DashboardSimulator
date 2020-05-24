package p.lodz.dashboardsimulator.model.player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Playlist {

    abstract List<File> getSongs();

    List<File> getAllShuffled() {

        List<File> copy = new ArrayList<>(getSongs());

        Collections.shuffle(copy);

        return copy;
    }
}
