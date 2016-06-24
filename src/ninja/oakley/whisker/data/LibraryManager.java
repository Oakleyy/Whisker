package ninja.oakley.whisker.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import ninja.oakley.whisker.data.Library.LibraryFactory;
import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.media.Media.MediaFactory;

/**
 * Used to manage all of the media and libraries present.
 * 
 * 
 * @author oakley
 *
 */
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

    /**
     * Get a list of all of the libraries.
     * 
     * @return list of libraries
     */
    public List<Library> getLibraries(){
        return new ArrayList<>(libraries.values());
    }

    /**
     * Load library media.
     * 
     */
    public void initLibraries(){
        for(Library lib : libraryDatabase.getList()){
            loadLibrary(lib);
        }  
    }

    /**
     * Load media from database.
     * 
     */
    public void initMedia(){
        addWildMedia(mediaDatabase.getList());
    }

    /**
     * Add all of the media into the library.
     * 
     * @param lib
     */
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

    /**
     * Add wild media for sorting.
     * 
     * @param media
     */
    public void addWildMedia(List<Media> media){
        for(Media m : media){
            wildMedia.put(m.getUniqueId(), m);
        }  
    }

    /**
     * Add wild media for sorting.
     * 
     * @param media
     */
    public void addWildMedia(Media media){
        wildMedia.put(media.getUniqueId(), media);
    }

    /**
     * Movie media to another library.
     * 
     * @param media
     * @param library destination
     */
    public void moveMedia(Media media, Library library){
        wipeoutMedia(media);
        library.add(media);
    }

    /**
     * Load media from path.
     * 
     * @param paths
     */
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