package com.example.rafaelsavaris.noteapplicationlivedata.notes.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.ViewModelFactory;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.add.AddEditNoteActivity;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.detail.DetailNoteActivity;
import com.example.rafaelsavaris.noteapplicationlivedata.utils.ActivityUtils;


public class NotesActivity extends AppCompatActivity implements NotesNavigator, NoteItemNavigator {

    private NotesViewModel mNotesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (notesFragment == null) {

            notesFragment = NotesFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), notesFragment, R.id.contentFrame);

        }

        mNotesViewModel = getViewModel(this);

        mNotesViewModel.getOpenNoteEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String noteId) {

                if (noteId != null){
                    openDetailNote(noteId);
                }

            }
        });

        mNotesViewModel.getNewNoteEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                addNewNote();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void addNewNote() {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        startActivityForResult(intent, AddEditNoteActivity.REQUEST_CODE);
    }

    @Override
    public void openDetailNote(String noteId) {
        Intent intent = new Intent(this, DetailNoteActivity.class);
        intent.putExtra(DetailNoteActivity.NOTE_ID, noteId);
        startActivityForResult(intent, AddEditNoteActivity.REQUEST_CODE);
    }

    public static NotesViewModel getViewModel(FragmentActivity fragmentActivity){

        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance(fragmentActivity.getApplication());

        NotesViewModel notesViewModel = ViewModelProviders.of(fragmentActivity, viewModelFactory).get(NotesViewModel.class);

        return notesViewModel;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mNotesViewModel.handleActivityResult(requestCode, resultCode);
    }
}
