package com.crestruction.web.database.services;

import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ClubSpot;
import com.crestruction.web.database.entities.ExhibitionSpot;

import java.util.List;

public interface ClubSpotService {
    ClubSpot saveClubSpot(ClubSpot clubSpot);
    ClubSpot editClubSpot(ClubSpot oldClubSpot, ClubSpot newClubSpot);
    void deleteClubSpot(ClubSpot clubSpot);
    void deleteAllClubSpot();
    List<ClubSpot> searchClubSpot(Club club, ExhibitionSpot exhibitionSpot);
    List<ClubSpot> getAllClubSpot();
}
