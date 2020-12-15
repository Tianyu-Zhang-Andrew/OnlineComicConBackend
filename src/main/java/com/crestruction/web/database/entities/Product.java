package com.crestruction.web.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy="clubProductKey.product", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "clubProductKey.product", allowSetters=true)
    private Set<ClubProduct> clubProducts = new HashSet();

    @OneToMany(mappedBy="productSpotKey.product", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "productSpotKey.product", allowSetters=true)
    private Set<ProductSpot> productSpots = new HashSet();

    private Integer price;
    private String picture;
    private String name;
    private String type;

    public Product(int price, String picture, String name, String type){
        this.price = price;
        this.picture = picture;
        this.name = name;
        this.type = type;
    }

    public Product(){

    }
}
