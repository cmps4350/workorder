package edu.csub.cs.WorkOrderApp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.csub.cs.WorkOrderApp.R;
import edu.csub.cs.WorkOrderApp.app.AppConfig;

import static edu.csub.cs.WorkOrderApp.R.drawable.camera;
import static edu.csub.cs.WorkOrderApp.R.drawable.plus;

/**
 * Created on 9/24/2016.
 */
public class NewWOActivity extends Activity{

    // dummy data
    public static final CharSequence[] BUILDING_OPTIONS  = {"Building A", "Building B", "Building C", "Building D", "Building E", "Building F", "Building G"};
    public static final CharSequence[] AREA_OPTIONS  = {"Track Area", "Pool Area", "Gym", "Locker Area", "Bathroom Area", "Weight", "Room 1", "Room 2"};
    public static final CharSequence[] EQUIPMENT_OPTIONS  = {"Treadmill", "Calf Machine", "Squat Rack", "Pull Up Bar", "Leg Curl Machine", "Leg Press Machine", "Incline"};
    public static final CharSequence[] PRIORITY_OPTIONS  = {"Low", "Medium", "High", "Critical", "Recurring"};
    public static final CharSequence[] PROBLEM_OPTIONS  = {"Broken Equiments", "Electrical Failure", "Plumbing", "Cosmetic Damages", "Others"};


    private Uri file_uri;
    private Bitmap bitmap;
    private static String[] encode_string;
    private ProgressDialog pDialog;
    private static String[] image_name;
    private static final int CAM_REQUEST = 1;
    private ImageView imageView1,imageView2,imageView3;
    private Button btn_submit;
    private static final int MY_REQUEST_CODE = 1;
    private int img_count = 0;
    private int img_pos = 0;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newwo);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        imageView1 = (ImageView)findViewById(R.id.iv_camera1);
        imageView2 = (ImageView)findViewById(R.id.iv_camera2);
        imageView3 = (ImageView)findViewById(R.id.iv_camera3);
        encode_string = new String[3];
        image_name = new String[3];

        // setting up spinners
        Spinner building = (Spinner) findViewById(R.id.spinner_building);
        Spinner area = (Spinner) findViewById(R.id.spinner_area);
        Spinner equipment = (Spinner) findViewById(R.id.spinner_equipment);
        Spinner priority = (Spinner) findViewById(R.id.spinner_priority);
        Spinner problem = (Spinner) findViewById(R.id.spinner_problem);
        btn_submit = (Button) findViewById(R.id.submit_wo);

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

        btn_submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                insert_to_database();
            }

        });


        imageView1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    img_pos = getImgPos(1);
                    getWorkOrderImage(view, 1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        imageView2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    img_pos = getImgPos(2);
                    getWorkOrderImage(view, 2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        imageView3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    img_pos = getImgPos(3);
                    getWorkOrderImage(view, 3);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        imageView1.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                imageView1.setTag(plus);
                Object tag2 = imageView1.getTag();

                if (imageView1.getDrawable() != null ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewWOActivity.this);
                    builder.setMessage("Delete the image?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // to remove image
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;
                }
                return false;
            }
        });

        imageView2.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                imageView2.setTag(camera);
                Object tag = imageView2.getTag();
                imageView2.setTag(plus);
                Object tag2 = imageView2.getTag();
                int camera = R.drawable.camera;
                int plus = R.drawable.plus;

                if (imageView2.getDrawable() != null && ((Integer)tag).intValue() != camera &&
                        ((Integer)tag2).intValue() != plus) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewWOActivity.this);
                    builder.setMessage("Delete the image?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // to remove image
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;
                }
                return false;
            }

        });

        imageView3.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                imageView3.setTag(plus);
                Object tag2 = imageView3.getTag();

                if (imageView3.getDrawable() != null ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewWOActivity.this);
                    builder.setMessage("Delete the image?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // to remove image
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;
                }
                return false;
            }
        });
    }

    private int getImgPos(int a) {
        if (img_count == 0 && a >= 1 && a <=3)
            return 1;
        if (img_count == 1 && a == 1)
            return 1;
        if (img_count == 1 && (a == 2 || a ==3))
            return 2;
        if (img_count == 2 && a == 1)
            return 1;
        if (img_count == 2 && a == 2)
            return 2;
        if (img_count == 2 && a == 3)
            return 3;
        if (img_count == 3)
            return a;
        return 0;
    }
    // return the file the be write
    private File getFile() throws ParseException {
        File folder = new File("sdcard/workorder_app");
        if (!folder.exists()) {
            folder.mkdir();
        }

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        image_name[img_pos-1] = "image_"+formattedDate+".jpg";
        File image_file = new File(folder,image_name[img_pos-1]);
        return image_file;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getWorkOrderImage(View view, int pos) throws ParseException {
        // checking for deny permission once user click "Dont show this again"
/*        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
        } else {*/
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getFile();
            file_uri = Uri.fromFile(file);
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
            startActivityForResult(camera_intent, CAM_REQUEST);
/*        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 || resultCode == 0) {
            imageView2.setTag(R.drawable.camera);
            Object tag = imageView2.getTag();
            int camera = R.drawable.camera;
            if (((Integer)tag).intValue() != camera) {
                imageView2.setImageResource(camera);
            }

        } else {
            imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
            String image_path = "sdcard/workorder_app/" + image_name[img_pos-1];

            // cases to test for position of imageview clicked
            // if image count is 0, images will be filled left to right
            // if image count is more than 3, then user wants to replace image. We must check
            // the correct image position that the user clicked.
            switch (img_pos) {
                case 1:
                    imageView1.setImageDrawable(Drawable.createFromPath(image_path));
                    if (img_count == 1)
                        img_count--;
                    break;
                case 2:
                    if (img_count == 0) {
                        imageView1.setImageDrawable(Drawable.createFromPath(image_path));
                        img_pos = 1;
                    }
                    if (img_count >= 1) {
                        imageView2.setImageDrawable(Drawable.createFromPath(image_path));
                        img_pos = 2;
                    }
                    break;
                case 3:
                    if (img_count == 0) {
                        imageView1.setImageDrawable(Drawable.createFromPath(image_path));
                        img_pos = 1;
                    }
                    if (img_count == 1) {
                        imageView2.setImageDrawable(Drawable.createFromPath(image_path));
                        img_pos = 2;
                    }
                    if (img_count >= 2)
                        imageView3.setImageDrawable(Drawable.createFromPath(image_path));

                    break;
            }
            img_count++;
            //Toast.makeText(this, img_pos+"", Toast.LENGTH_SHORT).show();
            if (img_count == 1) {
                imageView2.setImageResource(plus);
            } else if (img_count ==2 ) {
                imageView3.setImageResource(plus);
            }

            new Encode_image().execute();
        }
    }

    // encode images
    private class Encode_image extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

            byte[] array = stream.toByteArray();
            encode_string[img_pos-1] = Base64.encodeToString(array, Base64.DEFAULT);

            bitmap.recycle();
            bitmap = null;
            return null;
        }
    }

    public void insert_to_database() {

        pDialog.setMessage("Adding New Work Order ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_WO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewWOActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String, String>();

                    if (encode_string[0] != null) {
                        map.put("encoded_string1", encode_string[0]);
                        map.put("image_name1", image_name[0]);
                    }
                    if (encode_string[1] != null) {
                        map.put("encoded_string2", encode_string[1]);
                        map.put("image_name2", image_name[1]);
                    }
                    if (encode_string[2] != null) {
                        map.put("encoded_string3", encode_string[2]);
                        map.put("image_name3", image_name[2]);
                    }


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
