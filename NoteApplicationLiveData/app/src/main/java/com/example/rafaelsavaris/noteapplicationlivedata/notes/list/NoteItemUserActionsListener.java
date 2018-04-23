package com.example.rafaelsavaris.noteapplicationlivedata.notes.list;

import android.view.View;

import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;

public interface NoteItemUserActionsListener {

    void onMarkChanged(Note note, View view);

    void onNoteClicked(Note note);

}
