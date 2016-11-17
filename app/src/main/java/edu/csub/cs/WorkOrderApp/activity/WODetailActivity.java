package edu.csub.cs.WorkOrderApp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import edu.csub.cs.WorkOrderApp.R;

/**
 * Created by Ponism on 11/16/2016.
 */

public class WODetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cogs_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.workorder_detail);

        // get passed data from previous intent
        WorkOrderHolder data = (WorkOrderHolder)getIntent().getSerializableExtra("Info");
        Toast.makeText(this, ""+data.getArea(), Toast.LENGTH_SHORT).show();
    }
}
