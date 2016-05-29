package ninja.oakley.whisker;

import java.util.ArrayList;
import java.util.List;

import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.menu.Library;

public final class Configuration {
    
    private final DatabaseController databaseController;

    private final String systemUser = "pi";
    private final String systemGroup = "pi";

    private final List<Library> libraries;
    private final List<Media> media;

    public Configuration() {
        this.databaseController = new DatabaseController();
        this.media = new ArrayList<>();
        this.libraries = new ArrayList<>();
    }
    
    public void saveMedia(Media m){
        media.add(m);
        databaseController.addMedia(m);
    }
    
    public void deleteMedia(Media m){
    }
    
    public void getMedia(){
        media.addAll(databaseController.getMediaList());
    }
    
    public void updateMedia(){
        databaseController.updateAllMedia(media);
    }

    public String getSystemUser(){
        return this.systemUser;
    }

    public String getSystemGroup(){
        return this.systemGroup;
    }


}
