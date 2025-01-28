package service.DaoMongoDb.Cliente;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dao.ClienteDao;
import model.ClienteBean.ClienteBean;
import org.bson.Document;
import org.bson.conversions.Bson;
import service.DaoMongoDb.DaoMongoDB;

import java.util.List;

public class ClienteServices implements ClienteDao {

        DaoMongoDB daoMongoDB;

    @Override
    public void insert(ClienteBean cliente) throws Exception {
        Document document = new Document("dni", cliente.getDni())
                .append("nombre", cliente.getNombre())
                .append("telefono", cliente.getTelefono())
                .append("email", cliente.getEmail())
                .append("fecha",cliente.getFechaRegistro());

        daoMongoDB.getDatabase().getCollection("clientes").insertOne(document);
    }

    @Override
    public void delete(int dni) throws Exception {
        Bson filter = Filters.eq("dni", dni);
        daoMongoDB.getDatabase().getCollection("clientes").deleteOne(filter);
    }

    @Override
    public void update(ClienteBean cliente) throws Exception {
        Bson filter = Filters.eq("dni", cliente.getDni());

        Bson update = Updates.combine(
                Updates.set("nombre", cliente.getNombre()),
                Updates.set("direccion", cliente.getDireccion()),
                Updates.set("telefono",cliente.getTelefono()),
                Updates.set("email", cliente.getEmail()),
                Updates.set("fecha",cliente.getFechaRegistro())
        );

        var collection =  daoMongoDB.getDatabase().getCollection("clientes");

        UpdateResult result = collection.updateOne(filter, update);

        if (result.getMatchedCount() == 0) {
            throw new Exception("No document found with the given DNI to update.");
        }

    }

    @Override
    public ClienteBean findById(int dni) throws Exception {
        MongoCollection<Document> collection = daoMongoDB.getDatabase().getCollection("clientes");

        Bson filter = Filters.eq("dni", dni);
        FindIterable<Document> result = collection.find(filter);

        Document document = result.first();
        if (document != null) {
            ClienteBean cliente = new ClienteBean();
            cliente.setDni(document.getInteger("dni"));
            cliente.setNombre(document.getString("nombre"));
            cliente.setDireccion(document.getString("direccion"));
            cliente.setTelefono(document.getString("telefono"));
            cliente.setEmail(document.getString("email"));
            cliente.setFechaRegistro(document.getDate("fechaRegistro"));
            return cliente;
        } else {
            return null;
        }
    }

    @Override
    public List<ClienteBean> findAll() throws Exception {
        return List.of();
    }
}
