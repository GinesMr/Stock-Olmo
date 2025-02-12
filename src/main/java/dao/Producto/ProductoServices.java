package dao.Producto;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dao.Conex.mongoDbConnector;
import dao.Dao.Producto.ProductoDao;
import model.ClienteBean.ClienteBean;
import model.ProductoBean.ProductoBean;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

// Importaciones necesarias para MongoDB y otras clases

public class ProductoServices implements ProductoDao {
    // Conexión a MongoDB
    mongoDbConnector connector = new mongoDbConnector();
    MongoDatabase db = connector.getDatabase();

    @Override
    public void insert(ProductoBean producto) throws Exception {
        // Crear documento con los datos del producto
        Document document = new Document("id", producto.getId())
                .append("nombre", producto.getNombre())
                .append("descripcion", producto.getDescripcion())
                .append("precio", producto.getPrecio())
                .append("cantidad", producto.getCantidad())
                .append("Fecha:",producto.getFechaRegistro());

        // Insertar el producto en la colección
        db.getCollection("productos").insertOne(document);
    }

    @Override
    public void delete(String nombre) throws Exception {
        // Borrar producto por nombre
        Bson filter = Filters.eq("Nombre", nombre);
        db.getCollection("productos").deleteOne(filter);
    }

    @Override
    public void update(ProductoBean producto) throws Exception {
        // Actualizar producto por ID
        Bson filter = Filters.eq("id", producto.getId());

        // Campos a actualizar
        Bson update = Updates.combine(
                Updates.set("nombre", producto.getNombre()),
                Updates.set("descripcion", producto.getDescripcion()),
                Updates.set("precio", producto.getPrecio()),
                Updates.set("cantidad", producto.getCantidad()),
                Updates.set("Fecha:",producto.getFechaRegistro())
        );

        var collection =  db.getCollection("productos");
        UpdateResult result = collection.updateOne(filter, update);

        // Error si no encuentra el producto
        if (result.getMatchedCount() == 0) {
            throw new Exception("No document found with the given id to update.");
        }
    }

    @Override
    public ProductoBean findById(int id) throws Exception {
        try {
            // Buscar producto por ID
            MongoCollection<Document> collection = db.getCollection("productos");
            Bson filter = Filters.eq("id", id);
            Document document = collection.find(filter).first();

            // Si encuentra el producto, convertir a ProductoBean
            if (document != null) {
                ProductoBean producto = new ProductoBean();
                producto.setId(document.getInteger("id"));
                producto.setNombre(document.getString("nombre"));
                producto.setDescripcion(document.getString("descripcion"));
                producto.setPrecio(document.getDouble("precio"));
                producto.setCantidad(document.getInteger("cantidad"));
                producto.setFechaRegistro(document.getDate("fecha"));
                return producto;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ProductoBean> findAll() throws Exception {
        // Obtener todos los productos
        MongoCollection<Document> collection = db.getCollection("productos");
        FindIterable<Document> documents = collection.find();
        List<ProductoBean> productos = new ArrayList<>();

        // Convertir cada documento a ProductoBean
        for (Document doc : documents) {
            ProductoBean producto = new ProductoBean();
            producto.setId(doc.getInteger("id"));
            producto.setNombre(doc.getString("nombre"));
            producto.setDescripcion(doc.getString("descripcion"));
            producto.setPrecio(doc.getDouble("precio"));
            producto.setCantidad(doc.getInteger("cantidad"));
            producto.setFechaRegistro(doc.getDate("fechaRegistro"));
            productos.add(producto);
        }

        return productos;
    }
}