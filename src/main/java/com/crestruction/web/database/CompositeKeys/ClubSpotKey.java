package com.crestruction.web.database.CompositeKeys;

import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ExhibitionSpot;
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
public class ClubSpotKey implements Serializable {
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH })
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "clubSpots", allowSetters=true)
    private Club club;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH })
    @JoinColumn(name = "exhibitionSpot_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "clubSpots", allowSetters=true)
    private ExhibitionSpot exhibitionSpot;

    public ClubSpotKey(Club club, ExhibitionSpot exhibitionSpot){
        this.club = club;
        this.exhibitionSpot = exhibitionSpot;
    }

    public ClubSpotKey(){

    }
}
