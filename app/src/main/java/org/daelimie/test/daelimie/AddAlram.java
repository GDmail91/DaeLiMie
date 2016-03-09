package org.daelimie.test.daelimie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by YS on 2016-03-09.
 */
public class AddAlram extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alram);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 클릭키를 추가할 버튼정의
        //Button addBtn = (Button) findViewById(R.id.addRouteButton);
        /*addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 경로 추가 버튼
            }
        });
*/
    }
}
