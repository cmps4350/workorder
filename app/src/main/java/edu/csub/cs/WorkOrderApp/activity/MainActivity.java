/**
 http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/
 * */
package edu.csub.cs.WorkOrderApp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.csub.cs.WorkOrderApp.R;
import edu.csub.cs.WorkOrderApp.helper.SQLiteHandler;
import edu.csub.cs.WorkOrderApp.helper.SessionManager;

public class MainActivity extends AppCompatActivity {

	// Testing push on a mac

	private TextView txtName;
	private Button btnLogout;
    private Button btnNewWorkorder;
	private Button btnCompleteOrder;
	public String eid;
	private SQLiteHandler db;
	private SessionManager session;
	private ProgressDialog pDialog;
	//public WorkOrderHolder [] workorderlist;
	public static List<WorkOrderHolder> workorderlist = new ArrayList<WorkOrderHolder>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setLogo(R.drawable.cogs_icon);
		getSupportActionBar().setDisplayUseLogoEnabled(true);
		setContentView(R.layout.activity_main);

		txtName = (TextView) findViewById(R.id.name);
		btnLogout = (Button) findViewById(R.id.btnLogout);
        btnNewWorkorder = (Button) findViewById(R.id.btnNewWorkOrder);
		btnCompleteOrder = (Button) findViewById(R.id.btnViewWorkOrder);
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("name");
		eid = user.get("uid");

		// Displaying the user details on the screen
		txtName.setText(name);

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
		btnNewWorkorder.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),
                            NewWOActivity.class);
                    startActivity(i);
                    //finish();
                }
            });

		//Mark Order Complete
		btnCompleteOrder.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				//get_equipment(URL_VIEW,workorderlist);
				Intent i = new Intent(getApplicationContext(),
						OrderComplete.class);
				startActivity(i);
				//finish();
			}
		});
	}


	private void get_equipment(String url, final List<WorkOrderHolder> list) {
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
					final String[] name = new String[jObj.length()];
					final int[] id = new int[jObj.length()];
					final String[] areas = new String[jObj.length()];
					final String[] equipments = new String[jObj.length()];
					final String[] dates = new String[jObj.length()];
					final String[] status = new String[jObj.length()];
					final String[] priority = new String[jObj.length()];
					for(int i=0;i<jObj.length(); i++){
						json = jObj.getJSONObject(i);
						equipments[i] = json.getString("equipment_name");
						dates[i] = json.getString("created_date");
						areas[i] = json.getString("room_name");
						id[i] = json.getInt("id");
						status[i] = json.getString("status_name");
						priority[i] = json.getString("priority_name");
					}
					if (workorderlist.isEmpty()) {
						for (int i = 0; i < name.length; i++) {
							list.add(new WorkOrderHolder(id[i], areas[i], equipments[i], status[i], priority[i], dates[i] ));
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
				Toast.makeText(MainActivity.this, "Failure getting data from server", Toast.LENGTH_SHORT).show();
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

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}


}
