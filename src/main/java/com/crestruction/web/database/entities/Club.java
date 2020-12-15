package com.crestruction.web.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "Club")
public class Club {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type;
    private String mainPage;
    private String logo;

    @OneToMany(mappedBy="clubProductKey.club", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "clubProductKey.club", allowSetters=true)
    private Set<ClubProduct> clubProducts = new HashSet();

    @OneToMany(mappedBy="clubSpotKey.club", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "clubSpotKey.club", allowSetters=true)
    private Set<ClubSpot> clubSpots = new HashSet();

    public Club(String name, String type, String mainPage, String logo){
        this.name = name;
        this.type = type;
        this.mainPage = mainPage;
        this.logo = logo;
    }

    public Club(){

    }
}
