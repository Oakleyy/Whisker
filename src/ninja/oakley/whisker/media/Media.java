package ninja.oakley.whisker.media;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.bson.Document;
import org.bson.types.ObjectId;

import javafx.scene.image.Image;
import ninja.oakley.whisker.Documentable;
import ninja.oakley.whisker.DocumentableFactory;

public class Media implements Documentable {

    private final String name;
    private final MediaType type;
    private final Path path;
    private final ObjectId uniqueId;
    private MediaMetadata meta;

    public Media(Document doc){
        this(doc.getString("name"), Paths.get(doc.getString("path")), MediaType.valueOf(doc.getString("type")), doc.getObjectId("_id"));
    }
    
    public Media(String name, Path path, MediaType type, ObjectId uniqueId){
        this.name = name;
        this.path = path;
        this.uniqueId = uniqueId;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public Path getPath() {
        return this.path;
    }
    
    public MediaMetadata getMediaMetadata(){
        return this.meta;
    }

    @Override
    public ObjectId getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public Document toDocument() {
        return new Document().append("_id", getUniqueId())
                .append("name", getName())
                .append("type", type.toString())
                .append("path", getPath().toString());
    }
    
    public static class MediaFactory implements DocumentableFactory<Media> {

        @Override
        public Media newInstance(Document d) {
            return new Media(d);
        }
    }
    
}
