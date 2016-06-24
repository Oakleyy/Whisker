package ninja.oakley.whisker.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

/**
 * Used to tput entriers inside of a database for easy access.
 * 
 * @author oakley
 *
 * @param <T> documentable type
 * @param <T1> document factory for type t
 */
public class DatabaseController<T extends Documentable, T1 extends DocumentableFactory<T>> {

    private final T1 factory;
    private final MongoClient client;
    
    private final String database;
    private final String collection;

    public DatabaseController(T1 factory, String databaseConnection, String database, String collection){
        this.factory = factory;
        this.client = new MongoClient(databaseConnection);
        this.database = database;
        this.collection = collection;
    }
    
    public DatabaseController(T1 factory, String database, String collection){
        this.factory = factory;
        this.client = new MongoClient();
        this.database = database;
        this.collection = collection;
    }

    /**
     * Get a list of all of the objects in the database.
     * 
     * @return list of type T
     */
    public List<T> getList(){
        List<Document> docs = client.getDatabase(database).getCollection(collection).find().into(new ArrayList<Document>());
        List<T> rt = new ArrayList<>();

        for(Document d : docs){
            rt.add(factory.newInstance(d));
        }
        return rt;
    }

    /**
     * Add type T into the database.
     * 
     * @param t media type
     */
    public void add(T t){
        Document d = t.toDocument();
        d.remove("_id");
        
        client.getDatabase(database).getCollection(collection).
        updateOne(Filters.eq("_id", t.getUniqueId()), new Document("$set", d), new UpdateOptions().upsert(true));
    }

    /**
     * Update the database with the entries of the list.
     * 
     * @param list
     */
    public void updateAll(List<T> list){
        for(T m : list){
            add(m);
        }
    }

}
