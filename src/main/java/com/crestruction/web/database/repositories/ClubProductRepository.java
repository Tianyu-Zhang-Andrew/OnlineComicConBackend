package com.crestruction.web.database.repositories;

import com.crestruction.web.database.CompositeKeys.ClubProductKey;
import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ClubProduct;
import com.crestruction.web.database.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubProductRepository extends JpaRepository<ClubProduct, ClubProductKey> {
    List<ClubProduct> findByClubProductKeyClub(Club club);
    List<ClubProduct> findByClubProductKeyProduct(Product product);
}
