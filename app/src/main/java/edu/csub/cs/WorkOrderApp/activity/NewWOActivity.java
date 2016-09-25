package edu.csub.cs.WorkOrderApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.csub.cs.WorkOrderApp.R;

/**
 * Created on 9/24/2016.
 */
public class NewWOActivity extends Activity{

    public static final CharSequence[] BUILDING_OPTIONS  = {"Building A", "Building B", "Building C", "Building D", "Building E", "Building F", "Building G"};
    public static final CharSequence[] AREA_OPTIONS  = {"Track Area", "Pool Area", "Gym", "Locker Area", "Bathroom Area", "Weight", "Room 1", "Room 2"};
    public static final CharSequence[] EQUIPMENT_OPTIONS  = {"Treadmill", "Calf Machine", "Squat Rack", "Pull Up Bar", "Leg Curl Machine", "Leg Press Machine", "Incline"};
    public static final CharSequence[] PRIORITY_OPTIONS  = {"Low", "Medium", "High", "Critical", "Recurring"};
    public static final CharSequence[] PROBLEM_OPTIONS  = {"Broken Equiments", "Electrical Failure", "Plumbing", "Cosmetic Damages", "Others"};


    String image_name;
    static final int CAM_REQUEST = 1;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newwo);
        imageView = (ImageView)findViewById(R.id.iv_camera);

        // setting up spinners
        Spinner building = (Spinner) findViewById(R.id.spinner_building);
        Spinner area = (Spinner) findViewById(R.id.spinner_area);
        Spinner equipment = (Spinner) findViewById(R.id.spinner_equipment);
        Spinner priority = (Spinner) findViewById(R.id.spinner_priority);
        Spinner problem = (Spinner) findViewById(R.id.spinner_problem);


        // setting up adapters
        // Building
        ArrayAdapter<CharSequence> dataAdapter1 = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, BUILDING_OPTIONS);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        building.setAdapter(dataAdapter1);

        // Area
        ArrayAdapter<CharSequence> dataAdapter2 = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, AREA_OPTIONS);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area.setAdapter(dataAdapter2);

        // Equipment
        ArrayAdapter<CharSequence> dataAdapter3 = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_dropdown_item, EQUIPMENT_OPTIONS);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipment.setAdapter(dataAdapter3);

        // Priority
        ArrayAdapter<CharSequence> dataAdapter4 = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, PRIORITY_OPTIONS);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(dataAdapter4);

        // Problem
        ArrayAdapter<CharSequence> dataAdapter5 = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, PROBLEM_OPTIONS);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        problem.setAdapter(dataAdapter5);

    }


    private File getFile() throws ParseException {
        File folder = new File("sdcard/workorder_app");
        if (!folder.exists()) {
            folder.mkdir();
        }

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        image_name = "image_"+formattedDate+".jpg";
        File image_file = new File(folder,image_name);
        return image_file;
    }

    public void getWorkOrderImage(View view) throws ParseException {
        //Toast.makeText(NewWOActivity.this, "Test Click on Image", Toast.LENGTH_SHORT).show();
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getFile();
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_intent,CAM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 || resultCode == 0) {
            imageView.setImageResource(R.drawable.camera);
        } else {
            imageView.getLayoutParams().height = 350;
            imageView.getLayoutParams().width = 350;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String image_path = "sdcard/workorder_app/" + image_name;
            imageView.setImageDrawable(Drawable.createFromPath(image_path));
            Toast.makeText(NewWOActivity.this, image_path, Toast.LENGTH_SHORT).show();
        }
    }
}
