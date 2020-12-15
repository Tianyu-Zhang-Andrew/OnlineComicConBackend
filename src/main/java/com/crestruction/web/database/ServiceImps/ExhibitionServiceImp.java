package com.crestruction.web.database.ServiceImps;

import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ExhibitionSpotAlreadyExistException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ClubNotFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ExhibitionSpotNotFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.NoResultFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.TableIsEmptyException;
import com.crestruction.web.database.repositories.ExhibitionSpotRepository;
import com.crestruction.web.database.services.ExhibitionSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionServiceImp implements ExhibitionSpotService{
    @Autowired
    private ExhibitionSpotRepository exhibitionSpotRepository;

    @Override
    public ExhibitionSpot saveExhibitionSpot(ExhibitionSpot exhibitionSpot) {
        //Check if the exhibitionSpot already exists
        Example<ExhibitionSpot> clubExample =  Example.of(exhibitionSpot);
        Optional<ExhibitionSpot> existExhibitionSpot = this.exhibitionSpotRepository.findOne(clubExample);

        //Is the exhibitionSpot already exists, throw exception
        if(existExhibitionSpot.isPresent()) {
            throw new ExhibitionSpotAlreadyExistException();

            //If exhibitionSpot doesn't exist, create exhibitionSpot
        }else {
            ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotRepository.save(exhibitionSpot);
            return savedExhibitionSpot;
        }
    }

    @Override
    public ExhibitionSpot editExhibitionSpot(ExhibitionSpot exhibitionSpot) {
        //Check if the edited exhibitionSpot exists
        Optional<ExhibitionSpot> existExhibitionSpot = this.exhibitionSpotRepository.findById(exhibitionSpot.getId());

        //If the exhibitionSpot doesn't exists, throw exception
        if(!existExhibitionSpot.isPresent()) {
            throw new ExhibitionSpotNotFoundException(exhibitionSpot.getId());

            //If the exhibitionSpot exists, edit the exhibitionSpot
        }else {
            ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotRepository.save(exhibitionSpot);
            return savedExhibitionSpot;
        }
    }

    @Override
    public void deleteExhibitionSpot(ExhibitionSpot exhibitionSpot) {
        //Check if the exhibitionSpot to be deleted exists
        Optional<ExhibitionSpot> existExhibitionSpot = this.exhibitionSpotRepository.findById(exhibitionSpot.getId());

        //If the exhibitionSpot doesn't exists, throw exception
        if(!existExhibitionSpot.isPresent()) {
            throw new ExhibitionSpotNotFoundException(exhibitionSpot.getId());

            //If the exhibitionSpot exists, delete the exhibitionSpot
        }else {
            this.exhibitionSpotRepository.delete(exhibitionSpot);
        }
    }

    @Override
    public void deleteAllExhibitionSpot() {
        this.exhibitionSpotRepository.deleteAll();
    }

    @Override
    public List<ExhibitionSpot> searchExhibitionSpot(ExhibitionSpot exhibitionSpot) {
        //Dynamically search for the result
        Example<ExhibitionSpot> exhibitionSpotExample =  Example.of(exhibitionSpot);
        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotRepository.findAll(exhibitionSpotExample);

        //If there is no result found, throw exception
        if(exhibitionSpotList.size() == 0){
            throw new NoResultFoundException();

            //If result found, return all results
        }else {
            return exhibitionSpotList;
        }
    }

    @Override
    public List<ExhibitionSpot> getAllExhibitionSpot() {
        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotRepository.findAll();

        //If there is no records in the table, throw exception
        if(exhibitionSpotList.size() == 0){
            throw new TableIsEmptyException();

            //If there is records in the table, return all records
        }else{
            return exhibitionSpotList;
        }
    }
}
