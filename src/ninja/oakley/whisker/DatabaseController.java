package ninja.oakley.whisker;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.UpdateOptions;

import ninja.oakley.whisker.media.Documentable;
import ninja.oakley.whisker.media.Media;

public class DatabaseController<T extends Documentable> {

    private final MongoClient client;

    public DatabaseController(String databaseConnection){
        this.client = new MongoClient(databaseConnection);
    }
    
    public DatabaseController(){
        this.client = new MongoClient();
    }

    public List<T> getList(T blank){
        List<Document> docs = client.getDatabase("media").getCollection("media").find().into(new ArrayList<Document>());
        List<T> rt = new ArrayList<>();

        for(Document d : docs){
            rt.add(blank.fromDocument(d));
        }
        return rt;
    }
    
    public List<T> getList(){
        List<Document> docs = client.getDatabase("media").getCollection("media").find().into(new ArrayList<Document>());
        List<Media> rt = new ArrayList<>();

        for(Document d : docs){
            rt.add(new Media(d));
        }
        return rt;
    }

    public void add(T media){
        Document d = media.toDocument();
        d.remove("_id");
        
        client.getDatabase("media").getCollection("media").
        updateOne(Filters.eq("_id", media.getUniqueId()), new Document("$set", d), new UpdateOptions().upsert(true));
    }

    public void updateAll(List<T> list){
        for(T m : list){
            add(m);
        }
    }

}
