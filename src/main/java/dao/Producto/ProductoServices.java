package dao.Producto;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dao.Dao.Producto.ProductoDao;
import model.ProductoBean.ProductoBean;
import org.bson.Document;
import org.bson.conversions.Bson;

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
    public void delete(int id) throws Exception {
        Bson filter = Filters.eq("id", id);
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
        MongoCollection<Document> collection = db.getCollection("productos");

        Bson filter = Filters.eq("dni", id);
        FindIterable<Document> result = collection.find(filter);

        Document document = result.first();
        if (document != null) {
            ProductoBean producto = new ProductoBean();
            producto.setId(document.getInteger("id"));
            producto.setNombre(document.getString("nombre"));
            producto.setDescripcion(document.getString("descripcion"));
            producto.setPrecio(Double.parseDouble (document.getString ("precio")));
            producto.setCantidad(document.getInteger("cantidad"));
            producto.setFechaRegistro(document.getDate("fechaRegistro"));
            return producto;
        } else {
            return null;
        }
    }

    @Override
    public List<ProductoBean> findAll() throws Exception {
        return List.of();
    }
}
