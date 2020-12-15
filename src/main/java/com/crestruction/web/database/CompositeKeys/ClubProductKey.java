package com.crestruction.web.database.CompositeKeys;

import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class ClubProductKey implements Serializable {
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH })
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "clubProducts", allowSetters=true)
    private Club club;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH })
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "clubProducts", allowSetters=true)
    private Product product;

    public ClubProductKey(Club club, Product product){
        this.club = club;
        this.product = product;
    }

    public ClubProductKey(){
    }
}
