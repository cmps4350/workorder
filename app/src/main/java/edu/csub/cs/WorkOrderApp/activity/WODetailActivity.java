package edu.csub.cs.WorkOrderApp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.csub.cs.WorkOrderApp.R;

/**
 * Created by Ponism on 11/16/2016.
 */

public class WODetailActivity extends AppCompatActivity {
    private TextView wo_no, created_on, created_by, wo_area, wo_equipment, wo_status, wo_priority;
    private Button back, mark_complete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cogs_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.workorder_detail);

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
                        // Do nothing but close the dialog

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





    }


}
