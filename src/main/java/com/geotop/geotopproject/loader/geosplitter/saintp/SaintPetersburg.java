package com.geotop.geotopproject.loader.geosplitter.saintp;


import com.geotop.geotopproject.loader.geosplitter.CommonSplitter;
import com.geotop.geotopproject.loader.geosplitter.model.GeoArea;
import com.geotop.geotopproject.loader.geosplitter.model.GeoPoint;

public interface SaintPetersburg extends CommonSplitter {
    int MAX_RADIUS = 6000;

    GeoArea GEO_AREA = new GeoArea(
            new GeoPoint(60.074455, 30.180011),
            new GeoPoint(60.063974, 30.495000),
            new GeoPoint(59.825841, 30.146778),
            new GeoPoint(59.833893, 30.515641)
    );

}
