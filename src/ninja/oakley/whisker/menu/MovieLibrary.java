package ninja.oakley.whisker.menu;

import org.bson.Document;

public final class MovieLibrary extends AbstractLibrary {

    
    public MovieLibrary(String name, long creationTime, String desc) {
        super(name, creationTime, desc);
    }

    public MovieLibrary(Document doc) {
        super(doc);
    }

}
