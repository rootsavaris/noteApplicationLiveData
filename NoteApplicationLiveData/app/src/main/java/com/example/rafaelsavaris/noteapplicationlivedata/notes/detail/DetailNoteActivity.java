package com.example.rafaelsavaris.noteapplicationlivedata.notes.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.ViewModelFactory;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.add.AddEditNoteActivity;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.add.AddEditNoteFragment;
import com.example.rafaelsavaris.noteapplicationlivedata.utils.ActivityUtils;

/**
 * Created by rafael.savaris on 01/12/2017.
 */

public class DetailNoteActivity extends AppCompatActivity {

    public static final int EDIT_RESULT_OK = RESULT_FIRST_USER + 2;

    public static final int DELETE_RESULT_OK = RESULT_FIRST_USER + 3;

    public static final String NOTE_ID = "NOTE_ID";

    private DetailNoteViewModel mDetailNoteViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_note_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //String noteId = getIntent().getStringExtra(NOTE_ID);

        DetailNoteFragment detailNoteFragment = (DetailNoteFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (detailNoteFragment == null) {
            detailNoteFragment = DetailNoteFragment.newInstance();
        }

        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                detailNoteFragment, R.id.contentFrame);

        mDetailNoteViewModel = getViewModel(this);

        mDetailNoteViewModel.getEditNoteCommand().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                DetailNoteActivity.this.startEditNote();
            }
        });

        mDetailNoteViewModel.getDeleteNoteCommand().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void aVoid) {
                DetailNoteActivity.this.deletedNote();
            }
        });

    }

    public static DetailNoteViewModel getViewModel(FragmentActivity fragmentActivity){

        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance(fragmentActivity.getApplication());

        return ViewModelProviders.of(fragmentActivity, viewModelFactory).get(DetailNoteViewModel.class);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DetailNoteFragment.REQUEST_EDIT_NOTE){

            if (resultCode == AddEditNoteActivity.ADD_EDIT_RESULT_OK){
                setResult(EDIT_RESULT_OK);
                finish();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startEditNote(){

        String noteId = getIntent().getStringExtra(NOTE_ID);

        Intent intent = new Intent(this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteFragment.NOTE_ID, noteId);
        startActivityForResult(intent, AddEditNoteFragment.REQUEST_EDIT_NOTE);

    }

    public void deletedNote(){

        setResult(DELETE_RESULT_OK);

        finish();

    }

}
