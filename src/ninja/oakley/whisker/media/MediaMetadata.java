package ninja.oakley.whisker.media;

import javafx.scene.image.Image;

/**
 * Interface for different types of metadata.
 * 
 * @author oakley
 *
 */
public interface MediaMetadata {
    
    /**
     * Cover image of the media file.
     * @return image
     */
    public Image getCoverImage();
    
    /**
     * Get a short description of the media.
     * 
     * @return description.
     */
    public String getDescription();
}
