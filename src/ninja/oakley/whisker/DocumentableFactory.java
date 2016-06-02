package ninja.oakley.whisker;

import org.bson.Document;

public interface DocumentableFactory<T> {

    public T newInstance(Document d);
    
}
