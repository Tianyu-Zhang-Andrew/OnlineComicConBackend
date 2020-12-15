package com.crestruction.web.database.services;

import com.crestruction.web.database.entities.ExhibitionSpot;

import java.util.List;

public interface ExhibitionSpotService {
    ExhibitionSpot saveExhibitionSpot(ExhibitionSpot exhibitionSpot);
    ExhibitionSpot editExhibitionSpot(ExhibitionSpot exhibitionSpot);
    void deleteExhibitionSpot(ExhibitionSpot exhibitionSpot);
    void deleteAllExhibitionSpot();
    List<ExhibitionSpot> searchExhibitionSpot(ExhibitionSpot exhibitionSpot);
    List<ExhibitionSpot> getAllExhibitionSpot();
}
