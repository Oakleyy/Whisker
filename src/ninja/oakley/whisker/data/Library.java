package ninja.oakley.whisker.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import ninja.oakley.whisker.media.Media;

public class Library implements Documentable  {

    private final Map<ObjectId, Media> loadedMedia;
    private final List<ObjectId> unloadedMedia;

    private final String name;
    private final long creationTime;
    private final String desc;

    private Library(Document doc){
        this(doc.getString("name"), doc.getLong("creationTime"), doc.getString("description"));

        List<ObjectId> ids = (ArrayList<ObjectId>) doc.get("media");
        if(ids != null){
            unloadedMedia.addAll(ids);
        }
    }

    /**
     * Construct a new Library using the defined parameters.
     * 
     * @param name name of library
     * @param creationTime time of creation, -1 if now
     * @param desc description of the library
     */
    public Library(String name, long creationTime, String desc){
        this.loadedMedia = new HashMap<>();
        this.unloadedMedia = new ArrayList<>();
        this.name = name.replace("_", " ");
        this.creationTime = creationTime != -1 ? creationTime : System.currentTimeMillis();
        this.desc = desc;
    }

    @Override
    public String getUniqueId(){
        return name.replace(" ", "_");
    }

    /**
     * Get the name of the Library.
     * 
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the date of creation of this library.
     * 
     * @return date
     */
    public Date getCreationTime() {
        return new Date(creationTime);
    }

    /**
     * Get a small description of the library's purpose.
     * 
     * @return description
     */
    public String getDescription() {
        return this.desc;
    }

    /**
     * Get a list of all the identifiers of unloaded media.
     * 
     * @return list of ObjectId
     */
    public List<ObjectId> getUnloadedMedia(){
        return new ArrayList<>(unloadedMedia);
    }

    /**
     * Get a list of the currently loaded media.
     * Changes made to the list do not affect this instance.
     * 
     * @return list of media
     */
    public List<Media> getLoadedMedia() {
        return new ArrayList<>(loadedMedia.values());
    }

    /**
     * Used to unload media.
     * 
     * @param id id of object to unload
     * @return media object unloaded
     */
    public Media unload(ObjectId id) {
        unloadedMedia.add(id);
        return loadedMedia.remove(id);
    }

    /**
     * Remove a media object from loaded and unloaded.
     * 
     * @param id
     */
    public void delete(ObjectId id) {
        unloadedMedia.remove(id);
        loadedMedia.remove(id);
    }
    /**
     * Load media into this library.
     * 
     * @param m media
     * 
     * throws if media is aready loaded or isn't apart of this library
     */
    public void load(Media m){
        if(!unloadedMedia.contains(m.getUniqueId())){
            throw new RuntimeException("");
        }
        
        if(loadedMedia.containsKey(m.getUniqueId())){
            throw new RuntimeException("");
        }
        
        unloadedMedia.remove(m.getUniqueId());
        loadedMedia.put(m.getUniqueId(), m);
        
    }

    /**
     * Add media to this library,
     * 
     * @param m media
     * 
     */
    public void add(Media m) {
        if(loadedMedia.containsKey(m.getUniqueId())){
            return;
        }
        
        if(unloadedMedia.contains(m.getUniqueId())){
            load(m);
            return;
        }
        
        loadedMedia.put(m.getUniqueId(), m);
        
    }

    /**
     * Get the identifiers of all loaded and unloaded media.
     * 
     * @return list of object ids
     */
    private List<ObjectId> getAllMedia(){
        List<ObjectId> rt = new ArrayList<>();
        rt.addAll(unloadedMedia);
        rt.addAll(loadedMedia.keySet());

        return rt;
    }

    @Override
    public Document toDocument() {
        return new Document().append("_id", getUniqueId())
                .append("name", getName())
                .append("creationTime", getCreationTime())
                .append("description", getDescription())
                .append("mediaIds", getAllMedia());
    }
    
    /**
     * Factory for creating new insstances of Library from Documents.
     * 
     * @author oakley
     *
     */
    public static class LibraryFactory implements DocumentableFactory<Library> {

        @Override
        public Library newInstance(Document d) {
            return new Library(d);
        }
    }
}
