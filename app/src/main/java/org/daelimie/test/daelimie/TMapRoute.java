package org.daelimie.test.daelimie;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YS on 2016-03-20.
 */
public class TMapRoute {
    private String TAG = "TMapRoute";
    private JSONObject responseData;

    public static TMapRoute mTMapRoute = new TMapRoute();

    private TMapRoute() {}

    public JSONObject searchRoute(String appKey, String departureName, LatLng departureLocate, String destinationName, LatLng destinationLocate) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apis.skplanetx.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMapCom service = retrofit.create(TMapCom.class);

        Call<LinkedHashMap> res = service.routeSearch(
                appKey,
                1,
                departureLocate.longitude,
                departureLocate.latitude,
                destinationLocate.longitude,
                destinationLocate.latitude,
                departureName,
                destinationName,
                "WGS84GEO",
                "WGS84GEO"
        );

        res.enqueue(new Callback<LinkedHashMap>() {

            @Override
            public void onResponse(Call<LinkedHashMap> call, Response<LinkedHashMap> response) {
                Log.d(TAG, "응답 받음");
                try {
                    // 받은 데이터
                    LinkedHashMap temp = response.body();

                    responseData = new JSONObject(temp);
                    String type = responseData.getString("type");
                    Log.d(TAG, type);
                    if (type.equals("FeatureCollection")) {
                        // TODO 종료
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LinkedHashMap> call, Throwable t) {

            }
        });
        return responseData;
    }

    public JSONObject searchRoute(String departure, String destination) {
        return new JSONObject();
    }
}
