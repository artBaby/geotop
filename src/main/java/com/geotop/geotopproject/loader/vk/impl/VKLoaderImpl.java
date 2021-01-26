package com.geotop.geotopproject.loader.vk.impl;

import com.geotop.geotopproject.loader.geosplitter.model.GeoArea;
import com.geotop.geotopproject.loader.geosplitter.model.GeoPoint;
import com.geotop.geotopproject.loader.geosplitter.saintp.SaintPetersburg;
import com.geotop.geotopproject.loader.helper.CollisionResolver;
import com.geotop.geotopproject.loader.helper.SentimentAnalyser;
import com.geotop.geotopproject.loader.vk.VKLoader;
import com.geotop.geotopproject.model.places.APISpecificData;
import com.geotop.geotopproject.model.places.Checkin;
import com.geotop.geotopproject.model.places.Place;
import com.geotop.geotopproject.model.places.api.VkData;
import com.geotop.geotopproject.model.places.deserializer.PlaceDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class VKLoaderImpl implements VKLoader {

    private static final Logger LOG = LoggerFactory.getLogger(VKLoaderImpl.class);

    private CollisionResolver collisionResolver;
    private PlaceDeserializer placeDeserializer;
    private SentimentAnalyser sentimentAnalyser;

    @Autowired
    public VKLoaderImpl(CollisionResolver collisionResolver, PlaceDeserializer placeDeserializerVK, SentimentAnalyser sentimentAnalyser) {
        this.collisionResolver = collisionResolver;
        this.placeDeserializer = placeDeserializerVK;
        this.sentimentAnalyser = sentimentAnalyser;
    }

    @Override
    public List<Place> loadData() throws Exception {
        // load for Saint-P by default
        List<GeoArea> geoAreas = Collections.singletonList(SaintPetersburg.GEO_AREA);
        return loadData(geoAreas);
    }

    @Override
    public List<Place> loadData(List<GeoArea> geoAreas) throws Exception {
        List<Place> places = loadPlaces(geoAreas);
        places = collisionResolver.resolvePlaceCollision(places);

        LOG.info("vk loading done");
        return places;
    }

    public List<Place> loadPlaces(List<GeoArea> geoAreas) throws Exception {
        List<Place> places = new ArrayList<>();
        if (geoAreas == null) {
            throw new IllegalArgumentException("There should be at least one GeoArea");
        }
        for (GeoArea area : geoAreas) {
            places.addAll(loadPlacesByArea(area));
        }
        // delete duplicates produced by merging center and corners areas
        places = collisionResolver.deleteDuplicatePlaces(places, new VkData());

        for (Place place : places) {
            APISpecificData vkData = place.getVkData();
            List<Checkin> checkins = callCheckinsAPI(vkData.getId());
            vkData.setCheckins(checkins);
            try {
                vkData.setRating(Double.valueOf(sentimentAnalyser.ratePlace(checkins)));
            } catch (NullPointerException e) {
                //LOG.error("Rating is null for " + place.getTitle());
            }

            place.setVkData(vkData);
        }

        LOG.info("places loaded");
        return places;
    }

    public List<Place> loadPlacesByArea(GeoArea area) throws Exception {
        List<Place> places = new ArrayList<>();
        GeoPoint center = area.getCenter();
        GeoPoint upLeft = area.getUpLeftPoint();
        GeoPoint upRight = area.getUpRightPoint();
        GeoPoint botLeft = area.getBottomLeftPoint();
        GeoPoint botRight = area.getBottomRightPoint();

        // Call for area's center with huge radius
        places = callPlaceAPI(places, center, CENTER_RADIUS, 0, PLACE_COUNT, PLACE_MAX_OFFSET);
        // Call for area's corners with small radius
        places = callPlaceAPI(places, upLeft, CENTER_RADIUS, 0, PLACE_COUNT, PLACE_MAX_OFFSET);
        places = callPlaceAPI(places, upRight, CENTER_RADIUS, 0, PLACE_COUNT, PLACE_MAX_OFFSET);
        places = callPlaceAPI(places, botLeft, CENTER_RADIUS, 0, PLACE_COUNT, PLACE_MAX_OFFSET);
        places = callPlaceAPI(places, botRight, CENTER_RADIUS, 0, PLACE_COUNT, PLACE_MAX_OFFSET);

        return places;
    }

    public List<Checkin> callCheckinsAPI(String placeId) throws Exception {
        String url = String.format(API_CHECKINS_URL, String.valueOf(placeId), "100", ACCESS_TOKEN);
        String json = IOUtils.toString(new URL(url).openStream())
                .replaceAll("\\{\"response\":","")
                .replaceAll("\\{\"count\".*\"items\":","")
                .replaceAll("\\}\\}$","");
        Thread.sleep(300);
        //LOG.info("Checkins loaded for " + placeId);
        Type listType = new TypeToken<ArrayList<Checkin>>(){}.getType();
        try {
            return new Gson().fromJson(json, listType);
        } catch (Exception e){
            LOG.error("place " + placeId + ": " + e.getMessage());
            return null;
        }
    }

    public List<Place> callPlaceAPI(List<Place> places, GeoPoint point, int radius, int offset, int count, int maxOffset) throws Exception {
        String url = getRequestUrl(API_PLACES_URL, ACCESS_TOKEN, point, radius, 0, count);
        LOG.info(url);
        String json = IOUtils.toString(new URL(url).openStream())
                .replaceAll("\\{\"response\":","")
                .replaceAll("\\{\"count\".*\"items\":","")
                .replaceAll("\\]\\}\\}$",",");
        Thread.sleep(500);
        if(json.endsWith(",")) {
            json = json.substring(0, json.length() - 1) + "]";
        }
        LOG.info(json);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Place.class, placeDeserializer);
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<ArrayList<Place>>(){}.getType();
        List<Place> placeList = gson.fromJson(json, listType);
        places.addAll(placeList);
        if (offset == maxOffset) {
            LOG.info("exit from rec");
            return places;
        }
        LOG.info("rec");
        return callPlaceAPI(places, point, radius, offset + count, count, maxOffset);
    }

    public String getRequestUrl(String baseUrl, String token, GeoPoint point, int radius, int offset, int count) {
        String lat = String.valueOf(point.getLatitude());
        String lon = String.valueOf(point.getLongitude());
        return String.format(baseUrl, lat, lon, radius, offset, count, token);
    }
}
