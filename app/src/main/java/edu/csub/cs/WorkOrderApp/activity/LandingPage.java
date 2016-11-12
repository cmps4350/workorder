
package edu.csub.cs.WorkOrderApp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.csub.cs.WorkOrderApp.R;

import static edu.csub.cs.WorkOrderApp.app.AppConfig.URL_EQUIPMENT;
import static edu.csub.cs.WorkOrderApp.app.AppConfig.URL_PRIORITY;
import static edu.csub.cs.WorkOrderApp.app.AppConfig.URL_ROOM;
import static edu.csub.cs.WorkOrderApp.app.AppConfig.URL_TYPE;


public class LandingPage extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btn_landing;
    private ProgressDialog pDialog;
    public EquipmentHolder[] equipments;
    public static List<String> equipment = new ArrayList<String>();
    public static List<EquipmentHolder> equipment2 = new ArrayList<EquipmentHolder>();
    public static List<String> priority = new ArrayList<String>();
    public static List<String> room = new ArrayList<String>();
    public static List<String> type = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cogs_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.landing_page);
        ImageView cogs = (ImageView) findViewById(R.id.cogs);
        cogs.setBackgroundResource(R.drawable.cogs_animation);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        AnimationDrawable cogsAnimation = (AnimationDrawable) cogs.getBackground();
        cogsAnimation.start();
        Toast.makeText(getApplicationContext(),
                "Starting Facility Maintenance Reporter", Toast.LENGTH_LONG)
                .show();
        btn_landing = (Button) findViewById(R.id.btn_landing);
        //equipment2.add(new EquipmentHolder(25,"Testing"));
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

        get_data(URL_PRIORITY,priority);
        get_data(URL_ROOM,room);
        get_data(URL_TYPE,type);
        get_equipment(URL_EQUIPMENT, equipment2);
    }

    private void get_data(String url, final List list) {
        pDialog.setMessage("Loading data ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONArray jObj = new JSONArray(response);
                    JSONObject json= null;
                    final String[] name = new String[jObj.length()];
                    for(int i=0;i<jObj.length(); i++){
                        json = jObj.getJSONObject(i);
                        name[i] = json.getString("name");
                    }
                    if (list.isEmpty()) {
                        for (int i = 0; i < name.length; i++) {
                            list.add(name[i]);
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(LandingPage.this, "Failure getting data from server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("verify", "4350");

                return map;
            }
        };
        requestQueue.add(strReq);

    }


    private void get_equipment(String url, final List<EquipmentHolder> list) {
        pDialog.setMessage("Loading data ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONArray jObj = new JSONArray(response);
                    equipments = new EquipmentHolder[jObj.length()];
                    JSONObject json= null;
                    final String[] name = new String[jObj.length()];
                    final int[] id = new int[jObj.length()];
                    final int[] room_id = new int[jObj.length()];
                    for(int i=0;i<jObj.length(); i++){
                        json = jObj.getJSONObject(i);
                        name[i] = json.getString("name");
                        id[i] = json.getInt("id");
                        room_id[i] = json.getInt("room_id");

                    }
                    if (equipment2.isEmpty()) {
                        for (int i = 0; i < name.length; i++) {
                            list.add(new EquipmentHolder(id[i], room_id[i],  name[i] ));
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(LandingPage.this, "Failure getting data from server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("verify", "4350");

                return map;
            }
        };
        requestQueue.add(strReq);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
