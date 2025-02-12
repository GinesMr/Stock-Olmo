    package dao.Cliente;

    import com.mongodb.client.*;
    import com.mongodb.client.model.Filters;
    import com.mongodb.client.model.Updates;
    import com.mongodb.client.result.UpdateResult;
    import dao.Conex.mongoDbConnector;
    import dao.Dao.Cliente.ClienteDao;
    import model.ClienteBean.ClienteBean;
    import org.bson.Document;
    import org.bson.conversions.Bson;


    import java.util.ArrayList;
    import java.util.List;



// Importaciones necesarias para MongoDB y otras clases
    public class ClienteServices implements ClienteDao {
        // Conexión a MongoDB
        mongoDbConnector connector = new mongoDbConnector();
        MongoDatabase db = connector.getDatabase();

        @Override
        public void insert(ClienteBean cliente) throws Exception {
            // Crear documento con los datos del cliente y guardarlo en MongoDB
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
            // Borrar cliente por DNI
            Bson filter = Filters.eq("dni", dni);
            db.getCollection("clientes").deleteOne(filter);
        }

        @Override
        public void update(ClienteBean cliente) throws Exception {
            // Actualizar datos del cliente buscándolo por DNI
            Bson filter = Filters.eq("dni", cliente.getDni());

            // Preparar los campos a actualizar
            Bson update = Updates.combine(
                    Updates.set("nombre", cliente.getNombre()),
                    Updates.set("direccion", cliente.getDireccion()),
                    Updates.set("telefono",cliente.getTelefono()),
                    Updates.set("email", cliente.getEmail()),
                    Updates.set("fecha",cliente.getFechaRegistro())
            );

            var collection =  db.getCollection("clientes");
            UpdateResult result = collection.updateOne(filter, update);

            // Si no encuentra el cliente, lanza error
            if (result.getMatchedCount() == 0) {
                throw new Exception("No document found with the given DNI to update.");
            }
        }

        @Override
        public ClienteBean findById(int dni) throws Exception {
            // Buscar un cliente por su DNI
            MongoCollection<Document> collection = db.getCollection("clientes");
            Bson filter = Filters.eq("dni", dni);
            FindIterable<Document> result = collection.find(filter);

            // Si encuentra el cliente, convierte el documento a ClienteBean
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
            // Obtener todos los clientes de la base de datos
            MongoCollection<Document> collection = db.getCollection("clientes");
            FindIterable<Document> documents = collection.find();
            List<ClienteBean> clientes = new ArrayList<>();

            // Convertir cada documento a ClienteBean y añadirlo a la lista
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