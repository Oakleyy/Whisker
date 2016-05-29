package ninja.oakley.whisker.menu;

import org.bson.Document;

public class TelevisionLibrary extends AbstractLibrary {

    
    public TelevisionLibrary(String name, long creationTime, String desc) {
        super(name, creationTime, desc);
    }

    public TelevisionLibrary(Document doc) {
        super(doc);
    }
    
}
