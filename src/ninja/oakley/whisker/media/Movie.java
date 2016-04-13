package ninja.oakley.whisker.media;

import java.nio.file.Path;
import java.util.UUID;

import javafx.scene.image.Image;

public class Movie implements Media {

    private String name;
    private Path path;
    private UUID uniqueId;

    public Movie(){
        
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Path getPath() {
        return this.path;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAuthor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Image getCoverImage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

}
