package com.geotop.geotopproject.service;

import com.geotop.geotopproject.dao.PlaceCollectionRepository;
import com.geotop.geotopproject.dao.PlaceRepository;
import com.geotop.geotopproject.loader.CommonLoader;
import com.geotop.geotopproject.loader.helper.CollisionResolver;
import com.geotop.geotopproject.loader.helper.PopularityRatingProcessor;
import com.geotop.geotopproject.model.places.Place;
import com.geotop.geotopproject.model.places.PlaceCollection;
import com.geotop.geotopproject.utils.Utils;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private PlaceCollectionRepository placeCollectionRepository;
    @Mock
    private CollisionResolver collisionResolver;
    @Mock
    private PopularityRatingProcessor popularityRatingProcessor;

    @InjectMocks
    PlaceService placeService;
    @InjectMocks
    LoaderService loaderService;

    @Test
    public void getPlacesByType() throws Exception {
        //prepare
        List<Place> places = new ArrayList<>();
        places.add(new Place());
        PlaceCollection placeCollection = new PlaceCollection();
        placeCollection.setPlaces(places);
        List<PlaceCollection> placeCollections = new ArrayList<>();
        placeCollections.add(placeCollection);
        when(placeCollectionRepository.findByTitle(anyString())).thenReturn(placeCollections);

        //testing
        List<Place> placesByType = placeService.getPlacesByType(anyString());

        //validate
        verify(placeCollectionRepository).findByTitle(anyString());
        Assertions.assertEquals(placesByType, places);

    }

    @Test
    public void getPlacesById() throws Exception {
        //prepare
        Place place = new Place();
        when(placeRepository.findById(anyString())).thenReturn(place);
        //testing
        Place placeById = placeService.getPlaceById(anyString());
        //validate
        verify(placeRepository).findById(anyString());
        Assertions.assertEquals(placeById, place);
    }

    @Test
    public void getTopPlacesByType() throws Exception {
        List<Place> places = new ArrayList<>();
        places.add(new Place());
        PlaceCollection placeCollection = new PlaceCollection();
        placeCollection.setPlaces(places);
        List<PlaceCollection> placeCollections = new ArrayList<>();
        placeCollections.add(placeCollection);
        when(placeCollectionRepository.findByTitle(anyString())).thenReturn(placeCollections);

        List<Place> topPlacesByType = placeService.getTopPlacesByType(anyString());

        Assertions.assertEquals(topPlacesByType, places);
    }

//    @Test
//    public void load() throws Exception {
//        List<Place> places = new ArrayList<>();
//        places.add(new Place());
//        when(loaderService.load(mock(CommonLoader.class))).thenReturn(places);
//
//        List<Place> load = loaderService.load(mock(CommonLoader.class));
//
//        Assertions.assertEquals(new ArrayList<>(), load);
//
//    }

    @Test
    public void save() throws Exception {
        List<Place> places = new ArrayList<>();
        places.add(new Place());
        PlaceCollection placeCollection = new PlaceCollection();
        placeCollection.setPlaces(places);
        List<PlaceCollection> placeCollections = new ArrayList<>();
        placeCollections.add(placeCollection);
        when(placeCollectionRepository.save(placeCollections)).thenReturn(null);

        loaderService.save(placeCollections);
    }

    @Test
    public void resolveCollision() throws Exception {
        List<Place> places = new ArrayList<>();
        places.add(new Place());
        when(collisionResolver.resolvePlaceCollision(places)).thenReturn(places);

        List<Place> placeList = loaderService.resolveCollision(places);

        Assertions.assertEquals(placeList, places);
    }

    @Test
    public void collectPlaces() throws Exception {
        List<Place> places = new ArrayList<>();
        places.add(new Place());
        List<Place> places2 = new ArrayList<>();
        places.add(new Place());
        List<List<Place>> listPlaces = Arrays.asList(places, places2);
        Stream<Place> stream = listPlaces.stream().flatMap(Collection::stream);

        List<Place> placeList = loaderService.collectPlaces(places, places2);

        Assertions.assertEquals(placeList, stream.collect(Collectors.toList()));
    }

    @Test
    public void initPlacesTypes() throws Exception {
        List<Place> places = new ArrayList<>();
        places.add(new Place());
        PlaceCollection placeCollection = new PlaceCollection();
        placeCollection.setPlaces(places);
        List<PlaceCollection> placeCollections = new ArrayList<>();
        placeCollections.add(placeCollection);
        if (when(placeCollectionRepository.findById(anyInt())).thenReturn(null) == null) {
            when(placeCollectionRepository.insert(placeCollections)).thenReturn(placeCollections);
        }

        loaderService.initPlacesTypes();

        Assertions.assertEquals(placeCollectionRepository.findById(anyInt()), null);

    }
}