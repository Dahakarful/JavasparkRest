package com.boc.beers;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

import java.rmi.server.UID;
import java.util.UUID;

/**
 * Created by Ragonda on 15/12/2016.
 */
public class Beer {

    String name;
    String id;
    double alcohol;

    public Beer(BasicDBObject dbObject){
        this.id = ((ObjectId) dbObject.get("_id")).toString();
        this.name = dbObject.getString("name");
        this.alcohol = dbObject.getDouble("alcohol");
    }

    public Beer(String name, double alcohol){
        this.name = name;
        this.alcohol = alcohol;
        this.id = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(double alcohol) {
        this.alcohol = alcohol;
    }
}
