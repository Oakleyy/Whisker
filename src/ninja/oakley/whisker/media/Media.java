package ninja.oakley.whisker.media;

import java.nio.file.Path;

import javafx.scene.image.Image;

public interface Media {
    
    public Image getCoverImage();
    
    public String getName();
    
    public Path getPath();
    
    public String getDescription();
    
    public String getAuthor();
}
