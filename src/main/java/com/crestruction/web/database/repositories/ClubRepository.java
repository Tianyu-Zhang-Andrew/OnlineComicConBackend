package com.crestruction.web.database.repositories;

import com.crestruction.web.database.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
}