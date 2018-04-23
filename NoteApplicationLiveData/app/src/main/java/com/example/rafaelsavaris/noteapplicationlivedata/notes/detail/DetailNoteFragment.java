package com.example.rafaelsavaris.noteapplicationlivedata.notes.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.SnackbarMessage;
import com.example.rafaelsavaris.noteapplicationlivedata.databinding.DetailNoteFragmentBinding;
import com.example.rafaelsavaris.noteapplicationlivedata.utils.SnackbarUtils;

/**
 * Created by rafael.savaris on 02/01/2018.
 */

public class DetailNoteFragment extends Fragment{

    public static final int REQUEST_EDIT_NOTE = 1;

    private DetailNoteViewModel mDetailNoteViewModel;

    public static DetailNoteFragment newInstance(){
        return new DetailNoteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.detail_note_fragment, container, false);

        DetailNoteFragmentBinding detailNoteFragmentBinding = DetailNoteFragmentBinding.bind(root);

        mDetailNoteViewModel = DetailNoteActivity.getViewModel(getActivity());

        detailNoteFragmentBinding.setViewmodel(mDetailNoteViewModel);

        DetailNoteUserActionsListener detailNoteUserActionsListener = getDetailNoteUserActionsListener();

        detailNoteFragmentBinding.setListener(detailNoteUserActionsListener);

        setHasOptionsMenu(true);

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_note);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailNoteViewModel.editNote();
            }
        });

        mDetailNoteViewModel.getSnackbarMessage().observe(this, new SnackbarMessage.SnackbarObserver() {
            @Override
            public void onNewMessage(int snackbarMessageResourceId) {
                SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mDetailNoteViewModel.start(
                getActivity().getIntent() != null && getActivity().getIntent().hasExtra(DetailNoteActivity.NOTE_ID) ? (String) getActivity().getIntent().getExtras().get(DetailNoteActivity.NOTE_ID) : null
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_delete:
                mDetailNoteViewModel.deleteNote();
                return true;
        }

        return false;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_note_fragment_menu, menu);
    }

    private DetailNoteUserActionsListener getDetailNoteUserActionsListener(){
        return new DetailNoteUserActionsListener() {
            @Override
            public void onMarkChanged(View view) {
                mDetailNoteViewModel.setMarked(((CheckBox) view).isChecked());
            }
        };
    }

}
