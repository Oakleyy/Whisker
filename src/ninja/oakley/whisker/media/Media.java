package ninja.oakley.whisker.media;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.bson.Document;
import org.bson.types.ObjectId;

import ninja.oakley.whisker.data.Documentable;
import ninja.oakley.whisker.data.DocumentableFactory;

/**
 * Storage container for media files. Stores other metadata along with it.
 * 
 * @author oakley
 *
 */
public class Media implements Documentable {

    private final String name;
    private final MediaType type;
    private final Path path;
    private final ObjectId uniqueId;
    private MediaMetadata meta;

    /**
     * Construct media from document.
     * 
     * @param doc
     */
    public Media(Document doc) {
        this(doc.getString("name"), Paths.get(doc.getString("path")), MediaType.valueOf(doc.getString("type")),
                doc.getObjectId("_id"));
    }

    /**
     * Construct a new media object.
     * 
     * @param name
     * @param path
     * @param type
     * @param uniqueId
     */
    public Media(String name, Path path, MediaType type, ObjectId uniqueId) {
        this.name = name;
        this.path = path;
        this.uniqueId = uniqueId;
        this.type = type;
    }

    /**
     * Get name of this media.
     * 
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the absolute path of this file in the filesystem.
     * 
     * @return path
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Get the metadata of this media object.
     * 
     * @return metadata
     */
    public MediaMetadata getMediaMetadata() {
        return this.meta;
    }

    public void setMediaMetadata(MediaMetadata meta) {
        this.meta = meta;
    }

    @Override
    public ObjectId getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public Document toDocument() {
        return new Document().append("_id", getUniqueId()).append("name", getName()).append("type", type.toString())
                .append("path", getPath().toString());
    }

    /**
     * Used to create new instances of Media from documents.
     * 
     * @author oakley
     *
     */
    public static class MediaFactory implements DocumentableFactory<Media> {

        @Override
        public Media newInstance(Document d) {
            return new Media(d);
        }
    }

}
