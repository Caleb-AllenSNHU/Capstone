package com.example.sumoweighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Database extends AppCompatActivity {

    private ArrayList<String> entries;
    private ArrayAdapter<String> entriesAdapter;
    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        listView = findViewById(R.id.ListView);
        button = findViewById(R.id.button);

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
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Weight Removed", Toast.LENGTH_LONG).show();

                entries.remove(i);
                entriesAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.editTextTextPersonName);
        String entry = input.getText().toString();

        if (!(entry.equals(""))){
            entriesAdapter.add(entry);
            input.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(), "Please do not leave blank", Toast.LENGTH_LONG);
        }
    }
}