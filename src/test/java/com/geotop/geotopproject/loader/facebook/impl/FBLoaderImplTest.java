package com.geotop.geotopproject.loader.facebook.impl;

import com.geotop.geotopproject.loader.helper.CollisionResolver;
import com.geotop.geotopproject.model.places.Place;
import com.geotop.geotopproject.model.places.api.FbData;
import com.geotop.geotopproject.model.places.deserializer.PlaceDeserializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FBLoaderImplTest {
    @Mock
    private CollisionResolver collisionResolver;
    @Mock
    private PlaceDeserializer placeDeserializer;

    @InjectMocks
    FBLoaderImpl fbLoader;

//    @Test
//    public void loadData() throws Exception {
//        List<Place> places = new ArrayList<>();
//        places.add(new Place());
//
//        when(collisionResolver.resolvePlaceCollision(places)).thenReturn(places);
//        assertEquals(places,collisionResolver.resolvePlaceCollision(places));
//
//        when(fbLoader.loadData()).thenReturn(places);
//        assertEquals(places,fbLoader.loadData());
//
//    }

}