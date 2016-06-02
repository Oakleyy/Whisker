package ninja.oakley.whisker.menu;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import ninja.oakley.whisker.DatabaseController;
import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.media.Media.MediaFactory;
import ninja.oakley.whisker.menu.Library.LibraryFactory;

public class LibraryManager {

    private final DatabaseController<Library, LibraryFactory> libraryDatabase;
    private final DatabaseController<Media, MediaFactory> mediaDatabase;

    private final Map<String, Library> libraries;
    private final Map<ObjectId, Media> wildMedia;

    public LibraryManager(){
        this.libraryDatabase = new DatabaseController<>(new Library.LibraryFactory(), "media", "library");
        this.mediaDatabase = new DatabaseController<>(new Media.MediaFactory(), "media", "media");
        this.libraries = new HashMap<>();
        this.wildMedia = new HashMap<>();
    }

    public List<Library> getLibraries(){
        return new ArrayList<>(libraries.values());
    }

    public void initLibraries(){
        for(Library lib : libraryDatabase.getList()){
            loadLibrary(lib);
        }  
    }

    public void initMedia(){
        addWildMedia(mediaDatabase.getList());
    } 

    public void loadLibrary(Library lib){
        if(libraries.containsKey(lib.getUniqueId())){
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

    public void processRawPaths(List<Path> paths){
        for(Path p : paths){
            System.out.println(p);
        }
    }

    private Media wipeoutMedia(Media media){
        wildMedia.remove(media.getUniqueId());

        for(Library lib : getLibraries()){
            lib.delete(media.getUniqueId());
        }

        return media;
    }

}