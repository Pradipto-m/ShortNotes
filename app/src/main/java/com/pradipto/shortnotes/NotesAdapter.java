package com.pradipto.shortnotes;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    List<Notes> notesList;
    NotesClickListener clicked;

    public NotesAdapter(Context context, List<Notes> notesList, NotesClickListener clicked) {
        this.context = context;
        this.notesList = notesList;
        this.clicked = clicked;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.title.setText(notesList.get(position).getTitle());
        holder.title.setSelected(true);
        holder.notes.setText(notesList.get(position).getNotes());
        holder.date.setText(notesList.get(position).getDate());

        int colour = getColor();
        holder.noteCard.setCardBackgroundColor(holder.itemView.getResources().getColor(colour, null));

        holder.noteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked.onClick(notesList.get(holder.getAdapterPosition()));
            }
        });

        holder.noteCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clicked.onLongClick(notesList.get(holder.getAdapterPosition()), holder.noteCard);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    private int getColor() {

        List<Integer> colors = new ArrayList<>();
        colors.add(R.color.color1);
        colors.add(R.color.color2);
        colors.add(R.color.color3);
        colors.add(R.color.color4);
        colors.add(R.color.color5);
        colors.add(R.color.color6);

        Random random = new Random();
        int randomInt = random.nextInt(colors.size());
        return colors.get(randomInt);
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {

        CardView noteCard;
        TextView title, notes, date;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            noteCard = itemView.findViewById(R.id.notecard);
            title = itemView.findViewById(R.id.title);
            notes = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }
    }
}
