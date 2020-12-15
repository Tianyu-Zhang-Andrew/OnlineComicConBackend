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
@Table(name = "ExhibitionSpot")
public class ExhibitionSpot {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy="productSpotKey.exhibitionSpot", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "productSpotKey.exhibitionSpot", allowSetters=true)
    private Set<ProductSpot> productSpots = new HashSet();

    @OneToMany(mappedBy="clubSpotKey.exhibitionSpot", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "clubSpotKey.exhibitionSpot", allowSetters=true)
    private Set<ClubSpot> clubSpots = new HashSet();

    private String size;
    private String additionalInfo;

    public  ExhibitionSpot(String size, String additionalInfo){
        this.size = size;
        this.additionalInfo = additionalInfo;
    }

    public ExhibitionSpot(){

    }
}
