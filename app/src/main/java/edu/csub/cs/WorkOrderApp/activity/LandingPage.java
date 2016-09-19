
package edu.csub.cs.WorkOrderApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.csub.cs.WorkOrderApp.R;

public class LandingPage extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btn_landing;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
        btn_landing = (Button) findViewById(R.id.btn_landing);

        btn_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
