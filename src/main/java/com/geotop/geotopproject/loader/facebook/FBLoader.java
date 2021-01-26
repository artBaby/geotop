package com.geotop.geotopproject.loader.facebook;

import com.geotop.geotopproject.loader.CommonLoader;
import org.springframework.stereotype.Component;

public interface FBLoader extends CommonLoader {
    String ACCESS_TOKEN = "EAAHppxZAPpFEBAD5pZAeSfPFX50IpGtDxtUVZAJnVhOyeZBf2QbaNVQjpavIUtK3diwbjam7nGJYzKFNLUixw05GpYHNzU31gxgtM35YrXkSObLCKgmnFwXizzT2wwov3bEcJ0MHtwB8qxkrR8CPHbIZA114ZACBLCae7mkn5vFgZDZD";
    String COORDINATES = "59.935634,30.325935";

    String API_BASE = "https://graph.facebook.com/v3.0";
    String API_PLACES_URL = API_BASE +
            "/search" +
            "?type=place" +
            "&center=%s" +
            "&distance=15000" +
            "&limit=100" +
            "&fields=name,link,location,checkins,rating_count,overall_star_rating,category,picture.type(large){url}" +
            "&access_token=%s";

}