package com.crestruction.web.database.entities;

import com.crestruction.web.database.CompositeKeys.ClubProductKey;
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
@Table(name = "ClubProduct")
public class ClubProduct {
    @EmbeddedId
    private ClubProductKey clubProductKey;

    public ClubProduct(ClubProductKey clubProductKey){
        this.clubProductKey = clubProductKey;
    }

    public ClubProduct(){

    }
}
