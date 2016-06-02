package ninja.oakley.whisker;

import org.bson.Document;

public interface Documentable {
    
    public Document toDocument();
    
    public Object getUniqueId();
    
}
