package com.crestruction.web;

import com.crestruction.web.database.CompositeKeys.ClubProductKey;
import com.crestruction.web.database.CompositeKeys.ClubSpotKey;
import com.crestruction.web.database.entities.Club;
import com.crestruction.web.database.entities.ClubProduct;
import com.crestruction.web.database.entities.ClubSpot;
import com.crestruction.web.database.entities.ExhibitionSpot;
import com.crestruction.web.database.exceptions.AlreadyExistExceptions.ClubSpotAlreadyExistException;
import com.crestruction.web.database.exceptions.AttributeEntityIncompleteException.ClubHasNoIdException;
import com.crestruction.web.database.exceptions.NotFoundExceptions.ClubNotFoundException;
import com.crestruction.web.database.services.ClubService;
import com.crestruction.web.database.services.ClubSpotService;
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
public class ClubSpotTest {
    @Autowired
    private ClubService clubService;
    @Autowired
    private ExhibitionSpotService exhibitionSpotService;
    @Autowired
    private ClubSpotService clubSpotService;

//    @After
//    public void clear() throws Exception {
//        clubSpotService.deleteAllClubSpot();
//    }

    @Test
    public void testAddNewClubSpot(){
        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);
        long clubId = savedClub.getId();

        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);
        long exhibitionSpotId = savedExhibitionSpot.getId();

        //Save a clubSpot
        ClubSpotKey clubSpotKey = new ClubSpotKey(club, exhibitionSpot);
        ClubSpot clubSpot = new ClubSpot(clubSpotKey);
        this.clubSpotService.saveClubSpot(clubSpot);

        List<ClubSpot> clubSpotList = this.clubSpotService.getAllClubSpot();
        assert(clubSpotList.size() == 1);
        assert(clubSpotList.get(0).getClubSpotKey().getClub().getId().equals(clubId));
        assert(clubSpotList.get(0).getClubSpotKey().getExhibitionSpot().getId().equals(exhibitionSpotId));

        //Check the clubProduct has been added to the club and exhibitionSpot
        Club exampleClub = new Club();
        exampleClub.setId(clubId);
        List<Club> clubList = this.clubService.searchClub(exampleClub);
        assert(clubList.get(0).getClubSpots().size() == 1);

        ExhibitionSpot exampleExhibitionSpot = new ExhibitionSpot();
        exampleExhibitionSpot.setId(exhibitionSpotId);
        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotService.searchExhibitionSpot(exampleExhibitionSpot);
        assert(exhibitionSpotList.get(0).getClubSpots().size() == 1);

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        savedClub = this.clubService.saveClub(club2);
        long clubId2 = savedClub.getId();

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        ExhibitionSpot savedExhibitionSpot2 = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);
        long exhibitionSpotId2 = savedExhibitionSpot2.getId();

        //Save another clubSpot
        ClubSpotKey clubSpotKey2 = new ClubSpotKey(club2, exhibitionSpot2);
        ClubSpot clubSpot2 = new ClubSpot(clubSpotKey2);
        this.clubSpotService.saveClubSpot(clubSpot2);

        clubSpotList = this.clubSpotService.getAllClubSpot();
        assert(clubSpotList.size() == 2);
        assert(clubSpotList.get(1).getClubSpotKey().getClub().getId().equals(clubId2));
        assert(clubSpotList.get(1).getClubSpotKey().getExhibitionSpot().getId().equals(exhibitionSpotId2));

        //Test add clubSpot to a non-existing club
        Club club3 = new Club();
        club3.setId(clubId2 + 1);

        ClubSpotKey clubSpotKey3 = new ClubSpotKey(club3, exhibitionSpot2);
        ClubSpot clubSpot3 = new ClubSpot(clubSpotKey3);

        try {
            this.clubSpotService.saveClubSpot(clubSpot3);
        }catch (Exception  e){
            assert(e instanceof ClubNotFoundException);
        }

        //Test add clubSpot that is already existed
        Club club4 = new Club();
        club4.setId(clubId);

        ExhibitionSpot exhibitionSpot3 = new ExhibitionSpot();
        exhibitionSpot3.setId(exhibitionSpotId);

        ClubSpotKey clubSpotKey4 = new ClubSpotKey(club4, exhibitionSpot3);
        ClubSpot clubSpot4 = new ClubSpot(clubSpotKey4);

        try {
            this.clubSpotService.saveClubSpot(clubSpot4);
        }catch (Exception  e){
            assert(e instanceof ClubSpotAlreadyExistException);
        }

        //Test save clubSpot with club with no id
        Club club5 = new Club();
        ClubSpotKey clubSpotKey5 = new ClubSpotKey(club5, exhibitionSpot3);
        ClubSpot clubSpot5 = new ClubSpot(clubSpotKey5);

        try {
            this.clubSpotService.saveClubSpot(clubSpot5);
        }catch (Exception  e){
            assert(e instanceof ClubHasNoIdException);
        }
    }

    @Test
    public void testEditClubSpot(){
        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);
        long clubId = savedClub.getId();

        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);
        long exhibitionSpotId = savedExhibitionSpot.getId();

        //Save a clubSpot
        ClubSpotKey clubSpotKey = new ClubSpotKey(club, exhibitionSpot);
        ClubSpot clubSpot = new ClubSpot(clubSpotKey);
        this.clubSpotService.saveClubSpot(clubSpot);

        //Edit the Club
        savedClub.setName("club2");
        this.clubService.editClub(savedClub);

        //Check if the club is changed in the clubSpot
        Club exampleClub = new Club();
        exampleClub.setId(clubId);
        List<ClubSpot> clubSpotList = this.clubSpotService.searchClubSpot(exampleClub, null);
        assert(clubSpotList.size() == 1);
        assert(clubSpotList.get(0).getClubSpotKey().getClub().getName().equals("club2"));

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        Club savedClub2 = this.clubService.saveClub(club2);
        long clubId2 = savedClub2.getId();

        //Edit the saved clubSpot
        ClubSpotKey clubSpotKey2 = new ClubSpotKey(club2, exhibitionSpot);
        ClubSpot clubSpot2 = new ClubSpot(clubSpotKey2);
        this.clubSpotService.editClubSpot(clubSpot, clubSpot2);

        clubSpotList = this.clubSpotService.getAllClubSpot();
        assert(clubSpotList.size() == 1);
        assert(clubSpotList.get(0).getClubSpotKey().getClub().getId().equals(clubId2));

        //Check the old ClubSpot has been removed from the old club
        Club exampleClub2 = new Club();
        exampleClub2.setId(clubId);
        List<Club> clubList = this.clubService.searchClub(exampleClub2);
        assert(clubList.get(0).getClubSpots().size() == 0);

        //Check the ClubProduct has changed in the ExhibitionSpot
        ExhibitionSpot exampleExhibitionSpot = new ExhibitionSpot();
        exampleExhibitionSpot.setId(exhibitionSpotId);

        List<ExhibitionSpot> exhibitionSpotList = this.exhibitionSpotService.searchExhibitionSpot(exampleExhibitionSpot);
        assert(exhibitionSpotList.get(0).getClubSpots().size() == 1);
        for(ClubSpot clubSpotInList : exhibitionSpotList.get(0).getClubSpots()){
            assert(clubSpotInList.getClubSpotKey().getClub().getId().equals(clubId2));
        }
    }

    @Test
    public void testSearchClubSpot(){
        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);

        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);

        //Save a clubSpot
        ClubSpotKey clubSpotKey = new ClubSpotKey(club, exhibitionSpot);
        ClubSpot clubSpot = new ClubSpot(clubSpotKey);
        this.clubSpotService.saveClubSpot(clubSpot);

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        savedClub = this.clubService.saveClub(club2);
        long clubId2 = savedClub.getId();

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);
        long exhibitionSpotId2 = savedExhibitionSpot.getId();

        //Save another clubSpot
        ClubSpotKey clubSpotKey2 = new ClubSpotKey(club2, exhibitionSpot2);
        ClubSpot clubSpot2 = new ClubSpot(clubSpotKey2);
        this.clubSpotService.saveClubSpot(clubSpot2);

        //Save the third exhibitionSpot
        ExhibitionSpot exhibitionSpot3 = new ExhibitionSpot("size3", "info3");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot3);

        //Save the third clubSpot
        ClubSpotKey clubSpotKey3 = new ClubSpotKey(club2, exhibitionSpot3);
        ClubSpot clubSpot3 = new ClubSpot(clubSpotKey3);
        this.clubSpotService.saveClubSpot(clubSpot3);

        //Search all ClubSpots belong to club2
        List<ClubSpot> clubSpotList = this.clubSpotService.searchClubSpot(club2, null);
        assert(clubSpotList.size() == 2);

        //Search for ClubSpots2
        clubSpotList = this.clubSpotService.searchClubSpot(club2, exhibitionSpot2);
        assert(clubSpotList.size() == 1);
        assert(clubSpotList.get(0).getClubSpotKey().getClub().getId().equals(clubId2));
        assert(clubSpotList.get(0).getClubSpotKey().getExhibitionSpot().getId().equals(exhibitionSpotId2));
    }

    @Test
    public void testDeleteClubSpot(){
        //Save a club
        Club club = new Club("club1", "type1", "mainPageURL1", "logoURL1");
        Club savedClub = this.clubService.saveClub(club);

        //Save a exhibitionSpot
        ExhibitionSpot exhibitionSpot = new ExhibitionSpot("size1", "info1");
        this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot);

        //Save a clubSpot
        ClubSpotKey clubSpotKey = new ClubSpotKey(club, exhibitionSpot);
        ClubSpot clubSpot = new ClubSpot(clubSpotKey);
        this.clubSpotService.saveClubSpot(clubSpot);

        //Save another club
        Club club2 = new Club("club2", "type2", "mainPageURL2", "logoURL2");
        savedClub = this.clubService.saveClub(club2);
        long clubId2 = savedClub.getId();

        //Save another exhibitionSpot
        ExhibitionSpot exhibitionSpot2 = new ExhibitionSpot("size2", "info2");
        ExhibitionSpot savedExhibitionSpot = this.exhibitionSpotService.saveExhibitionSpot(exhibitionSpot2);
        long exhibitionSpotId2 = savedExhibitionSpot.getId();

        //Save another clubSpot
        ClubSpotKey clubSpotKey2 = new ClubSpotKey(club2, exhibitionSpot2);
        ClubSpot clubSpot2 = new ClubSpot(clubSpotKey2);
        this.clubSpotService.saveClubSpot(clubSpot2);

        //Delete clubSpot
        this.clubSpotService.deleteClubSpot(clubSpot);

        //Check there is only 1 clubSpot left
        assert(this.clubSpotService.getAllClubSpot().size() == 1);
        assert(this.clubSpotService.getAllClubSpot().get(0).getClubSpotKey().getClub().getId().equals(clubId2));
        assert(this.clubSpotService.getAllClubSpot().get(0).getClubSpotKey().getExhibitionSpot().getId().equals(exhibitionSpotId2));

        //Check the clubSpot has been removed from club abd exhibitionSpot
        assert(this.clubService.searchClub(club).get(0).getClubSpots().size() == 0);
        assert(this.exhibitionSpotService.searchExhibitionSpot(exhibitionSpot).get(0).getClubSpots().size() == 0);
    }
}
