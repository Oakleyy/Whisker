package ninja.oakley.whisker.menu;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import ninja.oakley.whisker.media.Media;

public interface Library {

    public String getIdentifier();
    
    public String getName();

    public Date getCreationTime();

    public String getDescription();

    public List<Media> getLoadedMedia();
    
    public List<ObjectId> getUnloadedMedia();

    public void add(Media media);

    public void delete(ObjectId id);

    public void load(Media m);
    
    public Media unload(ObjectId id);
}
