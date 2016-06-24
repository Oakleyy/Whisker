package ninja.oakley.whisker.media;

import java.nio.file.Path;

import javafx.scene.image.Image;

public class MovieMetadata implements MediaMetadata {

    private final Path image;
    private final String desc;
    
    public MovieMetadata(Path image, String desc){
        this.image = image;
        this.desc = desc;
    }
    
    @Override
    public Image getCoverImage() {
        return new Image("file:" + image.toAbsolutePath().toString());
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

}
