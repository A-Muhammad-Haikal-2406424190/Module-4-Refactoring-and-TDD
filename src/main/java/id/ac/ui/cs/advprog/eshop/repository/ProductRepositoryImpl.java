package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private List<Product> productData = new ArrayList<>();

    @Override
    public Product create(Product product) {
        if (product.getProductId() == null){
            product.setProductId(java.util.UUID.randomUUID().toString());
        }
        productData.add(product);
        return product;
    }

    @Override
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    @Override
    public Product findById(String id){
        return productData.stream().filter(p -> id.equals(p.getProductId())).findFirst().orElse(null);
    }

    @Override
    public Product update(String id, Product product) {
        if (product == null || id == null) return null;
        for (int i = 0; i < productData.size(); i++) {
            if (id.equals(productData.get(i).getProductId())) {
                productData.set(i, product);
                return product;
            }
        }
        return null;
    }

    @Override
    public void delete(String id) {
        productData.removeIf(p -> p.getProductId().equals(id));
    }
}