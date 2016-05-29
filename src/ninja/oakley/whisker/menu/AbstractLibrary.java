package ninja.oakley.whisker.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import ninja.oakley.whisker.media.Documentable;
import ninja.oakley.whisker.media.Media;

public abstract class AbstractLibrary extends Documentable implements Library  {

    private final Map<ObjectId, Media> loadedMedia;
    private final List<ObjectId> unloadedMedia;

    private final String name;
    private final long creationTime;
    private final String desc;

    public AbstractLibrary(Document doc){
        this(doc.getString("name"), doc.getLong("creationTime"), doc.getString("description"));

        List<ObjectId> ids = (ArrayList<ObjectId>) doc.get("media");
        if(ids != null){
            unloadedMedia.addAll(ids);
        }
    }

    public AbstractLibrary(String name, long creationTime, String desc){
        this.loadedMedia = new HashMap<>();
        this.unloadedMedia = new ArrayList<>();
        this.name = name.replace("_", "");
        this.creationTime = creationTime;
        this.desc = desc;
    }

    public String getIdentifier(){
        return name.replace(" ", "_");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Date getCreationTime() {
        return new Date(creationTime);
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ObjectId> getUnloadedMedia(){
        return new ArrayList<>(unloadedMedia);
    }

    @Override
    public List<Media> getLoadedMedia() {
        return new ArrayList<>(loadedMedia.values());
    }

    @Override
    public Media unload(ObjectId id) {
        unloadedMedia.add(id);
        return loadedMedia.remove(id);
    }

    @Override
    public void delete(ObjectId id) {
        unloadedMedia.remove(id);
        loadedMedia.remove(id);
    }
    
    @Override
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

    @Override
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
        return new Document().append("_id", getIdentifier())
                .append("name", getName())
                .append("creationTime", getCreationTime())
                .append("description", getDescription())
                .append("mediaIds", getAllMedia());
    }
}
