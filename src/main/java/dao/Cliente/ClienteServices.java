package dao.Cliente;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dao.Dao.Cliente.ClienteDao;
import model.ClienteBean.ClienteBean;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.util.ArrayList;
import java.util.List;

public class ClienteServices implements ClienteDao {

    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    MongoDatabase db = mongoClient.getDatabase("EasyManage");

    @Override
    public void insert(ClienteBean cliente) throws Exception {
        Document document = new Document("dni", cliente.getDni())
                .append("nombre", cliente.getNombre())
                .append("direccion", cliente.getDireccion())
                .append("telefono", cliente.getTelefono())
                .append("email", cliente.getEmail())
                .append("fecha",cliente.getFechaRegistro());

        db.getCollection("clientes").insertOne(document);
    }

    @Override
    public void delete(int dni) throws Exception {
        Bson filter = Filters.eq("dni", dni);
        db.getCollection("clientes").deleteOne(filter);
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

        var collection =  db.getCollection("clientes");

        UpdateResult result = collection.updateOne(filter, update);

        if (result.getMatchedCount() == 0) {
            throw new Exception("No document found with the given DNI to update.");
        }

    }

    @Override
    public ClienteBean findById(int dni) throws Exception {
        MongoCollection<Document> collection = db.getCollection("clientes");

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
        MongoCollection<Document> collection = db.getCollection("clientes");
        FindIterable<Document> documents = collection.find();
        List<ClienteBean> clientes = new ArrayList<>();

        for (Document doc : documents) {
            ClienteBean cliente = new ClienteBean();
            cliente.setDni(doc.getInteger("dni"));
            cliente.setNombre(doc.getString("nombre"));
            cliente.setDireccion(doc.getString("direccion"));
            cliente.setTelefono(doc.getString("telefono"));
            cliente.setEmail(doc.getString("email"));
            cliente.setFechaRegistro(doc.getDate("fecha"));
            clientes.add(cliente);
        }

        return clientes;
    }

}
