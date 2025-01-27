package service.DaoMongoDb;

import Dao.PersonDao;
import Doop.PersonBean;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public class DaoMongoDB implements PersonDao {

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    public DaoMongoDB() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("Mesii");
    }

    @Override
    public void insert(PersonBean person) throws Exception {
        Document document = new Document("Nombre:",person.getNombre()).append("Dni",person.getDni()).append("Direccion",person.getDireccion());
        collection.insertOne(document);
    }

    @Override
    public void remove(String dni) throws Exception {
    Bson filter = Filters.eq("Dni",dni);
    collection.deleteOne(filter);    }

    @Override
    public void update(PersonBean person) throws Exception {

    }

    @Override
    public PersonBean findById(String dni) throws Exception {
        Bson filter = Filters.eq("Dni",dni);
        Document document = collection.find(filter).first();
        return documentToPerson(document);
    }

    @Override
    public List<PersonBean> findAll() throws Exception {
        return List.of();
    }
    private PersonBean documentToPerson(Document doc) {
        PersonBean person = new PersonBean();
        person.setDni(doc.getString("dni"));
        person.setNombre(doc.getString("nombre"));
        person.setDireccion(doc.getList("direccion", String.class).toArray(new String[0]));
        return person;
    }
}
