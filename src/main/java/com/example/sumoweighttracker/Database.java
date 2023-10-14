package com.example.sumoweighttracker;

import static com.example.sumoweighttracker.ui.login.DatabaseHelper.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
// added multiple items for the added functionality of the code that must be imported for the new
// methods to interact properly
import com.example.sumoweighttracker.ui.login.DatabaseHelper;

import java.util.ArrayList;
// Add a constant to DatabaseHelper class for "timestamp" -- Complete
// Update the DATABASE_CREATE SQL statement -- Complete


public class Database extends AppCompatActivity {

    private ArrayList<String> entries;
    private ArrayAdapter<String> entriesAdapter;
    private ListView listView;
    private Button button;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        listView = findViewById(R.id.ListView);
        button = findViewById(R.id.button);
        dbHelper = new DatabaseHelper(this);

        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        entries = new ArrayList<>();
        entriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entries);
        listView.setAdapter(entriesAdapter);
        setUpListViewListener();
        loadEntriesFromDatabase();
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Weight Removed", Toast.LENGTH_LONG).show();

                // Remove the entry from the database
                removeEntryFromDatabase(i);

                // Remove the entry from the list and update the adapter
                entries.remove(i);
                entriesAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.editTextTextPersonName);
        String entry = input.getText().toString();

        if (!entry.equals("")) {
            // Insert the entry into the database
            insertEntryIntoDatabase(entry);

            // Add the entry to the list and update the adapter
            entriesAdapter.add(entry);
            input.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Please do not leave blank", Toast.LENGTH_LONG).show();
        }
    }

    private void insertEntryIntoDatabase(String entry) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ENTRY, entry);
        db.insert(DatabaseHelper.TABLE_ENTRIES, null, values);
    }


    private void removeEntryFromDatabase(int position) {
        String entry = entries.get(position);
        db.delete(TABLE_ENTRIES, COLUMN_ENTRY + " = ?", new String[]{entry});
    }

    private void loadEntriesFromDatabase() {
        entries.clear();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ENTRIES,
                new String[]{DatabaseHelper.COLUMN_ENTRY, DatabaseHelper.COLUMN_TIMESTAMP},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String entry = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ENTRY));
                String timestamp = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP));
                entries.add(entry + " (Added: " + timestamp + ")");
            }
            cursor.close();
        }
        entriesAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
//method to add timestamp t all entries
    public static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ENTRIES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_ENTRY
            + " text not null, " + COLUMN_TIMESTAMP
            + " timestamp default current_timestamp);";
}
