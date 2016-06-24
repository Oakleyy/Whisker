package ninja.oakley.whisker.data;

import org.bson.Document;

public interface DocumentableFactory<T> {

    public T newInstance(Document d);
    
}
