package com.pradipto.shortnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView noteView;
    NotesAdapter noteAdapter;
    List<Notes> noteList = new ArrayList<>();
    DataRoom database;
    FloatingActionButton addbtn;
    Notes selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteView = findViewById(R.id.notes_view);
        addbtn = findViewById(R.id.add_btn);

        database = DataRoom.getInstance(this);
        noteList = database.daoBase().getAll();
        
        updateRV(noteList);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    private void updateRV(List<Notes> noteList) {

        noteView.setHasFixedSize(true);
        noteView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        noteAdapter = new NotesAdapter(MainActivity.this, noteList, noteClicked);
        noteView.setAdapter(noteAdapter);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101) {
            if(resultCode==RESULT_OK) {
                assert data != null;
                Notes newNotes = (Notes) data.getSerializableExtra("notes");
                database.daoBase().insert(newNotes);
                noteList.clear();
                noteList.addAll(database.daoBase().getAll());
                noteAdapter.notifyDataSetChanged();
            }
        }

        else if (requestCode==102) {
            if(resultCode==RESULT_OK) {
                assert data != null;
                Notes newNotes = (Notes) data.getSerializableExtra("notes");
                database.daoBase().update(newNotes.getID(), newNotes.getTitle(), newNotes.getNotes());
                noteList.clear();
                noteList.addAll(database.daoBase().getAll());
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                noteAdapter.notifyDataSetChanged();
            }
        }
    }

    private final NotesClickListener noteClicked = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            intent.putExtra("oldNote", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selected = new Notes();
            selected = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, cardView);
        popupMenu.setOnMenuItemClickListener(MainActivity.this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.delete:
                database.daoBase().delete(selected);
                noteList.remove(selected);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}