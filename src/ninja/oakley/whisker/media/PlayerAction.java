package ninja.oakley.whisker.media;

import javafx.scene.image.Image;

public enum PlayerAction {
    PLAY("play.png"),
    PAUSE("pause.png");
    
    private final String name;
    private final Image image;
    
    private PlayerAction(String name){
        this.name = name;
        this.image = new Image(this.getClass().getResource("/javafx/" + this.name).toString());
    }
    
    public Image getImage(){
        return this.image;
    }
    
}
