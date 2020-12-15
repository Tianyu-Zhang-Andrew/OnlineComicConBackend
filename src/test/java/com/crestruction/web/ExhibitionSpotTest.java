package com.crestruction.web;

import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ExhibitionSpotAlreadyExistException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ExhibitionSpotNotFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.NoResultFoundException;
import com.crestruction.web.database.services.ExhibitionSpotService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
//@Rollback(false)
public class ExhibitionSpotTest {
    @Autowired
    private ExhibitionSpotService exhibitionSpotService;

//    @After
//    public void clear() throws Exception {
//        exhibitionSpotService.deleteAllExhibitionSpot();
//    }

    @Test
    public void testAddNewExhibitionSpot() throws Exception {
        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);

        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotService.getAllExhibitionSpot();
        assert(exhibitionSpotList.size() == 1);
        assert(exhibitionSpotList.get(0).getId().equals(savedExhibitionSpot.getId()));

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);

        exhibitionSpotList = this.exhibitionSpotService.getAllExhibitionSpot();
        assert(exhibitionSpotList.size() == 2);

        //Save a club that is already existed
        try{
            this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);
        }catch (Exception  e) {
            assert(e instanceof ExhibitionSpotAlreadyExistException);

            //Test the existed club will not be saved again
            exhibitionSpotList = this.exhibitionSpotService.getAllExhibitionSpot();
            assert(exhibitionSpotList.size() == 2);
        }
    }

    @Test
    public void testEditExhibitionSpot() throws Exception{
        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);

        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotService.getAllExhibitionSpot();
        assert(exhibitionSpotList.size() == 1);
        assert(exhibitionSpotList.get(0).getId().equals(savedExhibitionSpot.getId()));

        //Edit the ExhibitionSpot
        ExhibitionSpot editedExhibitionSpot = new ExhibitionSpot();
        editedExhibitionSpot.setId(savedExhibitionSpot.getId());
        editedExhibitionSpot.setAdditionalInfo("info2");

        this.exhibitionSpotService.editExhibitionSpot(editedExhibitionSpot);
        exhibitionSpotList = this.exhibitionSpotService.getAllExhibitionSpot();
        assert(exhibitionSpotList.size() == 1);
        assert(exhibitionSpotList.get(0).getAdditionalInfo().equals("info2"));
        assert(exhibitionSpotList.get(0).getId().equals(savedExhibitionSpot.getId()));

        //Try to edit a editedExhibitionSpot that doesn't exist
        ExhibitionSpot editedClub2 = new ExhibitionSpot();
        editedClub2.setId(savedExhibitionSpot.getId() + 1);
        editedClub2.setAdditionalInfo("info3");

        try{
            this.exhibitionSpotService.editExhibitionSpot(editedClub2);
        }catch (Exception  e) {
            assert(e instanceof ExhibitionSpotNotFoundException);

            //Test the edit is not made
            exhibitionSpotList = this.exhibitionSpotService.getAllExhibitionSpot();
            assert(exhibitionSpotList.get(0).getAdditionalInfo().equals("info2"));
        }
    }

    @Test
    public void testSearchExhibitionSpot() throws Exception{
        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);

        //Save the third exhibitionSpot
        ExhibitionSpot exhibitionSpot3 = new ExhibitionSpot("size2", "info3");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot3);

        //Search all size2 exhibitionSpots(should have 2 results)
        ExhibitionSpot exampleExhibitionSpot = new ExhibitionSpot();
        exampleExhibitionSpot.setSize("size2");
        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotService.searchExhibitionSpot(exampleExhibitionSpot);
        assert(exhibitionSpotList.size() == 2);

        //Try to search for exhibitionSpot that doesn't exist
        ExhibitionSpot exampleExhibitionSpot2 = new ExhibitionSpot();
        exampleExhibitionSpot2.setSize("size3");

        try {
            this.exhibitionSpotService.searchExhibitionSpot(exampleExhibitionSpot2);
        }catch (Exception  e){
            assert(e instanceof NoResultFoundException);
        }
    }

    @Test
    public void testDeleteExhibitionSpot() throws Exception{
        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);

        //Save the third exhibitionSpot
        ExhibitionSpot exhibitionSpot3 = new ExhibitionSpot("size2", "info3");
        ExhibitionSpot savedExhibitionSpot3 = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot3);
        long exhibitionSpotId3 = savedExhibitionSpot3.getId();

        //Try to delete exhibitionSpot3
        ExhibitionSpot deleteExhibitionSpot = new ExhibitionSpot();
        deleteExhibitionSpot.setId(exhibitionSpotId3);
        this.exhibitionSpotService.deleteExhibitionSpot(deleteExhibitionSpot);

        //There should only be 2 exhibitionSpots left
        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotService.getAllExhibitionSpot();
        assert(exhibitionSpotList.size() == 2);

        //Try to delete exhibitionSpot that doesn't exist
        ExhibitionSpot deleteClub2 = new ExhibitionSpot();
        deleteClub2.setId(exhibitionSpotId3);
        try {
            this.exhibitionSpotService.deleteExhibitionSpot(deleteClub2);
        }catch (Exception  e){
            assert(e instanceof ExhibitionSpotNotFoundException);
        }
    }
}
