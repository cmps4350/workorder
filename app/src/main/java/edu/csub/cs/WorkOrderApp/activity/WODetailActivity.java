package edu.csub.cs.WorkOrderApp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.csub.cs.WorkOrderApp.R;

import static edu.csub.cs.WorkOrderApp.app.AppConfig.URL_SET_COMPLETE;

/**
 * Created by Ponism on 11/16/2016.
 */

public class WODetailActivity extends AppCompatActivity {
    private TextView wo_no, created_on, created_by, wo_area, wo_equipment, wo_status, wo_priority, wo_description;
    private Button back, mark_complete;
    private ProgressDialog pDialog;
    private int workorder_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cogs_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.workorder_detail);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // get passed data from previous intent
        final WorkOrderHolder data = (WorkOrderHolder)getIntent().getSerializableExtra("Info");

        back = (Button)findViewById(R.id.btn_back);
        mark_complete = (Button)findViewById(R.id.btn_complete);

        wo_no = (TextView)findViewById(R.id.wo_no);
        created_on = (TextView)findViewById(R.id.created_on);
        created_by = (TextView)findViewById(R.id.created_by);
        wo_area = (TextView)findViewById(R.id.wo_area);
        wo_equipment = (TextView)findViewById(R.id.wo_equipment);
        wo_status = (TextView)findViewById(R.id.wo_status);
        wo_priority = (TextView)findViewById(R.id.wo_priority);
        wo_description = (TextView)findViewById(R.id.wo_description);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        mark_complete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WODetailActivity.this);

                builder.setTitle("Confirmation");
                builder.setMessage("Mark #00"+data.getId()+" as \"Completed\"?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mark_complete(URL_SET_COMPLETE,data.getId());

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        wo_no.setText("Work Order #00"+data.getId());
        created_on.setText(data.getCreateDate());
        created_by.setText(data.getEmp());
        wo_area.setText(data.getArea());
        wo_equipment.setText(data.getEquipment());
        wo_status.setText(data.getStatus());
        wo_priority.setText(data.getPriority());
        wo_description.setText(data.getDescription());




    }


    private void mark_complete(String url, final String workorder_id) {
        pDialog.setMessage("Loading data ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(WODetailActivity.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),
                                OrderComplete.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(WODetailActivity.this, "Failed performing action.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error

                }
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(WODetailActivity.this, "Failure getting data from server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("verify", "4350");
                map.put("workorder_id", workorder_id);

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
