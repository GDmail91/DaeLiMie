package org.daelimie.test.daelimie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by YS on 2016-03-09.
 */
public class AddAlram extends AppCompatActivity {

    // 선택된 지역 정보
    private LatLng departureLocate; // 출발지 위도,경도
    private String departurePlaceId; // 출발지 지역 정보
    private String departureName; // 출발지 이름
    private LatLng destinationLocate; //도착지 위도, 경도
    private String destinationPlaceId; //도착지 지역 정보
    private String destinationName; // 도착지 이름
    private String arrivalTime; // 도착하고 싶은 시간

    // 뷰
    private TextView departureNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alram);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        departureNameView = (TextView) findViewById(R.id.departureNameView);

        // 클릭키를 추가할 버튼정의
        Button setDeparture = (Button) findViewById(R.id.setDeparture);
        setDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAlram.this, LocatePicker.class);
                intent.putExtra("BUTTON_FLAG", "DEPARTURE");
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK) {
            switch (intent.getStringExtra("BUTTON_FLAG")) {
                case "DEPARTURE":
                    departureLocate = new LatLng(intent.getDoubleExtra("selectedLocateLat", 0.0), intent.getDoubleExtra("selectedLocateLng", 0.0));
                    departurePlaceId = intent.getStringExtra("selectedPlaceId");
                    departureName = intent.getStringExtra("selectedName");
                    departureNameView.setText(departureName);
            }
        }
    }
}
