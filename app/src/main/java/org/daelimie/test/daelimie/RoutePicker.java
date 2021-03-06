package org.daelimie.test.daelimie;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YS on 2016-03-19.
 */
public class RoutePicker extends AppCompatActivity {

    private static final String TAG = "RoutePicker";

    TextView top_route_name;
    TextView top_route_address;

    static final LatLng SEOUL = new LatLng(37.56, 126.97);
    private GoogleMap map;
    private Marker marker;
    private Boolean isSetMarker = false;

    // 선택된 경로 정보
    private LatLng departureLocate; // 출발지 위도,경도
    private String departurePlaceId; // 출발지 지역 정보
    private String departureName; // 출발지 이름
    private LatLng destinationLocate; //도착지 위도, 경도
    private String destinationPlaceId; //도착지 지역 정보
    private String destinationName; // 도착지 이름
    private String arrivalTime; // 도착하고 싶은 시간

    private SlidingUpPanelLayout mLayout;

    // 구글 플레이스 관련
    private int PLACE_PICKER_REQUEST = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_picker);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Double departureLocateLat = bundle.getDouble("departureLocateLat");
        Double departureLocateLng = bundle.getDouble("departureLocateLng");
        departureLocate = new LatLng(departureLocateLat, departureLocateLng);
        departurePlaceId = bundle.getString("departurePlaceId");
        departureName = bundle.getString("departureName");
        Double destinationLocateLat = bundle.getDouble("destinationLocateLat");
        Double destinationLocateLng = bundle.getDouble("destinationLocateLng");
        destinationLocate = new LatLng(destinationLocateLat, destinationLocateLng);
        destinationPlaceId = bundle.getString("destinationPlaceId");
        destinationName = bundle.getString("destinationName");
        //arrivalTime;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initGoogleMap();


        top_route_name = (TextView) findViewById(R.id.top_route_name);
        top_route_address = (TextView) findViewById(R.id.top_route_address);
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openAutocompleteActivity();
            }
        });

        Button addRouteButton = (Button) findViewById(R.id.addRouteButton);
        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /**************
         * 리사이클러 뷰
         **************/
        try {
            String testData = "[{ locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }, { locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }, { locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }" +
                    ",{ locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }, { locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }, { locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }" +
                    ",{ locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }, { locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }, { locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }" +
                    ",{ locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }, { locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }, { locate_name: '럭키아파트', locate_address: '서울시 금천구 시흥대로 47길' }]";
            JSONArray data = new JSONArray(testData);
            Log.d("JSON: ", data.toString());
            ArrayList<String> tmp_locate_name = new ArrayList<String>();
            ArrayList<String> tmp_locate_address = new ArrayList<String>();

            top_route_name.setText(data.getJSONObject(0).getString("locate_name"));
            top_route_address.setText(data.getJSONObject(0).getString("locate_address"));

            for (int i = 1; i < data.length(); i++) {
                // List 어댑터에 전달할 값들
                tmp_locate_name.add(data.getJSONObject(i).getString("locate_name"));
                tmp_locate_address.add(data.getJSONObject(i).getString("locate_address"));
            }

            // 가장 상위(도로) 다음 장소 item 셋팅
            top_route_name.setText(data.getJSONObject(0).getString("locate_name"));
            top_route_address.setText(data.getJSONObject(0).getString("locate_address"));

            // ListView 생성하면서 작성할 값 초기화
            LocateAdapter m_ListAdapter = new LocateAdapter(tmp_locate_name, tmp_locate_address);

            // ListView 어댑터 연결
            ListView m_ListView = (ListView) findViewById(R.id.route_list);
            m_ListView.setAdapter(m_ListAdapter);

            m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(RoutePicker.this, "onItemClick", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Sliding panel
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if (slideOffset >= 0) {
                    mLayout.bringToFront();
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (newState.toString().equals("EXPANDED")) {
                    ImageView pannel_arrow = (ImageView) findViewById(R.id.pannel_arrow);
                    pannel_arrow.setImageResource(R.drawable.down);
                } else if (newState.toString().equals("COLLAPSED")) {
                    LinearLayout searchLayout = (LinearLayout) findViewById(R.id.search_bar);
                    searchLayout.bringToFront();
                    ImageView pannel_arrow = (ImageView) findViewById(R.id.pannel_arrow);
                    pannel_arrow.setImageResource(R.drawable.up);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // 구글맵 초기화
    private void initGoogleMap() {

        /*******************
         * 구글맵 컴포넌트
         *******************/
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.route_map))
                .getMap(); // 맵 가져옴

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(departureLocate, 15)); // Zoom 단계 설정
        route_info(departureLocate, destinationLocate, departurePlaceId, destinationPlaceId);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (isSetMarker) {
                    marker.remove();
                }
                marker = map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(latLng));
                isSetMarker = true;

                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));   // 마커생성위치로 이동
                marker.showInfoWindow();
            }
        });
    }

    public void route_info(LatLng departureLocate, LatLng destinationLocate, String departurePlaceId, String destinationPlaceId) {
        Log.d(TAG, departurePlaceId + " / " + destinationPlaceId);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleMapsCom service = retrofit.create(GoogleMapsCom.class);

        Call<LinkedHashMap> res = service.getDirections(
                getString(R.string.WEB_API_KEY),
                "place_id:" + departurePlaceId,
                "place_id:" + destinationPlaceId,
                "transit",
                "true",
                "ko",
                "fewer_transfers",
                "subway|bus|train",
                "now");

        res.enqueue(new Callback<LinkedHashMap>() {
            @Override
            public void onResponse(Call<LinkedHashMap> call, Response<LinkedHashMap> response) {
                Log.d(TAG, "응답 받음");
                try {
                    // 받은 데이터
                    LinkedHashMap temp = response.body();

                    JSONObject responseData = new JSONObject(temp);
                    Log.d(TAG, responseData.toString());
                    String status = responseData.getString("status");
                    Log.d(TAG, status);
                    if (status.equals("OK")) {
                        JSONArray routes = responseData.getJSONArray("routes");
                        Log.d(TAG, routes.toString());
                        ArrayList<JSONObject> items = new ArrayList<JSONObject>();
                        for (int i = 0; i < routes.length(); i++) {
                            items.add(routes.getJSONObject(i)); // 가장 상위(도로) 다음 장소 가져옴
                        }
                        JSONArray steps = items.get(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

                        // 지도에 그릴 Polyline
                        PolylineOptions polylineOptions = new PolylineOptions();

                        for (int i = 0; i < steps.length(); i++) {
                            // 교통편 확인
                            switch (steps.getJSONObject(i).getString("travel_mode")) {
                                case "WALKING": // 걸어갈 경우
                                    // 도보 길안내 (Google Map 은 길안내가 자세히 안나와있으므로 T Map 이용)
                                    JSONArray walkStep = steps.getJSONObject(i).getJSONArray("steps");
                                    for (int j = 0; j < walkStep.length(); j++) {
                                        List<LatLng> poly = PolyUtil.decode(walkStep.getJSONObject(j).getJSONObject("polyline").getString("points"));
                                        Log.d(TAG, poly.toString());

                                        // T Map 에서 도보 길찾기 정보 가져옴
                                        TMapRoute.mTMapRoute.searchRoute(
                                                getString(R.string.T_API_KEY),
                                                "도보로 걷기", // 시작위치 이름
                                                poly.get(0),
                                                steps.getJSONObject(i).getString("html_instructions"), // 목적지 위치 이름
                                                poly.get(1),
                                                new MyCallback() { // Data 콜백
                                                    @Override
                                                    public void httpProcessing(JSONObject result) {
                                                        markingMap(result); // 지도에 마크하기
                                                    }
                                                });
                                    }
                                    break;
                                case "TRANSIT": // 교통수단 탈 경우
                                    JSONObject detailTransit = steps.getJSONObject(i).getJSONObject("transit_details");
                                    // 교통수단 길안내
                                    List<LatLng> tranPoly = PolyUtil.decode(steps.getJSONObject(i).getJSONObject("polyline").getString("points"));
                                    for (int j=0; j<tranPoly.size(); j++) {
                                        Log.d(TAG, tranPoly.toString());
                                        polylineOptions.add(tranPoly.get(j))
                                                .width(25)
                                                .color(Color.BLUE);
                                    }

                                    switch (detailTransit.getJSONObject("line").getJSONObject("vehicle").getString("type")) {
                                        case "BUS": // 버스인 경우
                                            // TODO 버스
                                            break;
                                        case "SUBWAY": // 지하철인 경우
                                            // TODO 지하철
                                            break;
                                    }
                                    break;
                            }
                        }

                        Polyline polyline = map.addPolyline(polylineOptions);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LinkedHashMap> call, Throwable t) {

                // TODO 실패
                Log.d(TAG, t.toString());
                Log.d(TAG, "아예실패");
            }
        });
    }

    // 지도에 마크하기
    protected void markingMap(JSONObject result) {
        try {
            JSONObject tmapData = result;
            JSONArray features = tmapData.getJSONArray("features");

            for (int k = 0; k < features.length(); k++) {
                int index = features.getJSONObject(k).getJSONObject("properties").getInt("index");
                JSONObject geometry = features.getJSONObject(k).getJSONObject("geometry");
                JSONObject properties = features.getJSONObject(k).getJSONObject("properties");
                switch (geometry.getString("type")) {
                    case "Point":
                        // TODO 마커찍기
                        JSONArray markLocation = geometry.getJSONArray("coordinates");
                        LatLng mark = new LatLng(markLocation.getDouble(1), markLocation.getDouble(0)); // 위도 경도가 반대로 옴
                        Log.d(TAG, mark.toString());
                        try {
                            marker = map.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                    .position(mark)
                                    .title(URLDecoder.decode(properties.getString("name"), "UTF-8"))
                                    .snippet(URLDecoder.decode(properties.getString("description"), "UTF-8")));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        isSetMarker = true;
                        marker.showInfoWindow();
                        break;
                    case "LineString":
                        // TODO 라인그리기
                        ArrayList<LatLng> walkingPoly = new ArrayList<LatLng>();
                        JSONArray polyLocation = geometry.getJSONArray("coordinates");
                        for (int l=0; l<polyLocation.length(); l++) {
                            double walkLat = polyLocation.getJSONArray(l).getDouble(1);
                            double walkLng = polyLocation.getJSONArray(l).getDouble(0);
                            walkingPoly.add(new LatLng(walkLat, walkLng));
                        }

                        // 지도에 그릴 Polyline
                        PolylineOptions polylineOptions = new PolylineOptions();
                        for (int l=0; l<walkingPoly.size(); l++) {
                            polylineOptions.add(walkingPoly.get(l))
                                    .width(25)
                                    .color(Color.RED);
                        }

                        map.addPolyline(polylineOptions);

                        break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /******************
     * 구글플레이스 컴포넌트
     ******************/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RoutePicker Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.daelimie.test.daelimie/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RoutePicker Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.daelimie.test.daelimie/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
