package ninja.oakley.whisker.menu;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import ninja.oakley.whisker.media.Media;

public class MediaImageView extends ImageView {

    private final static DropShadow ds = new DropShadow(20, Color.CORNFLOWERBLUE);

    private final Media media;

    public MediaImageView(Media media){
        super(media.getMediaMetadata().getCoverImage());
        this.media = media;
    }

    public void applyEffect(boolean value){
        if(value){
            this.setEffect(ds);
        } else {
            this.setEffect(null);
        }
    }

    public Media getMedia(){
        return media;
    }

}
