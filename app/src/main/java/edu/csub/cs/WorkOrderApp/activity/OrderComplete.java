package edu.csub.cs.WorkOrderApp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.csub.cs.WorkOrderApp.R;
import edu.csub.cs.WorkOrderApp.activity.FakeDB.OrderDBHelper;

import static edu.csub.cs.WorkOrderApp.app.AppConfig.URL_VIEW;

public class OrderComplete extends AppCompatActivity {

    private static final String TAG = "Order Activity";
    private OrderDBHelper mHelper;
    private ListView mOrderList;
    private ArrayAdapter<String> mAdapter;
    private ProgressDialog pDialog;
    public static List<WorkOrderHolder> workorderlist = new ArrayList<WorkOrderHolder>();
    private WOFeedListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cogs_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_order_complete);

        mHelper = new OrderDBHelper(this);
        mOrderList = (ListView) findViewById(R.id.list_todo);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        listAdapter = new WOFeedListAdapter(this, workorderlist);
        mOrderList.setAdapter(listAdapter);
        get_workorder(URL_VIEW,workorderlist);

        mOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                // get data information and store inside an object to pass
                WorkOrderHolder s = (WorkOrderHolder) arg0.getItemAtPosition(position);

                // new intent
                Intent i = new Intent(OrderComplete.this, WODetailActivity.class);

                // adding the object to be pass to new intent
                i.putExtra("Info",(Serializable) s);

                // new intent
                startActivity(i);
                //Toast.makeText(OrderComplete.this, "" + s.getArea(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void get_workorder(String url, final List<WorkOrderHolder> list) {
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
                    //workorderlist = new WorkOrderHolder[jObj.length()];
                    JSONObject json= null;

                    // creating temporary string arrays to store data from server
                    final String[] name = new String[jObj.length()];
                    final int[] id = new int[jObj.length()];
                    final String[] areas = new String[jObj.length()];
                    final String[] equipments = new String[jObj.length()];
                    final String[] dates = new String[jObj.length()];
                    final String[] status = new String[jObj.length()];
                    final String[] priority = new String[jObj.length()];
                    // storing data from server
                    for(int i=0;i<jObj.length(); i++){
                        json = jObj.getJSONObject(i);
                        equipments[i] = json.getString("equipment_name");
                        dates[i] = json.getString("created_date");
                        areas[i] = json.getString("room_name");
                        id[i] = json.getInt("id");
                        status[i] = json.getString("status_name");
                        priority[i] = json.getString("priority_name");
                    }

                    // populating the array list
                    if (workorderlist.isEmpty()) {
                        for (int i = 0; i < name.length; i++) {
                            list.add(new WorkOrderHolder(id[i], areas[i], equipments[i], status[i], priority[i], dates[i] ));
                        }
                    }

                    // refresh adapter after data is populated
                    listAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(OrderComplete.this, "Failure getting data from server", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
