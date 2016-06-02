package ninja.oakley.whisker.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import ninja.oakley.whisker.Documentable;
import ninja.oakley.whisker.DocumentableFactory;
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

    public Library(String name, long creationTime, String desc){
        this.loadedMedia = new HashMap<>();
        this.unloadedMedia = new ArrayList<>();
        this.name = name.replace("_", "");
        this.creationTime = creationTime;
        this.desc = desc;
    }

    @Override
    public String getUniqueId(){
        return name.replace(" ", "_");
    }

    public String getName() {
        return this.name;
    }

    public Date getCreationTime() {
        return new Date(creationTime);
    }

    public String getDescription() {
        return this.desc;
    }

    public List<ObjectId> getUnloadedMedia(){
        return new ArrayList<>(unloadedMedia);
    }

    public List<Media> getLoadedMedia() {
        return new ArrayList<>(loadedMedia.values());
    }

    public Media unload(ObjectId id) {
        unloadedMedia.add(id);
        return loadedMedia.remove(id);
    }

    public void delete(ObjectId id) {
        unloadedMedia.remove(id);
        loadedMedia.remove(id);
    }
    
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
    
    public static class LibraryFactory implements DocumentableFactory<Library> {

        @Override
        public Library newInstance(Document d) {
            return new Library(d);
        }
        
    }
    
}
