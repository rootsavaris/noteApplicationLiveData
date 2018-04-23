package com.example.rafaelsavaris.noteapplicationlivedata.notes.add;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.ViewModelFactory;
import com.example.rafaelsavaris.noteapplicationlivedata.utils.ActivityUtils;

/**
 * Created by rafael.savaris on 01/12/2017.
 */

public class AddEditNoteActivity extends AppCompatActivity implements AddEditNoteNavigator {

    public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;

    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_edit_note_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        AddEditNoteFragment addEditNoteFragment = getFragment();

        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), addEditNoteFragment, R.id.contentFrame);

        AddEditNoteViewModel addEditNoteViewModel = getViewModel(this);

        addEditNoteViewModel.getNoteUpdatedEvent().observe(this, new Observer<Void>() {

            @Override
            public void onChanged(@Nullable Void aVoid) {
                AddEditNoteActivity.this.onNoteSaved();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private AddEditNoteFragment getFragment(){

        AddEditNoteFragment addEditNoteFragment = (AddEditNoteFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (addEditNoteFragment == null){
            addEditNoteFragment = AddEditNoteFragment.newInstance();
        }

        return addEditNoteFragment;

    }

    public static AddEditNoteViewModel getViewModel(FragmentActivity fragmentActivity){

        ViewModelFactory factory = ViewModelFactory.getInstance(fragmentActivity.getApplication());

        return ViewModelProviders.of(fragmentActivity, factory).get(AddEditNoteViewModel.class);

    }

    @Override
    public void onNoteSaved() {
        setResult(ADD_EDIT_RESULT_OK);
        finish();
    }
}
