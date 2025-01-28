package service.DaoMongoDb.Producto;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dao.ProductoDao;
import model.ClienteBean.ClienteBean;
import model.ProductoBean.ProductoBean;
import org.bson.Document;
import org.bson.conversions.Bson;
import service.DaoMongoDb.DaoMongoDB;

import java.util.List;

public class ProductoServices implements ProductoDao {
    DaoMongoDB daoMongoDB;


    @Override
    public void insert(ProductoBean producto) throws Exception {

        Document document = new Document("id", producto.getId())
                .append("nombre", producto.getNombre())
                .append("descripcion", producto.getDescripcion())
                .append("precio", producto.getPrecio())
                .append("cantidad", producto.getCantidad())
                .append("Fecha:",producto.getFechaRegistro());

        daoMongoDB.getDatabase().getCollection("productos").insertOne(document);
    }

    @Override
    public void delete(int id) throws Exception {
        Bson filter = Filters.eq("id", id);
        daoMongoDB.getDatabase().getCollection("productos").deleteOne(filter);
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

        var collection =  daoMongoDB.getDatabase().getCollection("productos");

        UpdateResult result = collection.updateOne(filter, update);

        if (result.getMatchedCount() == 0) {
            throw new Exception("No document found with the given id to update.");
        }
    }

    @Override
    public ProductoBean findById(int id) throws Exception {
        MongoCollection<Document> collection = daoMongoDB.getDatabase().getCollection("productos");

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
