package ninja.oakley.whisker.media;

import java.nio.file.Path;
import java.util.UUID;

import javafx.scene.image.Image;

public abstract class AbstractMedia implements Media {

    private String name;
    private Path path;
    private Path imagePath;
    private UUID uniqueId;

    public AbstractMedia(Path imagePath){
        this.imagePath = imagePath;
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
        return null;
    }

    @Override
    public String getAuthor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Image getCoverImage() {
        return new Image("file:" + imagePath.toAbsolutePath().toString());
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }
}
