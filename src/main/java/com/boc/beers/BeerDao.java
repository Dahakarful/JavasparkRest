package com.boc.beers;

import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ragonda on 15/12/2016.
 */
public class BeerDao {

    static List<Beer> beers = new LinkedList<>();
    private final DB db;
    private final DBCollection collection;

    public BeerDao(DB db){
        this.db = db;
        this.collection = db.getCollection("beers");
    }

    public List<Beer> all(){
        List<Beer> beers = new ArrayList<>();
        DBCursor dbObjects = collection.find();
        while(dbObjects.hasNext()){
            DBObject dbObject = dbObjects.next();
            beers.add(new Beer((BasicDBObject) dbObject));
        }
        return beers;
    }

    public Beer find(String id){
        return new Beer((BasicDBObject) collection.findOne(new BasicDBObject("_id", new ObjectId(id))));
    }

    public Beer add(Beer beer){
        DBObject doc = new BasicDBObject("name", beer.getName()).append("alcohol", beer.getAlcohol());
        collection.insert(doc);
        beer.setId(doc.get("_id").toString());
        return beer;
    }

    public void delete(String id){
        collection.remove(new BasicDBObject("_id", new ObjectId(id)));
    }

//    public Beer update(String id, String name, double alcohol){
//        Beer beer = new Beer();
//        for(Beer b : beers){
//            if(b.getId().equals(id)){
//                b.setAlcohol(alcohol);
//                b.setName(name);
//                beer = b;
//                break;
//            }
//        }
//        return beer;
//    }
}
