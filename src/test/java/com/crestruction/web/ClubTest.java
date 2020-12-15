package com.crestruction.web;

import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ClubAlreadyExistException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ClubNotFoundException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.NoResultFoundException;
import com.crestruction.web.database.services.ClubService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
//@Rollback(false)
public class ClubTest {
    @Autowired
    private ClubService clubService;

//    @After
//    public void clear() throws Exception {
//        clubService.deleteAllClub();
//    }

    @Test
    public void testAddNewClub() throws Exception {
        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);

        List<Club> clubList = this.clubService.getAllClub();
        assert(clubList.size() == 1);
        assert(clubList.get(0).getId().equals(savedClub.getId()));

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        this.clubService.saveClub(club2);
        clubList = this.clubService.getAllClub();
        assert(clubList.size() == 2);

        //Save a club that is already existed
        try{
            this.clubService.saveClub(club2);
        }catch (Exception  e) {
            assert(e instanceof ClubAlreadyExistException);

            //Test the existed club will not be saved again
            clubList = this.clubService.getAllClub();
            assert(clubList.size() == 2);
        }
    }

    @Test
    public void testEditClub() throws Exception{
        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);
        long clubId = savedClub.getId();

        List<Club> clubList = this.clubService.getAllClub();
        assert(clubList.size() == 1);
        assert(clubList.get(0).getName().equals("club1"));

        //Edit the club name
        Club editedClub = new Club();
        editedClub.setId(clubId);
        editedClub.setName("club2");

        this.clubService.editClub(editedClub);
        clubList = this.clubService.getAllClub();
        assert(clubList.size() == 1);
        assert(clubList.get(0).getName().equals("club2"));
        assert(clubList.get(0).getId().equals(clubId));

        //Try to edit a club that doesn't exist
        Club editedClub2 = new Club();
        editedClub2.setId(clubId + 1);
        editedClub2.setName("club3");

        try{
            this.clubService.editClub(editedClub2);
        }catch (Exception  e) {
            assert(e instanceof ClubNotFoundException);

            //Test the edit is not made
            clubList = this.clubService.getAllClub();
            assert(clubList.get(0).getName().equals("club2"));
        }
    }

    @Test
    public void testSearchClub() throws Exception{
        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        this.clubService.saveClub(club);

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        this.clubService.saveClub(club2);

        //Save the third club
        Club club3 = new Club("club3", "type1", "mainPageURL3", "logoURL3");
        this.clubService.saveClub(club3);

        //Search all type1 clubs(should have 2 results)
        Club exampleClub = new Club();
        exampleClub.setType("type1");
        List<Club> clubList = this.clubService.searchClub(exampleClub);
        assert(clubList.size() == 2);

        //Try to search for club that doesn't exist
        Club exampleClub2 = new Club();
        exampleClub2.setType("type3");

        try {
            this.clubService.searchClub(exampleClub2);
        }catch (Exception  e){
            assert(e instanceof NoResultFoundException);
        }
    }

    @Test
    public void testDeleteClub() throws Exception{
        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        this.clubService.saveClub(club);

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        this.clubService.saveClub(club2);

        //Save the third club
        Club club3 = new Club("club3", "type1", "mainPageURL3", "logoURL3");
        Club savedClub = this.clubService.saveClub(club3);
        long clubId3 = savedClub.getId();

        //Try to delete club3
        Club deleteClub = new Club();
        deleteClub.setId(clubId3);
        this.clubService.deleteClub(deleteClub);

        //There should only be 2 clubs left
        List<Club> clubList = this.clubService.getAllClub();
        assert(clubList.size() == 2);

        //Try to delete club that doesn't exist
        Club deleteClub2 = new Club();
        deleteClub2.setId(clubId3);
        try {
            this.clubService.deleteClub(deleteClub2);
        }catch (Exception  e){
            assert(e instanceof ClubNotFoundException);
        }
    }
}
