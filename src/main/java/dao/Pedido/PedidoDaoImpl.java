package dao.Pedido;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dao.Conex.MongoDbConnector;
import dao.Dao.Pedido.PedidoDao;
import model.PedidoBean.PedidoBean;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PedidoDaoImpl implements PedidoDao {
    private MongoCollection<Document> collection;

    private final MongoDbConnector connector = new MongoDbConnector();
    private final MongoDatabase db = connector.getDatabase();

    public PedidoDaoImpl() {
        // Inicializar la colección
        this.collection = db.getCollection("pedidos"); // Reemplaza "productos" con el nombre de tu colección
        if (this.collection == null) {
            throw new IllegalStateException("La colección no se pudo inicializar.");
        }
    }

    @Override
    public void insert(PedidoBean pedido) throws Exception {
        // Convert PedidoBean to Document
        Document doc = new Document("numeroPedido", pedido.getNumeroPedido())
                .append("idProducto", pedido.getIdProducto())
                .append("fecha", pedido.getFecha())
                .append("cantidad", pedido.getCantidad())
                .append("pedir", pedido.isPedir());

        // Insert into MongoDB
        collection.insertOne(doc);
    }

    @Override
    public void delete(int numeroPedido) throws Exception {
        // Delete by numeroPedido
        DeleteResult result = collection.deleteOne(eq("numeroPedido", numeroPedido));
        if (result.getDeletedCount() == 0) {
            throw new Exception("Pedido no encontrado");
        }
    }

    @Override
    public void update(PedidoBean pedido) throws Exception {
        // Update by numeroPedido
        Document doc = new Document("numeroPedido", pedido.getNumeroPedido())
                .append("idProducto", pedido.getIdProducto())
                .append("fecha", pedido.getFecha())
                .append("cantidad", pedido.getCantidad())
                .append("pedir", pedido.isPedir());

        UpdateResult result = collection.replaceOne(eq("numeroPedido", pedido.getNumeroPedido()), doc);
        if (result.getModifiedCount() == 0) {
            throw new Exception("Pedido no encontrado");
        }
    }

    @Override
    public PedidoBean findById(int numeroPedido) throws Exception {
        // Find by numeroPedido
        Document doc = collection.find(eq("numeroPedido", numeroPedido)).first();
        if (doc == null) {
            throw new Exception("Pedido no encontrado");
        }
        return documentToPedido(doc);
    }

    @Override
    public List<PedidoBean> findAll() throws Exception {
        // Find all pedidos
        List<PedidoBean> pedidos = new ArrayList<>();
        for (Document doc : collection.find()) {
            pedidos.add(documentToPedido(doc));
        }
        return pedidos;
    }

    // Helper method to convert Document to PedidoBean
    private PedidoBean documentToPedido(Document doc) {
        return new PedidoBean(
                doc.getInteger("numeroPedido"),
                doc.getInteger("idProducto"),
                doc.getDate("fecha"),
                doc.getInteger("cantidad"),
                doc.getBoolean("pedir")
        );
    }
}