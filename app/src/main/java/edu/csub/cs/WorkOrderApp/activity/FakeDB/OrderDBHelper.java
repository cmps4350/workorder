package edu.csub.cs.WorkOrderApp.activity.FakeDB;


 import android.content.Context;
 import android.database.sqlite.SQLiteDatabase;
 import android.database.sqlite.SQLiteOpenHelper;

public class OrderDBHelper extends SQLiteOpenHelper {

    public OrderDBHelper(Context context) {
        super(context, OrderContract.DB_NAME, null, OrderContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + OrderContract.TaskEntry.TABLE + " ( " +
                OrderContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OrderContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OrderContract.TaskEntry.TABLE);
        onCreate(db);
    }
}