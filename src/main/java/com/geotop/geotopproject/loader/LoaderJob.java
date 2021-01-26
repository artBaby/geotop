package com.geotop.geotopproject.loader;

import com.geotop.geotopproject.loader.facebook.FBLoader;
import com.geotop.geotopproject.loader.geosplitter.CommonSplitter;
import com.geotop.geotopproject.loader.geosplitter.model.GeoArea;
import com.geotop.geotopproject.loader.vk.VKLoader;
import com.geotop.geotopproject.model.places.Place;
import com.geotop.geotopproject.model.places.PlaceCollection;
import com.geotop.geotopproject.service.LoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoaderJob {

    private static final Logger LOG = LoggerFactory.getLogger(LoaderJob.class);

    private LoaderService loaderService;
    private VKLoader vkLoader;
    private FBLoader fbLoader;
    private CommonSplitter saintPetersburgSplitter;

    @Autowired
    public LoaderJob(LoaderService loaderService, VKLoader vkLoader, FBLoader fbLoader, CommonSplitter saintPetersburgSplitter) {
        this.loaderService = loaderService;
        this.vkLoader = vkLoader;
        this.fbLoader = fbLoader;
        this.saintPetersburgSplitter = saintPetersburgSplitter;
    }

    //TODO: ADD CHECK WHETHER USER'S COORDINATES ARE PRESENT IN DB. IF NOT, LOAD NEW DATA IMMEDIATELY
    // will be replaced with cron
    @Scheduled(fixedRate = 500000000)
    public void updateDatabase() {
        try {
            initPlacesTypes(loaderService);
            loadAndSaveData(loaderService);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadAndSaveData(LoaderService loaderService) throws Exception{
        List<PlaceCollection> placeCollectionList;
        List<GeoArea> areas = saintPetersburgSplitter.getGeoAreas();
        List<Place> vkPlaces = loaderService.load(vkLoader, areas);
        //List<Place> fbPlaces = loaderService.load(fbLoader);

        // merge places from all APIs
        //List<Place> mergedPlaces = loaderService.collectPlaces(vkPlaces, fbPlaces);
        LOG.info("places are merged");
        //resolve collision
        //mergedPlaces = loaderService.resolveCollision(mergedPlaces);

        //categorize places
        //placeCollectionList = loaderService.categorizeAndIndexPlaces(mergedPlaces);
        placeCollectionList = loaderService.categorizeAndIndexPlaces(vkPlaces);

        //save to db
        loaderService.save(placeCollectionList);
        LOG.info("data was saved to db");
    }

    private void initPlacesTypes(LoaderService loaderService) {
        loaderService.initPlacesTypes();
    }

}
