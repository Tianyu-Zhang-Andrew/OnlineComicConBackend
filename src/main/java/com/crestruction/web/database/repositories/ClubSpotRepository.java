package com.crestruction.web.database.repositories;

import com.crestruction.web.database.CompositeKeys.ClubSpotKey;
import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ClubSpot;
import com.crestruction.web.database.entities.ExhibitionSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubSpotRepository  extends JpaRepository<ClubSpot, ClubSpotKey> {
    List<ClubSpot> findByClubSpotKeyClub(Club club);
    List<ClubSpot> findByClubSpotKeyExhibitionSpot(ExhibitionSpot exhibitionSpot);
}
