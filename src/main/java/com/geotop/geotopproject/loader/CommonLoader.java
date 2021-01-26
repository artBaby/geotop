package com.geotop.geotopproject.loader;


import com.geotop.geotopproject.loader.geosplitter.model.GeoArea;
import com.geotop.geotopproject.model.places.Place;
import com.geotop.geotopproject.model.places.PlaceCollection;

import java.util.List;

public interface CommonLoader {
    List<Place> loadData() throws Exception;
    List<Place> loadData(List<GeoArea> geoAreas) throws Exception;
}
