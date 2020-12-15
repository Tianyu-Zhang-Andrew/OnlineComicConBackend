package com.crestruction.web.database.entities;

import com.crestruction.web.database.CompositeKeys.ClubSpotKey;
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
@Table(name = "ClubSpot")
public class ClubSpot {
    @EmbeddedId
    private ClubSpotKey clubSpotKey;

    public ClubSpot(ClubSpotKey clubSpotKey){
        this.clubSpotKey = clubSpotKey;
    }

    public ClubSpot(){

    }
}
