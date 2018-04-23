package com.example.rafaelsavaris.noteapplicationlivedata.notes.list;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;

import java.util.List;

public class NotesListBindings {

    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<Note> notes){

        NotesAdapter adapter = (NotesAdapter) listView.getAdapter();

        if (adapter != null){
            adapter.replaceData(notes);
        }

    }

}
