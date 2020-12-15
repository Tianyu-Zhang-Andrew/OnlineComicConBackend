package com.crestruction.web.database.services;

import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.entities.Product;
import com.crestruction.web.database.entities.ProductSpot;

import java.util.List;

public interface ProductSpotService {
    ProductSpot saveProductSpot(ProductSpot productSpot);
    ProductSpot editProductSpot(ProductSpot oldProductSpot, ProductSpot newProductSpot);
    void deleteProductSpot(ProductSpot productSpot);
    void deleteAllProductSpot();
    List<ProductSpot> searchProductSpot(ExhibitionSpot exhibitionSpot, Product product);
    List<ProductSpot> getAllProductSpot();
}
