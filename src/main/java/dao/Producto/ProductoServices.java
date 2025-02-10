package dao.Producto;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dao.Dao.Producto.ProductoDao;
import model.ClienteBean.ClienteBean;
import model.ProductoBean.ProductoBean;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class ProductoServices implements ProductoDao {

    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    MongoDatabase db = mongoClient.getDatabase("EasyManage");
    @Override
    public void insert(ProductoBean producto) throws Exception {

        Document document = new Document("id", producto.getId())
                .append("nombre", producto.getNombre())
                .append("descripcion", producto.getDescripcion())
                .append("precio", producto.getPrecio())
                .append("cantidad", producto.getCantidad())
                .append("Fecha:",producto.getFechaRegistro());

        db.getCollection("productos").insertOne(document);
    }

    @Override
    public void delete(String nombre) throws Exception {

        Bson filter = Filters.eq("Nombre", nombre);
        db.getCollection("productos").deleteOne(filter);
    }

    @Override
    public void update(ProductoBean producto) throws Exception {
        Bson filter = Filters.eq("id", producto.getId());

        Bson update = Updates.combine(
                Updates.set("nombre", producto.getNombre()),
                Updates.set("descripcion", producto.getDescripcion()),
                Updates.set("precio", producto.getPrecio()),
                Updates.set("cantidad", producto.getCantidad()),
                Updates.set("Fecha:",producto.getFechaRegistro())
        );

        var collection =  db.getCollection("productos");

        UpdateResult result = collection.updateOne(filter, update);

        if (result.getMatchedCount() == 0) {
            throw new Exception("No document found with the given id to update.");
        }
    }

    @Override
    public ProductoBean findById(int id) throws Exception {
        try {
            MongoCollection<Document> collection = db.getCollection("productos");

            Bson filter = Filters.eq("id", id);
            Document document = collection.find(filter).first();

            if (document != null) {
                ProductoBean producto = new ProductoBean();
                producto.setId(document.getInteger("id")); // Obtener el campo "id"
                producto.setNombre(document.getString("nombre"));
                producto.setDescripcion(document.getString("descripcion"));
                producto.setPrecio(document.getDouble("precio")); // Obtener el campo "precio" como double
                producto.setCantidad(document.getInteger("cantidad")); // Obtener el campo "cantidad"
                producto.setFechaRegistro(document.getDate("fecha")); // Obtener el campo "fecha" como Date
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
        MongoCollection<Document> collection = db.getCollection("productos");
        FindIterable<Document> documents = collection.find();
        List<ProductoBean> productos = new ArrayList<>();

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
