package ninja.oakley.whisker.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import ninja.oakley.whisker.media.Media;

public class LibraryManager {

    private final Map<String, Library> libraries;
    private final Map<ObjectId, Media> wildMedia;

    public LibraryManager(){
        this.libraries = new HashMap<>();
        this.wildMedia = new HashMap<>();
    }

    public List<Library> getLibraries(){
        return new ArrayList<>(libraries.values());
    }

    public void loadLibrary(Library lib){
        if(libraries.containsKey(lib.getIdentifier())){
            throw new IllegalArgumentException("Library already loaded."); 
        }

        for(ObjectId id : lib.getUnloadedMedia()){
            if(wildMedia.containsKey(id)){
                lib.add(wildMedia.remove(id));
            }
        }
    }

    public void addWildMedia(List<Media> media){
        for(Media m : media){
            wildMedia.put(m.getUniqueId(), m);
        }  
    }

    public void addWildMedia(Media media){
        wildMedia.put(media.getUniqueId(), media);
    }

    public void moveMedia(Media media, Library library){
        wipeoutMedia(media);
        library.add(media);
    }

    private Media wipeoutMedia(Media media){
        wildMedia.remove(media.getUniqueId());

        for(Library lib : getLibraries()){
            lib.delete(media.getUniqueId());
        }

        return media;
    }

}
