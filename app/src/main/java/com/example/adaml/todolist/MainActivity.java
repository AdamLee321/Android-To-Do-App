package com.example.adaml.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DbHelper dbHelper;
    ItemAdapter mAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.ab_centered);

        dbHelper = new DbHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.listItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new ItemAdapter(this, getItemList());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                updateItem((long) viewHolder.itemView.getTag());
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

    }

    public void insertNewItem(String item){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemContract.ItemEntry.DB_COLUMN,item);
        mDatabase.insert(ItemContract.ItemEntry.DB_TABLE,null,contentValues);
        mAdapter.swapCursor(getItemList());
    }

    public void updateItem(long id){
        String query = "SELECT "+ ItemContract.ItemEntry.DB_ISDONE +" FROM "+ ItemContract.ItemEntry.DB_TABLE +" WHERE " + ItemContract.ItemEntry._ID + " = " + id;
        Cursor isDone = mDatabase.rawQuery(query,null);
        isDone.moveToFirst();
        int val = isDone.getInt(0);
        ContentValues contentValues = new ContentValues();
        int update;
        if(val == 0){
            update = 1;
        }
        else{
            update = 0;
        }
        contentValues.put(ItemContract.ItemEntry.DB_ISDONE, update);
        mDatabase.update(ItemContract.ItemEntry.DB_TABLE,contentValues,ItemContract.ItemEntry._ID + " = " + id, null);
        mAdapter.swapCursor(getItemList());
    }

    public void deleteItem(long id){
        mDatabase.delete(ItemContract.ItemEntry.DB_TABLE,ItemContract.ItemEntry._ID + " = " +id, null);
        mAdapter.swapCursor(getItemList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addItem:
                final EditText text = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                        .setTitle("Add New Item")
                        .setView(text)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String item = String.valueOf(text.getText());
                                insertNewItem(item);
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Cursor getItemList(){
        return mDatabase.query(ItemContract.ItemEntry.DB_TABLE,null,null,null,null,null, ItemContract.ItemEntry._ID + " DESC");
    }
}