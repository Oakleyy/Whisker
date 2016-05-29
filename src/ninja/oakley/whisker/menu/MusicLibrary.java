package ninja.oakley.whisker.menu;

import org.bson.Document;

public class MusicLibrary extends AbstractLibrary {

    public MusicLibrary(String name, long creationTime, String desc) {
        super(name, creationTime, desc);
    }

    public MusicLibrary(Document doc) {
        super(doc);
    }
    
    
}
