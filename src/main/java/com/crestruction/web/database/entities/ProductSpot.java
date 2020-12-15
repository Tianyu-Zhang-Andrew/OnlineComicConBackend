package com.crestruction.web.database.entities;

import com.crestruction.web.database.CompositeKeys.ProductSpotKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Setter
@Getter
@Entity
@Table(name = "ProductSpot")
public class ProductSpot {
    @EmbeddedId
    private ProductSpotKey productSpotKey;

    public ProductSpot(ProductSpotKey productSpotKey){
        this.productSpotKey = productSpotKey;
    }

    public ProductSpot(){

    }
}
