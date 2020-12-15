package com.crestruction.web.database.repositories;

import com.crestruction.web.database.CompositeKeys.ProductSpotKey;
import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.entities.Product;
import com.crestruction.web.database.entities.ProductSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSpotRepository extends JpaRepository<ProductSpot, ProductSpotKey> {
    List<ProductSpot> findByProductSpotKeyExhibitionSpot(ExhibitionSpot exhibitionSpot);
    List<ProductSpot> findByProductSpotKeyProduct(Product product);
}
