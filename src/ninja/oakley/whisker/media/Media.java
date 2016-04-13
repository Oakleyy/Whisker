package ninja.oakley.whisker.media;

import java.nio.file.Path;
import java.util.UUID;

import javafx.scene.image.Image;

public interface Media {
    
    public UUID getUniqueId();
    
    public Image getCoverImage();
    
    public String getName();
    
    public Path getPath();
    
    public String getDescription();
    
    public String getAuthor();
}
