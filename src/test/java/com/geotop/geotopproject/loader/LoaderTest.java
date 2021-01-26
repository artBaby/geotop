package com.geotop.geotopproject.loader;

import com.geotop.geotopproject.dao.impl.PlaceRepositoryImpl;
import com.geotop.geotopproject.loader.geosplitter.DistanceResolver;
import com.geotop.geotopproject.loader.geosplitter.model.GeoArea;
import com.geotop.geotopproject.loader.geosplitter.model.GeoPoint;
import com.geotop.geotopproject.loader.geosplitter.saintp.SaintPetersburg;
import com.geotop.geotopproject.loader.geosplitter.saintp.SaintPetersburgSplitter;
import com.geotop.geotopproject.loader.helper.CollisionResolver;
import com.geotop.geotopproject.loader.helper.SentimentAnalyser;
import com.geotop.geotopproject.loader.vk.VKLoader;
import com.geotop.geotopproject.loader.vk.impl.VKLoaderImpl;
import com.geotop.geotopproject.model.places.Checkin;
import com.geotop.geotopproject.model.places.Place;
import com.geotop.geotopproject.model.places.deserializer.PlaceDeserializer;
import com.geotop.geotopproject.service.LoaderService;
import com.geotop.geotopproject.service.PlaceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.geotop.geotopproject.loader.geosplitter.saintp.SaintPetersburg.GEO_AREA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoaderTest {
    @Mock
    private PlaceService placeService;
    @Mock
    private LoaderService loaderService;
    @Mock
    private PlaceRepositoryImpl placeRepository;

    @Mock
    private SentimentAnalyser analyser;
    @Mock
    private DistanceResolver dr;
    @Mock
    private SaintPetersburgSplitter saintPetersburgSplitter;
    @Mock
    private PlaceDeserializer placeDeserializerVK;
    @Mock
    private CollisionResolver collisionResolver;
    @Mock
    private SentimentAnalyser sentimentAnalyser;
    @InjectMocks
    private VKLoaderImpl vkLoader;

    private GeoPoint centerEthalon;
    private GeoPoint upLeftPoint;
    private GeoPoint upRightPoint;
    private GeoPoint bottomLeftPoint;
    private GeoPoint bottomRightPoint;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);

        centerEthalon = new GeoPoint(59.889697457216414, 30.332824425552033);
        upLeftPoint = new GeoPoint(59.95012704883866,30.163453081497472);
        upRightPoint = new GeoPoint(59.948945708720636, 30.505371010696212);
        bottomLeftPoint = new GeoPoint(59.825841, 30.146778);
        bottomRightPoint = new GeoPoint(59.833893, 30.515641);
    }

    @Test
    public void testMockCreation(){
        Assert.assertNotNull(dr);
        Assert.assertNotNull(saintPetersburgSplitter);
    }

    @Test
    public void contextLoads() {
        List<Checkin> checkins = Collections.emptyList();
        String message = analyser.ratePlace(checkins);
        Assert.assertEquals(null, message);
    }

    @Test
    public void areasCountTest(){
        DistanceResolver dr = new DistanceResolver();
        SaintPetersburg saintPetersburgSplitter = new SaintPetersburgSplitter(dr);
        List<GeoArea> areaList = saintPetersburgSplitter.getGeoAreas();
        Assert.assertEquals(2,areaList.size());
    }

    @Test
    public void geoPointCoordsTest(){
        DistanceResolver dr = new DistanceResolver();
        SaintPetersburg saintPetersburgSplitter = new SaintPetersburgSplitter(dr);
        List<GeoArea> areaList = saintPetersburgSplitter.getGeoAreas();

        //Testing coordinates of the first point
        GeoPoint testPoint = new GeoPoint(59.950149044374974, 30.163332183707606);
        System.out.println(areaList.get(0).toString());
        Assert.assertEquals(testPoint.toString(), areaList.get(0).getBottomLeftPoint().toString());

        testPoint.setLongitude(30.50535631761174);
        testPoint.setLatitude(59.94893390289354);
        Assert.assertEquals(testPoint.toString(),areaList.get(0).getBottomRightPoint().toString());

        testPoint.setLongitude(30.180011);
        testPoint.setLatitude(60.074455);
        Assert.assertEquals(testPoint.toString(),areaList.get(0).getUpLeftPoint().toString());

        testPoint.setLongitude(30.495);
        testPoint.setLatitude(60.063974);
        Assert.assertEquals(testPoint.toString(),areaList.get(0).getUpRightPoint().toString());
    }

    @Test
    public void geoPointCentersTest(){
        DistanceResolver dr = new DistanceResolver();
        SaintPetersburg saintPetersburgSplitter = new SaintPetersburgSplitter(dr);
        List<GeoArea> areaList = saintPetersburgSplitter.getGeoAreas();
        Assert.assertEquals(String.valueOf(30.33593607321715), String.valueOf(areaList.get(0).getCenter().getLongitude()));
        Assert.assertEquals(String.valueOf(60.00948011218167), String.valueOf(areaList.get(0).getCenter().getLatitude()));
        Assert.assertEquals(String.valueOf(30.332764450997775), String.valueOf(areaList.get(1).getCenter().getLongitude()));
        Assert.assertEquals(String.valueOf(59.88982404946711), String.valueOf(areaList.get(1).getCenter().getLatitude()));
    }

    @Test
    public void distanceResolverMiddlePointTest(){
        DistanceResolver dr = new DistanceResolver();
        GeoPoint geoPointTest = dr.getMiddlePoint(GEO_AREA.getUpLeftPoint(), GEO_AREA.getBottomLeftPoint());
        Assert.assertEquals("59.950149044374974, 30.163332183707606", geoPointTest.toString());
    }

    @Test
    public void distanceResolverCentralGeoPointTest(){
        DistanceResolver dr = new DistanceResolver();
        centerEthalon = new GeoPoint(59.889821468130634, 30.332798243818594);
        upLeftPoint = new GeoPoint(59.95012704883866,30.163453081497472);
        upRightPoint = new GeoPoint(59.948945708720636, 30.505371010696212);
        bottomLeftPoint = new GeoPoint(59.825841, 30.146778);
        bottomRightPoint = new GeoPoint(59.833893, 30.515641);
        GeoPoint centerTest = dr.getCentralGeoPoint(Arrays.asList(upLeftPoint,upRightPoint, bottomLeftPoint, bottomRightPoint));
        Assert.assertEquals(centerEthalon.toString(),centerTest.toString());
    }

    @Test
    public void loadMultipleAreasVKTest() throws Exception {
        VKLoaderImpl vkLoader = new VKLoaderImpl(collisionResolver, placeDeserializerVK, sentimentAnalyser);
        vkLoader = Mockito.spy(vkLoader);

        List<GeoArea> geoAreas = getEthalonAreas();
        for (GeoArea area : geoAreas) {
            Assert.assertNotNull(area.getCenter());
            Assert.assertNotNull(area.getUpLeftPoint());
            Assert.assertNotNull(area.getUpRightPoint());
            Assert.assertNotNull(area.getBottomLeftPoint());
            Assert.assertNotNull(area.getBottomRightPoint());
        }

        vkLoader.loadPlaces(geoAreas);
        verify(vkLoader, atLeast(2)).loadPlacesByArea(any(GeoArea.class));
    }

    @Test
    public void requestBuilderTest() throws Exception {
        VKLoaderImpl vkLoader = new VKLoaderImpl(collisionResolver, placeDeserializerVK, sentimentAnalyser);
        vkLoader = Mockito.spy(vkLoader);

        GeoArea geoArea = new GeoArea(upLeftPoint,upRightPoint, bottomLeftPoint, bottomRightPoint);
        geoArea.setCenter(centerEthalon);
        vkLoader.loadPlacesByArea(geoArea);

        // verify loader called 5 times (for each point)
        verify(vkLoader, atLeast(5)).getRequestUrl(anyString(), anyString(), any(GeoPoint.class), anyInt(), anyInt(), anyInt());

        // assert url with specified parameters
        String urlCenter = "https://api.vk.com/method/places.search?latitude=59.889697457216414&longitude=30.332824425552033&radius=3&offset=0&count=1000&access_token=cc08c95d25e5eb527ebac704fa1cf4f6fecd09c5606eafb6c91e955aff473bbacff4bdcf51a93b03e3216&v=5.74";
        String urlUpLeft = "https://api.vk.com/method/places.search?latitude=59.95012704883866&longitude=30.163453081497472&radius=2&offset=0&count=1000&access_token=cc08c95d25e5eb527ebac704fa1cf4f6fecd09c5606eafb6c91e955aff473bbacff4bdcf51a93b03e3216&v=5.74";
        String urlUpRight = "https://api.vk.com/method/places.search?latitude=59.948945708720636&longitude=30.505371010696212&radius=2&offset=0&count=1000&access_token=cc08c95d25e5eb527ebac704fa1cf4f6fecd09c5606eafb6c91e955aff473bbacff4bdcf51a93b03e3216&v=5.74";
        String urlBotLeft = "https://api.vk.com/method/places.search?latitude=59.825841&longitude=30.146778&radius=2&offset=0&count=1000&access_token=cc08c95d25e5eb527ebac704fa1cf4f6fecd09c5606eafb6c91e955aff473bbacff4bdcf51a93b03e3216&v=5.74";
        String urlBotRight = "https://api.vk.com/method/places.search?latitude=59.833893&longitude=30.515641&radius=2&offset=0&count=1000&access_token=cc08c95d25e5eb527ebac704fa1cf4f6fecd09c5606eafb6c91e955aff473bbacff4bdcf51a93b03e3216&v=5.74";
        Assert.assertEquals(urlCenter, vkLoader.getRequestUrl(VKLoader.API_PLACES_URL, VKLoader.ACCESS_TOKEN, geoArea.getCenter(), VKLoader.CENTER_RADIUS, 0, 1000));
        Assert.assertEquals(urlUpLeft, vkLoader.getRequestUrl(VKLoader.API_PLACES_URL, VKLoader.ACCESS_TOKEN, geoArea.getUpLeftPoint(), VKLoader.CORNER_RADIUS, 0, 1000));
        Assert.assertEquals(urlUpRight, vkLoader.getRequestUrl(VKLoader.API_PLACES_URL, VKLoader.ACCESS_TOKEN, geoArea.getUpRightPoint(), VKLoader.CORNER_RADIUS, 0, 1000));
        Assert.assertEquals(urlBotLeft, vkLoader.getRequestUrl(VKLoader.API_PLACES_URL, VKLoader.ACCESS_TOKEN, geoArea.getBottomLeftPoint(), VKLoader.CORNER_RADIUS, 0, 1000));
        Assert.assertEquals(urlBotRight, vkLoader.getRequestUrl(VKLoader.API_PLACES_URL, VKLoader.ACCESS_TOKEN, geoArea.getBottomRightPoint(), VKLoader.CORNER_RADIUS, 0, 1000));
    }

    private List<GeoArea> getEthalonAreas() {
        GeoArea geoArea = new GeoArea(upLeftPoint,upRightPoint, bottomLeftPoint, bottomRightPoint);
        geoArea.setCenter(centerEthalon);

        List<GeoArea> geoAreas = new ArrayList<>();
        DistanceResolver distanceResolver = new DistanceResolver();
        geoAreas.add(new GeoArea(geoArea.getUpLeftPoint(), geoArea.getUpRightPoint()
                ,distanceResolver.getMiddlePoint(geoArea.getUpLeftPoint(), geoArea.getBottomLeftPoint())
                ,distanceResolver.getMiddlePoint(geoArea.getUpRightPoint(), geoArea.getBottomRightPoint())
        ));
        geoAreas.add(new GeoArea(distanceResolver.getMiddlePoint(geoArea.getUpLeftPoint(), geoArea.getBottomLeftPoint())
                ,distanceResolver.getMiddlePoint(geoArea.getUpRightPoint(), geoArea.getBottomRightPoint())
                ,geoArea.getBottomLeftPoint(), geoArea.getBottomRightPoint()
        ));
        for (GeoArea area : geoAreas) {
            area.setCenter(distanceResolver.getCentralGeoPoint(Arrays.asList(area.getUpLeftPoint(), area.getUpRightPoint()
                    ,area.getBottomLeftPoint(), area.getBottomRightPoint())));
        }

        return geoAreas;
    }

}
