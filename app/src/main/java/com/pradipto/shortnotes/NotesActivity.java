package com.pradipto.shortnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesActivity extends AppCompatActivity {

    EditText textTitle, textNote;
    ImageView save;
    Notes notes;
    Boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        textTitle = findViewById(R.id.edit_title);
        textNote = findViewById(R.id.edit_notes);
        save = findViewById(R.id.save_btn);

        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("oldNote");
            textTitle.setText(notes.getTitle());
            textNote.setText(notes.getNotes());
            isOldNote = true;
        }catch (Exception e) {
            e.printStackTrace();
        }

        save.setOnClickListener(view -> {
            String title = textTitle.getText().toString();
            String note = textNote.getText().toString();
            if(note.isEmpty()) {
                Toast.makeText(NotesActivity.this,"Nothing to save",Toast.LENGTH_SHORT).show();
                finish();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, H:mm");
            Date date = new Date();

            if(!isOldNote) {
                notes = new Notes();
            }
            notes.setTitle(title);
            notes.setNotes(note);
            notes.setDate(dateFormat.format(date));

            Intent intent = new Intent();
            intent.putExtra("notes",notes);
            setResult(Activity.RESULT_OK,intent);
            finish();
        });
    }
}