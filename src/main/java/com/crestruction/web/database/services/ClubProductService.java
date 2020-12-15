package com.crestruction.web.database.services;

import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ClubProduct;
import com.crestruction.web.database.entities.Product;

import java.util.List;

public interface ClubProductService {
    ClubProduct saveClubProduct(ClubProduct clubProduct);
    ClubProduct editClubProduct(ClubProduct oldClubProduct, ClubProduct newClubProduct);
    void deleteClubProduct(ClubProduct clubProduct);
    void deleteAllClubProduct();
    List<ClubProduct> searchClubProduct(Club club, Product product);
    List<ClubProduct> getAllClubProduct();
}
