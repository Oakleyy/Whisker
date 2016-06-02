package ninja.oakley.whisker;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.UpdateOptions;

import ninja.oakley.whisker.media.Media;

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

    public List<T> getList(){
        List<Document> docs = client.getDatabase(database).getCollection(collection).find().into(new ArrayList<Document>());
        List<T> rt = new ArrayList<>();

        for(Document d : docs){
            rt.add(factory.newInstance(d));
        }
        return rt;
    }

    public void add(T media){
        Document d = media.toDocument();
        d.remove("_id");
        
        client.getDatabase(database).getCollection(collection).
        updateOne(Filters.eq("_id", media.getUniqueId()), new Document("$set", d), new UpdateOptions().upsert(true));
    }

    public void updateAll(List<T> list){
        for(T m : list){
            add(m);
        }
    }

}
