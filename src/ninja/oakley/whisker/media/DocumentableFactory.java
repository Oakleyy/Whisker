package ninja.oakley.whisker.media;

import org.bson.Document;

public interface DocumentableFactory<T> {

    public T newInstance(Document d);
    
}
