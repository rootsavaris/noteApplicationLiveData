package com.example.rafaelsavaris.noteapplicationlivedata.notes.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.SnackbarMessage;
import com.example.rafaelsavaris.noteapplicationlivedata.databinding.AddEditNoteFragBinding;
import com.example.rafaelsavaris.noteapplicationlivedata.utils.SnackbarUtils;

/**
 * Created by rafael.savaris on 01/12/2017.
 */

public class AddEditNoteFragment extends Fragment {

    public static final String NOTE_ID = "NOTE_ID";

    public static final int REQUEST_EDIT_NOTE = 1;

    private AddEditNoteViewModel mAddEditNoteViewModel;

    private AddEditNoteFragBinding mAddEditNoteFragBinding;

    public AddEditNoteFragment() {
    }

    public static AddEditNoteFragment newInstance(){
        return new AddEditNoteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.add_edit_note_frag, container, false);

        if (mAddEditNoteFragBinding == null) {
            mAddEditNoteFragBinding = AddEditNoteFragBinding.bind(root);
        }

        mAddEditNoteViewModel = AddEditNoteActivity.getViewModel(getActivity());

        mAddEditNoteFragBinding.setViewmodel(mAddEditNoteViewModel);

        setHasOptionsMenu(true);

        setRetainInstance(false);

        return mAddEditNoteFragBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        setupActionBar();

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_note_done);

        fab.setImageResource(R.drawable.ic_done);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddEditNoteViewModel.saveNote();
            }
        });

        mAddEditNoteViewModel.getSnackbarMessage().observe(this, new SnackbarMessage.SnackbarObserver() {
            @Override
            public void onNewMessage(int snackbarMessageResourceId) {
                SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId));
            }
        });

        if (getActivity().getIntent() != null && getActivity().getIntent().hasExtra(NOTE_ID)){
            mAddEditNoteViewModel.start((String) getActivity().getIntent().getExtras().get(NOTE_ID));
        } else {
            mAddEditNoteViewModel.start(null);
        }

    }

    private void setupActionBar(){

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar == null){
            return;
        }

        if (getActivity().getIntent() != null && getActivity().getIntent().hasExtra(NOTE_ID) && getActivity().getIntent().getExtras().get(NOTE_ID) != null){
            actionBar.setTitle(R.string.edit_note);
        } else {
            actionBar.setTitle(R.string.add_note);
        }

    }

    /*
    @Override
    public void setPresenter(AddEditNoteContract.Presenter presenter) {
        mAddEditNotePresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddEditNotePresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.add_edit_note_frag, container, false);
        mTitle = root.findViewById(R.id.add_edit_note_title);
        mText = root.findViewById(R.id.add_edit_note_text);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_note_done);

        fab.setImageResource(R.drawable.ic_done);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddEditNotePresenter.saveNote(mTitle.getText().toString(), mText.getText().toString());
            }
        });

    }

    @Override
    public void showEmptyNotesError() {
        Snackbar.make(mTitle, getString(R.string.empty_note_text), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNotesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setText(String text) {
        mText.setText(text);
    }

    */

}
