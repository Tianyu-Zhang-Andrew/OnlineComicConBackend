package com.crestruction.web.database.ServiceImps;

import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ClubAlreadyExistException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ClubNotFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.NoResultFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.TableIsEmptyException;
import com.crestruction.web.database.repositories.ClubRepository;
import com.crestruction.web.database.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClubServiceImp implements ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Override
    public Club saveClub(Club club) {
        //Check if the club already exists
        Example<Club> clubExample =  Example.of(club);
        Optional<Club> existClub = this.clubRepository.findOne(clubExample);

        //Is the club already exists, throw exception
        if(existClub.isPresent()) {
            throw new ClubAlreadyExistException();

        //If club doesn't exist, create club
        }else {
            Club savedClub = this.clubRepository.save(club);
            return savedClub;
        }
    }

    @Override
    public Club editClub(Club club) {
        //Check if the edited club exists
        Optional<Club> existClub = this.clubRepository.findById(club.getId());

        //If the club doesn't exists, throw exception
        if(!existClub.isPresent()) {
            throw new ClubNotFoundException(club.getId());

        //If the club exists, edit the club
        }else {
            Club savedClub = this.clubRepository.save(club);
            return savedClub;
        }
    }

    @Override
    public void deleteClub(Club club) {
        //Check if the club to be deleted exists
        Optional<Club> existClub = this.clubRepository.findById(club.getId());

        //If the club doesn't exists, throw exception
        if(!existClub.isPresent()) {
            throw new ClubNotFoundException(club.getId());

        //If the club exists, delete the club
        }else {
            this.clubRepository.delete(club);
        }
    }

    @Override
    public void deleteAllClub() {
        this.clubRepository.deleteAll();
    }

    @Override
    public List<Club> searchClub(Club club) {
        //Dynamically search for the result
        Example<Club> clubExample =  Example.of(club);
        List<Club> clubList = this.clubRepository.findAll(clubExample);

        //If there is no result found, throw exception
        if(clubList.size() == 0){
            throw new NoResultFoundException();

        //If result found, return all results
        }else {
            return clubList;
        }
    }

    @Override
    public List<Club> getAllClub() {
        List<Club> clubList = this.clubRepository.findAll();

        //If there is no records in the table, throw exception
        if(clubList.size() == 0){
            throw new TableIsEmptyException();

        //If there is records in the table, return all records
        }else{
            return clubList;
        }
    }
}
