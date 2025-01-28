package service.DaoMongoDb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dao.ClienteDao;
import model.ClienteBean.ClienteBean;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.List;

public class DaoMongoDB {
    private MongoClient mongoClient;
    private MongoDatabase db;

    public DaoMongoDB() throws Exception {
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        this.db = mongoClient.getDatabase("EasyManage");
    }

    public MongoDatabase getDatabase() {
        return db;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}