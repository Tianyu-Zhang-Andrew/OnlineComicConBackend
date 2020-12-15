package com.crestruction.web.database.repositories;

import com.crestruction.web.database.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
