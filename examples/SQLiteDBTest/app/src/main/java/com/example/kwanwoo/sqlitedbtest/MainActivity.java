package com.example.kwanwoo.sqlitedbtest;

import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final static String TAG="SQLITEDBTEST";
    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new DBHelper(this);

        Button button = (Button)findViewById(R.id.insert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertRecord();
            }
        });

        Button button1 = (Button)findViewById(R.id.delete);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecord();
            }
        });

        Button button2 = (Button)findViewById(R.id.update);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRecord();
            }
        });

        Button button3 = (Button)findViewById(R.id.veiwall);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAllToTextView();
                viewAllToListView();

            }
        });

    }
    private void viewAllToTextView() {
        TextView result = (TextView)findViewById(R.id.result);

        Cursor cursor = mDbHelper.getAllDataBySQL();

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append(cursor.getInt(0)+" \t");
            buffer.append(cursor.getString(1)+" \t");
            buffer.append(cursor.getString(2)+"\n");
        }
        result.setText(buffer);
    }

    private void viewAllToListView() {

        Cursor cursor = mDbHelper.getAllDataByMethod();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.item, cursor, new String[]{
                UserContract.Users._ID,
                UserContract.Users.KEY_ACCOUNT,
                UserContract.Users.KEY_PASSWORD},
                new int[]{R.id._id, R.id.account, R.id.password}, 0);

        ListView lv = (ListView)findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    private void updateRecord() {
        EditText _id = (EditText)findViewById(R.id._id);
        EditText userid = (EditText)findViewById(R.id.userid);
        EditText password = (EditText)findViewById(R.id.password);

//        mDbHelper.updateDataBySQL(_id.getText().toString(),
//                                    userid.getText().toString(),
//                                    password.getText().toString());

        long nOfRows = mDbHelper.updateDataByMethod(_id.getText().toString(),
                userid.getText().toString(),
                password.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Deleted", Toast.LENGTH_SHORT).show();
    }

    private void deleteRecord() {
        EditText _id = (EditText)findViewById(R.id._id);

        //mDbHelper.deleteDataBySQL(_id.getText().toString());

        long nOfRows = mDbHelper.deleteDataByMethod(_id.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Deleted", Toast.LENGTH_SHORT).show();
    }

    private void insertRecord() {
        EditText userID = (EditText)findViewById(R.id.userid);
        EditText password = (EditText)findViewById(R.id.password);

        //mDbHelper.insertDataBySQL(userID.getText().toString(),password.getText().toString());

        long nOfRows = mDbHelper.insertDataByMethod(userID.getText().toString(),password.getText().toString());
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();
    }
}

