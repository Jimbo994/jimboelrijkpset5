
package com.example.jim.jimboelrijkpset5;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DB_Helper extends SQLiteOpenHelper {


    // set fields of database scheme
    private static final String DATABASE_NAME = "myDB.db";
    private static final int DATABASE_VERSION = 1;
    static final String TABLE = "ToDO";
    static final String SUBTABLE = "SubTask";

    static String task = "task";
    private static final String _ID = "_id";
    static String subtask = "subtask";
    private static final String _PARENT_ID = "_parent_id";
    private static final String _CHILD_ID = "_id";
    private static final String _F_ID = "_f_id";


    // constructor
    DB_Helper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // on create
    @Override
    public void onCreate (SQLiteDatabase sqLitedatabase){
        String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( " + _PARENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + task + " TEXT);";
        sqLitedatabase.execSQL(CREATE_TABLE);

        String CREATE_CHILD_TABLE = "CREATE TABLE " + SUBTABLE + " ( " + _CHILD_ID + " INTEGER AUTOINCREMENT, FOREIGN KEY(" + _F_ID + ") REFERENCES(" + TABLE + "(" + _PARENT_ID + ")" + subtask + " TEXT );";
        sqLitedatabase.execSQL(CREATE_CHILD_TABLE);
    }


//on upgrade

    @Override
    public void onUpgrade(SQLiteDatabase sqLitedatabase, int i, int i1) {
        sqLitedatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        sqLitedatabase.execSQL("DROP TABLE IF EXISTS " + SUBTABLE);
        onCreate(sqLitedatabase);
    }

//crud methods






}
