package com.crestruction.web.database.ServiceImps;

import com.crestruction.web.database.CompositeKeys.ClubSpotKey;
import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ClubSpot;
import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ClubSpotAlreadyExistException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ClubHasNoIdException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ExhibitionSpotHasNoIdException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.*;
import com.crestruction.web.database.repositories.ClubRepository;
import com.crestruction.web.database.repositories.ClubSpotRepository;
import com.crestruction.web.database.repositories.ExhibitionSpotRepository;
import com.crestruction.web.database.services.ClubSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubSpotServiceImp implements ClubSpotService {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ExhibitionSpotRepository exhibitionSpotRepository;
    @Autowired
    private ClubSpotRepository clubSpotRepository;


    @Override
    public ClubSpot saveClubSpot(ClubSpot clubSpot) {
        //Club must has id
        if(clubSpot.getClubSpotKey().getClub().getId() == null){
            throw new ClubHasNoIdException();
        }

        //ExhibitionSpot must has id
        if(clubSpot.getClubSpotKey().getExhibitionSpot().getId() == null){
            throw new ExhibitionSpotHasNoIdException();
        }

        //Check if the exhibitionSpot and club both exist
        Optional<ExhibitionSpot> existExhibitionSpot = this.exhibitionSpotRepository
                .findById(clubSpot.getClubSpotKey().getExhibitionSpot().getId());
        Optional<Club> existClub = this.clubRepository.findById(clubSpot.getClubSpotKey().getClub().getId());

        //If the exhibitionSpot and club both exist, save the clubSpot
        if(existExhibitionSpot.isPresent() && existClub.isPresent()){
            Optional<ClubSpot> existClubSpot = this.clubSpotRepository.findById(clubSpot.getClubSpotKey());

            //If the clubSpot already exists, throw exception
            if(existClubSpot.isPresent()){
                throw new ClubSpotAlreadyExistException();
            }else{
                existExhibitionSpot.get().getClubSpots().add(clubSpot);
                existClub.get().getClubSpots().add(clubSpot);

                this.exhibitionSpotRepository.save(existExhibitionSpot.get());
                this.clubRepository.save(existClub.get());

                Optional<ClubSpot> savedProductSpot = this.clubSpotRepository.findById(clubSpot.getClubSpotKey());
                return savedProductSpot.get();
            }

            //If the ExhibitionSpot doesn't exist, throw exception
        }else if(!existExhibitionSpot.isPresent()){
            throw new ExhibitionSpotNotFoundException(clubSpot.getClubSpotKey().getExhibitionSpot().getId());

            //If the club doesn't exist, throw exception
        }else{
            throw new ClubNotFoundException(clubSpot.getClubSpotKey().getClub().getId());
        }
    }

    @Override
    public ClubSpot editClubSpot(ClubSpot oldClubSpot, ClubSpot newClubSpot) {
        //Check if the clubSpot needs to be changed exists
        Optional<ClubSpot> existOldClubSpot = this.clubSpotRepository.findById(oldClubSpot.getClubSpotKey());

        //If the clubSpot needs to be changed exists
        if(existOldClubSpot.isPresent()){
            //Check if the newClubSpot's exhibitionSpot and club both exist
            Optional<ExhibitionSpot> exhibitionSpot = this.exhibitionSpotRepository
                    .findById(newClubSpot.getClubSpotKey().getExhibitionSpot().getId());
            Optional<Club> existClub = this.clubRepository
                    .findById(newClubSpot.getClubSpotKey().getClub().getId());

            //If the exhibitionSpot and club both exist, edit the clubSpot
            if(exhibitionSpot.isPresent() && existClub.isPresent()){
                //Remove the old clubSpot
                oldClubSpot.getClubSpotKey().getExhibitionSpot().getClubSpots().remove(oldClubSpot);
                oldClubSpot.getClubSpotKey().getClub().getClubSpots().remove(oldClubSpot);
                this.exhibitionSpotRepository.save(oldClubSpot.getClubSpotKey().getExhibitionSpot());
                this.clubRepository.save(oldClubSpot.getClubSpotKey().getClub());
                this.clubSpotRepository.deleteById(oldClubSpot.getClubSpotKey());

                //Add the new clubSpot
                exhibitionSpot.get().getClubSpots().add(newClubSpot);
                existClub.get().getClubSpots().add(newClubSpot);
                this.exhibitionSpotRepository.save(exhibitionSpot.get());
                this.clubRepository.save(existClub.get());

                Optional<ClubSpot> savedClubSpot = this.clubSpotRepository.findById(newClubSpot.getClubSpotKey());
                return savedClubSpot.get();

                //If the exhibitionSpot doesn't exist, throw exception
            }else if(!exhibitionSpot.isPresent()){
                throw new ExhibitionSpotNotFoundException(newClubSpot.getClubSpotKey().getExhibitionSpot().getId());

                //If the club doesn't exist, throw exception
            }else{
                throw new ClubNotFoundException(newClubSpot.getClubSpotKey().getClub().getId());
            }

        }else{
            throw new ClubSpotNotFoundException();
        }
    }

    @Override
    public void deleteClubSpot(ClubSpot clubSpot) {
        //Check if the clubSpot to be deleted exists
        Optional<ClubSpot> existClubSpot = this.clubSpotRepository.findById(clubSpot.getClubSpotKey());

        //If the clubSpot to be deleted exists, delete the clubSpot
        if(existClubSpot.isPresent()){
            //Delete the clubSpot from the exhibitionSpot
            clubSpot.getClubSpotKey().getExhibitionSpot().getClubSpots().remove(clubSpot);
            this.exhibitionSpotRepository.save(clubSpot.getClubSpotKey().getExhibitionSpot());

            //Delete the clubSpot from the club
            clubSpot.getClubSpotKey().getClub().getClubSpots().remove(clubSpot);
            this.clubRepository.save(clubSpot.getClubSpotKey().getClub());

            //Remove the clubSpot
            this.clubSpotRepository.deleteById(clubSpot.getClubSpotKey());

            //If the clubSpot to be deleted doesn't exist, throw exception
        }else{
            throw new ClubSpotNotFoundException();
        }
    }

    @Override
    public void deleteAllClubSpot() {
        List<ClubSpot> clubSpotList = this.clubSpotRepository.findAll();
        for(ClubSpot clubSpot : clubSpotList){
            deleteClubSpot(clubSpot);
        }
    }

    @Override
    public List<ClubSpot> searchClubSpot(Club club, ExhibitionSpot exhibitionSpot) {
        List<ClubSpot> clubSpotList = new ArrayList<>();

        //Search by both exhibitionSpot and product
        if(exhibitionSpot != null && club != null){

            //ExhibitionSpot must has id
            if(exhibitionSpot.getId() == null){
                throw new ExhibitionSpotHasNoIdException();
            }

            //Club must has id
            if(club.getId() == null){
                throw new ClubHasNoIdException();
            }

            ClubSpotKey clubSpotKey = new ClubSpotKey(club, exhibitionSpot);
            Optional<ClubSpot> existClubSpot = this.clubSpotRepository.findById(clubSpotKey);
            clubSpotList.add(existClubSpot.get());

            //Search only by exhibitionSpot
        }else if(exhibitionSpot != null){
            //ExhibitionSpot must has id
            if(exhibitionSpot.getId() == null){
                throw new ExhibitionSpotHasNoIdException();
            }

            clubSpotList = this.clubSpotRepository.findByClubSpotKeyExhibitionSpot(exhibitionSpot);

            //Search only by club
        }else{
            //Club must has id
            if(club.getId() == null){
                throw new ClubHasNoIdException();
            }

            clubSpotList = this.clubSpotRepository.findByClubSpotKeyClub(club);
        }

        if(clubSpotList.size() == 0){
            throw new NoResultFoundException();
        }

        return clubSpotList;

    }

    @Override
    public List<ClubSpot> getAllClubSpot() {
        List<ClubSpot> clubSpotList = this.clubSpotRepository.findAll();

        //If there is no records in the table, throw exception
        if(clubSpotList.size() == 0){
            throw new TableIsEmptyException();

            //If there is records in the table, return all records
        }else{
            return clubSpotList;
        }
    }
}
