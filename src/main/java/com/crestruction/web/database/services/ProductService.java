package com.crestruction.web.database.services;

import com.crestruction.web.database.entities.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);
    Product editProduct(Product product);
    void deleteProduct(Product product);
    void deleteAllProduct();
    List<Product> searchProduct(Product product);
    List<Product> getAllProduct();
}
