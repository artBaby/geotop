package com.geotop.geotopproject.loader.geosplitter.saintp;


import com.geotop.geotopproject.loader.geosplitter.DistanceResolver;
import com.geotop.geotopproject.loader.geosplitter.model.GeoArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("saintPetersburgSplitter")
public class SaintPetersburgSplitter implements SaintPetersburg {
    private static final Logger LOG = LoggerFactory.getLogger(SaintPetersburgSplitter.class);

    private DistanceResolver distanceResolver;

    @Autowired
    public SaintPetersburgSplitter(DistanceResolver distanceResolver) {
        this.distanceResolver = distanceResolver;
    }

    public List<GeoArea> getGeoAreas() {
        List<GeoArea> geoAreas = new ArrayList<>();

        geoAreas.add(new GeoArea(GEO_AREA.getUpLeftPoint(), GEO_AREA.getUpRightPoint()
                ,distanceResolver.getMiddlePoint(GEO_AREA.getUpLeftPoint(), GEO_AREA.getBottomLeftPoint())
                ,distanceResolver.getMiddlePoint(GEO_AREA.getUpRightPoint(), GEO_AREA.getBottomRightPoint())
        ));
        geoAreas.add(new GeoArea(distanceResolver.getMiddlePoint(GEO_AREA.getUpLeftPoint(), GEO_AREA.getBottomLeftPoint())
                ,distanceResolver.getMiddlePoint(GEO_AREA.getUpRightPoint(), GEO_AREA.getBottomRightPoint())
                ,GEO_AREA.getBottomLeftPoint(), GEO_AREA.getBottomRightPoint()
        ));

        for (GeoArea area : geoAreas) {
            area.setCenter(distanceResolver.getCentralGeoPoint(Arrays.asList(area.getUpLeftPoint(), area.getUpRightPoint()
                ,area.getBottomLeftPoint(), area.getBottomRightPoint())));
        }
        return geoAreas;
    }

}
