package ninja.oakley.whisker.menu;

import javafx.scene.image.ImageView;
import ninja.oakley.whisker.media.Media;

public class MediaImageView extends ImageView {

    private final static String CSS_CLASS = "sel";

    private final Media media;

    public MediaImageView(Media media){
        super(media.getCoverImage());
        this.media = media;
    }

    public void applyCSS(boolean value){
        if(value){
            this.getStyleClass().add(CSS_CLASS);
        } else {
            this.getStyleClass().remove(CSS_CLASS);
        }
    }

    public Media getMedia(){
        return media;
    }

}
