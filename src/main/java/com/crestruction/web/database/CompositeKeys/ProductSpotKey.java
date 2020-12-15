package com.crestruction.web.database.CompositeKeys;

import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.entities.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class ProductSpotKey implements Serializable {
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH })
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "productSpots", allowSetters=true)
    private Product product;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH })
    @JoinColumn(name = "exhibitionSpot_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "productSpots", allowSetters=true)
    private ExhibitionSpot exhibitionSpot;

    public ProductSpotKey(Product product, ExhibitionSpot exhibitionSpot){
        this.product = product;
        this.exhibitionSpot = exhibitionSpot;
    }

    public ProductSpotKey(){}
}
