package com.crestruction.web.database.services;

import com.crestruction.web.database.entities.Club;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClubService {
    Club saveClub(Club club);
    Club editClub(Club club);
    void deleteClub(Club club);
    void deleteAllClub();
    List<Club> searchClub(Club club);
    List<Club> getAllClub();
}
