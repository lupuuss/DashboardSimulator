package p.lodz.dashboardsimulator.model.player;

import p.lodz.dashboardsimulator.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DirPlaylist extends Playlist {

    private List<File> songs;

    public DirPlaylist(String root) {

        File dir = new File(root);

        if (dir.isDirectory() && dir.listFiles() != null) {

            this.songs =  Arrays
                    .stream(Objects.requireNonNull(dir.listFiles()))
                    .filter(file -> {
                        String extension = Utils.getFileExtension(file).orElse("");

                        return extension.equals("mp3") || extension.equals("wav");
                    }).collect(Collectors.toList());
        } else {
            songs = new ArrayList<>();
            System.out.print("[Warning] empty playlist!");
        }
    }

    @Override
    List<File> getSongs() {
        return songs;
    }
}
