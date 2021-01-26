package com.geotop.geotopproject.controller.model;

import com.geotop.geotopproject.model.places.Place;
import com.geotop.geotopproject.service.PlaceService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

    @Mock
    private PlaceService placeService;

    @InjectMocks
    PlaceController placeController;

    @Test
    public void placesByTypeTest(){
        List<Place> places = new ArrayList<>();
        places.add(new Place());
        when(placeController.getPlacesByType(anyString())).thenReturn(places);
        Assertions.assertEquals(places, placeController.getPlacesByType(anyString()));
    }
}
