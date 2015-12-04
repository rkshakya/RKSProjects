package com.ravishakya.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.ravishakya.notes", Context.MODE_PRIVATE);
        set = sharedPreferences.getStringSet("notes", null);

        notes.clear();
        if (set != null){
            notes.addAll(set);
        }else{
            notes.add("Example note");
            set = new HashSet<String>();
            set.addAll(notes);
            sharedPreferences.edit().putStringSet("notes", set).apply();
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), EditYourNote.class);
                i.putExtra("noteId", position);
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete note")
                        .setMessage("Do you want to delete selected note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);

                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.ravishakya.notes", Context.MODE_PRIVATE);
                                if(MainActivity.set == null) {
                                    MainActivity.set = new HashSet<String>();
                                }else{
                                    MainActivity.set.clear();
                                }

                                MainActivity.set.addAll(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", MainActivity.set).apply();

                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            notes.add("");
            SharedPreferences sharedPreferences = this.getSharedPreferences("com.ravishakya.notes", Context.MODE_PRIVATE);
            if(set == null) {
                set = new HashSet<String>();
            }else{
                set.clear();
            }

            set.addAll(notes);
            sharedPreferences.edit().putStringSet("notes", set).apply();
            arrayAdapter.notifyDataSetChanged();

            Intent i = new Intent(getApplicationContext(), EditYourNote.class);
            i.putExtra("noteId", notes.size()-1 );
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
