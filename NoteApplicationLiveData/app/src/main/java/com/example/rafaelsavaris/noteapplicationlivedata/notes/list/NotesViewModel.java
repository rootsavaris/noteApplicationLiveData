package com.example.rafaelsavaris.noteapplicationlivedata.notes.list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.SingleLiveEvent;
import com.example.rafaelsavaris.noteapplicationlivedata.SnackbarMessage;
import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesDatasource;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesRepository;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.add.AddEditNoteActivity;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.detail.DetailNoteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael.savaris on 28/03/2018.
 */

public class NotesViewModel extends AndroidViewModel{

    public final ObservableList<Note> mItems = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> mNotesFilterTypeLabel = new ObservableField<>();

    public final ObservableField<String> mNoNotesLabel = new ObservableField<>();

    public final ObservableField<Drawable> mNoNotesIconRes = new ObservableField<>();

    public final ObservableBoolean empty = new ObservableBoolean(false);

    public final ObservableField<Boolean> mNotesAddViewVisible = new ObservableField<>();

    public final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private NotesFilterType mNotesFilterType = NotesFilterType.ALL_NOTES;

    private final NotesRepository mNotesRepository;

    private final Context mContext;

    private final SnackbarMessage mSnackbarMessage = new SnackbarMessage();

    private final SingleLiveEvent<String> mOpenNoteEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mNewNoteEvent = new SingleLiveEvent<>();


    public NotesViewModel(@NonNull Application application, NotesRepository notesRepository) {
        super(application);
        mContext = application.getApplicationContext();
        mNotesRepository = notesRepository;

        setFilter(NotesFilterType.ALL_NOTES);

    }

    public void setFilter(NotesFilterType notesFilterType){

        mNotesFilterType = notesFilterType;

        switch (notesFilterType){

            case ALL_NOTES:
                mNotesFilterTypeLabel.set(mContext.getResources().getString(R.string.label_all));
                mNoNotesLabel.set(mContext.getResources().getString(R.string.no_notes_all));
                mNoNotesIconRes.set(mContext.getResources().getDrawable(R.drawable.ic_assignment_turned_in_24dp));
                mNotesAddViewVisible.set(true);
                break;
            case MARKED_NOTES:
                mNotesFilterTypeLabel.set(mContext.getResources().getString(R.string.label_marked));
                mNoNotesLabel.set(mContext.getResources().getString(R.string.no_notes_marked));
                mNoNotesIconRes.set(mContext.getResources().getDrawable(R.drawable.ic_verified_user_24dp));
                mNotesAddViewVisible.set(false);
                break;
        }

    }

    public void start(){
        loadNotes(false);
    }

    public void loadNotes(boolean forceUpdate) {
        loadNotes(forceUpdate, true);
    }

    private void loadNotes(boolean forceUpdate, final boolean showLoading){

        if (showLoading){
            dataLoading.set(true);
        }

        if (forceUpdate){
            mNotesRepository.refreshNotes();
        }

        mNotesRepository.getNotes(new NotesDatasource.LoadNotesCallBack() {

            @Override
            public void onNotesLoaded(List<Note> notes) {

                List<Note> notesToShow = new ArrayList<>();

                for (Note note : notes){

                    switch (mNotesFilterType){

                        case MARKED_NOTES:

                            if(note.isMarked()){
                                notesToShow.add(note);
                            }

                            break;

                        default:
                            notesToShow.add(note);
                    }

                }

                if (showLoading){
                    dataLoading.set(false);
                }

                mIsDataLoadingError.set(false);

                mItems.clear();

                mItems.addAll(notesToShow);

                empty.set(mItems.isEmpty());

            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.set(true);
            }

        });

    }

    public void clearMarkedNotes() {
        mNotesRepository.clearMarkedNotes();
        mSnackbarMessage.setValue(R.string.marked_notes_cleared);
        loadNotes(false, false);
    }

    public void markNote(Note markedNote) {
        mNotesRepository.markNote(markedNote);
        showSnackbarMessage(R.string.note_marked);
    }

    public void unMarkNote(Note markedNote) {
        mNotesRepository.unMarkNote(markedNote);
        showSnackbarMessage(R.string.note_unmarked);
    }

    SingleLiveEvent<String> getOpenNoteEvent(){
        return mOpenNoteEvent;
    }

    SingleLiveEvent<Void> getNewNoteEvent(){
        return mNewNoteEvent;
    }

    SnackbarMessage getSnackbarMessage(){
        return mSnackbarMessage;
    }

    public void addNewNote(){
        mNewNoteEvent.call();
    }

    private void showSnackbarMessage(Integer message){
        mSnackbarMessage.setValue(message);
    }

    void handleActivityResult(int requestCode, int resultCode){

        if (AddEditNoteActivity.REQUEST_CODE == requestCode){

            switch (resultCode){

                case DetailNoteActivity.EDIT_RESULT_OK:
                    mSnackbarMessage.setValue(R.string.successfully_saved_note_message);
                    break;
                case AddEditNoteActivity.ADD_EDIT_RESULT_OK:
                    mSnackbarMessage.setValue(R.string.successfully_added_note_message);
                    break;
                case DetailNoteActivity.DELETE_RESULT_OK:
                    mSnackbarMessage.setValue(R.string.successfully_deleted_note_message);
                    break;
            }

        }

    }

}
