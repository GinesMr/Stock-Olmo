package dao.Producto;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dao.Conex.mongoDbConnector;
import dao.Dao.Producto.ProductoDao;
import model.ProductoBean.ProductoBean;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ProductoDaoImpl implements ProductoDao {
    private MongoCollection<Document> collection;

    // Conexión a MongoDB
    private final mongoDbConnector connector = new mongoDbConnector();
    private final MongoDatabase db = connector.getDatabase();

    public ProductoDaoImpl() {
        // Inicializar la colección
        this.collection = db.getCollection("productos"); // Reemplaza "productos" con el nombre de tu colección
        if (this.collection == null) {
            throw new IllegalStateException("La colección no se pudo inicializar.");
        }
    }
    @Override
    public void insert(ProductoBean producto) throws Exception {
        // Convert ProductoBean to Document
        Document doc = new Document("idProducto", producto.getIdProducto())
                .append("descripcion", producto.getDescripcion())
                .append("stockActual", producto.getStockActual())
                .append("stockMinimo", producto.getStockMinimo())
                .append("pvp", producto.getPvp());

        // Insert into MongoDB
        collection.insertOne(doc);
    }

    @Override
    public void delete(int idProducto) throws Exception {
        DeleteResult result = collection.deleteOne(eq("idProducto", idProducto));
        if (result.getDeletedCount() == 0) {
            throw new Exception("Producto no encontrado");
        }
    }

    @Override
    public void update(ProductoBean producto) throws Exception {
        Document doc = new Document("idProducto", producto.getIdProducto())
                .append("descripcion", producto.getDescripcion())
                .append("stockActual", producto.getStockActual())
                .append("stockMinimo", producto.getStockMinimo())
                .append("pvp", producto.getPvp());

        UpdateResult result = collection.replaceOne(eq("idProducto", producto.getIdProducto()), doc);
        if (result.getModifiedCount() == 0) {
            throw new Exception("Producto no encontrado");
        }
    }

    @Override
    public ProductoBean findById(int idProducto) throws Exception {
        Document doc = collection.find(eq("idProducto", idProducto)).first();
        if (doc == null) {
            throw new Exception("Producto no encontrado");
        }
        return documentToProducto(doc);
    }

    @Override
    public List<ProductoBean> findAll() throws Exception {
        List<ProductoBean> productos = new ArrayList<>();
        for (Document doc : collection.find()) {
            productos.add(documentToProducto(doc));
        }
        return productos;
    }


    private ProductoBean documentToProducto(Document doc) {
        return new ProductoBean(
                doc.getInteger("idProducto"),
                doc.getString("descripcion"),
                doc.getInteger("stockActual"),
                doc.getInteger("stockMinimo"),
                doc.getDouble("pvp").floatValue()
        );
    }
}