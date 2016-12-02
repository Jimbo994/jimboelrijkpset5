package com.example.jim.jimboelrijkpset5;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SubTask extends AppCompatActivity {
    ListView SubTaskListView;
    //EditText to_be_added;
    private ArrayAdapter<String> arrayAdapter;
    private DB_Helper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_task);
        mHelper = new DB_Helper(this);
        SubTaskListView = (ListView) findViewById(R.id.SubTask_list);

        updateUI();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(DB_Helper.task, task);
                                db.insertWithOnConflict(DB_Helper.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(DB_Helper.SUBTABLE,
                new String[]{DB_Helper.subtask},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(DB_Helper.subtask);
            taskList.add(cursor.getString(idx));
        }

        if (arrayAdapter == null) {
            arrayAdapter = new ArrayAdapter<>(this,
                    R.layout.listview,
                    R.id.task_title,
                    taskList);
            SubTaskListView.setAdapter(arrayAdapter);
        } else {
            arrayAdapter.clear();
            arrayAdapter.addAll(taskList);
            arrayAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask(final View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete or Mark as done")
                .setMessage("What do you want us to do?")
//                .setNegativeButton("SubTasks", new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int which){
//                        Toast toast = Toast.makeText(getApplicationContext(), "adding", Toast.LENGTH_LONG);
//                        toast.show();
//                        Intent ToSubList = new Intent(getApplicationContext(), SubTask.class);
//                        int ID = which;
//                        ToSubList.putExtra("id", ID);
//                        startActivity(ToSubList);
//                        finish();
//                    }
//                })
                .setNeutralButton("Mark as done", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        View parent = (View) view.getParent();
                        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
                        taskTextView.setTextColor(Color.GREEN);
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View parent = (View) view.getParent();
                        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
                        String task = String.valueOf(taskTextView.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        db.delete(DB_Helper.SUBTABLE,
                                DB_Helper.subtask + " = ?",
                                new String[]{task});
                        db.close();
                        updateUI();
                    }
                })
                //.setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

}
