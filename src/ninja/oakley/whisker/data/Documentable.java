package ninja.oakley.whisker.data;

import org.bson.Document;

public interface Documentable {
    
    /**
     * Get document representation of the object.
     * 
     * @return Document
     */
    public Document toDocument();
    
    /**
     * Get the identifier for the object.
     * 
     * @return identifier
     */
    public Object getUniqueId();
    
}
