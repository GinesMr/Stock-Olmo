package dao.Conex;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDbConnector {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String DATABASE = "StockOlmo";

    public MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://" + HOST + ":" + PORT);
        return mongoClient.getDatabase(DATABASE);
    }
}