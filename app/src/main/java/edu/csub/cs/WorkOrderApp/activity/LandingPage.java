/**
 * Created by Jonathan Dinh on 9/24/2016.
 */

package edu.csub.cs.WorkOrderApp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

//comment
public class LandingPage extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btn_landing;
    private ProgressDialog pDialog;

    public static List<String> equipment = new ArrayList<String>();
    public static List<String> priority = new ArrayList<String>();
    public static List<String> room = new ArrayList<String>();
    public static List<String> type = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btn_landing = (Button) findViewById(R.id.btn_landing);

        btn_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        get_data(URL_PRIORITY,priority);
        get_data(URL_ROOM,room);
        get_data(URL_EQUIPMENT,equipment);
        get_data(URL_TYPE,type);
    }

    private void get_data(String url, final List list) {
        pDialog.setMessage("Adding New Work Order ...");
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


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
