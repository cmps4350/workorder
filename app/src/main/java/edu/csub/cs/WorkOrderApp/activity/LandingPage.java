
package edu.csub.cs.WorkOrderApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import edu.csub.cs.WorkOrderApp.R;

import static edu.csub.cs.WorkOrderApp.R.id.btn_landing;
import static edu.csub.cs.WorkOrderApp.R.id.landinglayout;
import static edu.csub.cs.WorkOrderApp.R.id.linearLayout;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.Toast;

public class LandingPage extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btn_landing;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cogs_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.landing_page);
        ImageView cogs = (ImageView) findViewById(R.id.cogs);
        cogs.setBackgroundResource(R.drawable.cogs_animation);
        AnimationDrawable cogsAnimation = (AnimationDrawable) cogs.getBackground();
        cogsAnimation.start();
        Toast.makeText(getApplicationContext(),
                "Starting Facility Maintenance Reporter", Toast.LENGTH_LONG)
                .show();
        btn_landing = (Button) findViewById(R.id.btn_landing);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(LandingPage.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();

        btn_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this,LoginActivity.class);
                startActivity(intent);

            }
        });

    }

}
