package com.boc.beers;

import com.mongodb.*;

import static spark.Spark.*;

/**
 * Created by Ragonda on 15/12/2016.
 */
public class Application {

    private static DB mongo() throws Exception{
        String host = System.getenv("MONGODB_ADDON_HOST");
        if(host == null){
            MongoClient mongoClient = new MongoClient("localhost");
            return mongoClient.getDB("todoapp");
        }
        int port = 27017;
        String dbname = System.getenv("MONGODB_ADDON_DB");
        String username = System.getenv("MONGODB_ADDON_USER");
        String password = System.getenv("MONGODB_ADDON_PASSWORD");
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), mongoClientOptions);
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        DB db = mongoClient.getDB(dbname);
        if(db.authenticate(username, password.toCharArray())){
            return db;
        }else{
            throw new RuntimeException("Not able to authenticate with MongoDB");
        }
    }

    public static void main(String args[]) throws Exception {
        port(8080);
        enableCORS("*", "*", "*");

        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        get("/", (reg, res) -> "Hello Beers");

        BeerDao dao = new BeerDao(mongo());
        get("/beers", (req, res) -> dao.all(), new JsonTransformer());

        post("/beers", (req, res) -> {
            Beer b = dao.add(new Beer(req.queryParams("name"),Double.parseDouble(req.queryParams("alcohol"))));
            res.status(201);
            return b;
        }, new JsonTransformer());

        get("/beers/:id", (req, res) -> dao.find(req.params("id")), new JsonTransformer());

        delete("/beers/:id", (req, res) -> {
            dao.delete(req.params("id"));
            return null;
        }, new JsonTransformer());


//        put("/beers/:id", (req, res) -> dao.update(req.params("id"), req.queryParams("name"), Double.parseDouble(req.queryParams("alcohol"))));
    }

    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
}
