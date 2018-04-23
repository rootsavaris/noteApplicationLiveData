package com.example.rafaelsavaris.noteapplicationlivedata.notes.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.ScrollChildSwipeRefreshLayout;
import com.example.rafaelsavaris.noteapplicationlivedata.SnackbarMessage;
import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.add.AddEditNoteActivity;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.detail.DetailNoteActivity;

import com.example.rafaelsavaris.noteapplicationlivedata.databinding.NotesFragmentBinding;
import com.example.rafaelsavaris.noteapplicationlivedata.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rafael.savaris on 18/10/2017.
 */

public class NotesFragment extends Fragment{

    private NotesViewModel mNotesViewModel;

    private NotesFragmentBinding mNotesFragmentBinding;

    private NotesAdapter mNotesAdapter;

    public NotesFragment(){}

    public static NotesFragment newInstance(){
        return new NotesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mNotesFragmentBinding = NotesFragmentBinding.inflate(inflater, container, false);

        mNotesViewModel = NotesActivity.getViewModel(getActivity());

        mNotesFragmentBinding.setViewmodel(mNotesViewModel);

        setHasOptionsMenu(true);

        return mNotesFragmentBinding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        mNotesViewModel.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mNotesViewModel.clearMarkedNotes();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                mNotesViewModel.loadNotes(true);
                break;
        }
        return true;
    }

    public void showFilteringPopUpMenu() {

        PopupMenu popupMenu = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));

        popupMenu.getMenuInflater().inflate(R.menu.filter_notes, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.marked:
                        mNotesViewModel.setFilter(NotesFilterType.MARKED_NOTES);
                        break;
                    default:
                        mNotesViewModel.setFilter(NotesFilterType.ALL_NOTES);
                        break;

                }

                mNotesViewModel.loadNotes(false);

                return true;

            }
        });

        popupMenu.show();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSnackbar();

        setupFab();

        setupListAdapter();

        setupRefreshLayout();

    }

    private void setupSnackbar(){

        mNotesViewModel.getSnackbarMessage().observe(this, new SnackbarMessage.SnackbarObserver() {
            @Override
            public void onNewMessage(@StringRes int snackbarMessageResourceId) {
                SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId));
            }
        });


    }

    private void setupFab(){

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_note);

        fab.setImageResource(R.drawable.ic_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotesViewModel.addNewNote();
            }
        });


    }

    private void setupListAdapter(){

        ListView listView = mNotesFragmentBinding.notesList;

        mNotesAdapter = new NotesAdapter(new ArrayList<Note>(0), mNotesViewModel);

        listView.setAdapter(mNotesAdapter);

    }

    private void setupRefreshLayout(){

        ListView listView = mNotesFragmentBinding.notesList;

        final ScrollChildSwipeRefreshLayout scrollChildSwipeRefreshLayout = mNotesFragmentBinding.refreshLayout;

        scrollChildSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        scrollChildSwipeRefreshLayout.setScrollUpChild(listView);

    }

}
