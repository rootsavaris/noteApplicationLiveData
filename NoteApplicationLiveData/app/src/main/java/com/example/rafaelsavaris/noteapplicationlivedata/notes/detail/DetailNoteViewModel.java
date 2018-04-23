package com.example.rafaelsavaris.noteapplicationlivedata.notes.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.SingleLiveEvent;
import com.example.rafaelsavaris.noteapplicationlivedata.SnackbarMessage;
import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesDatasource;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesRepository;

public class DetailNoteViewModel extends AndroidViewModel implements NotesDatasource.GetNoteCallBack {

    public final ObservableField<Note> mNote = new ObservableField<>();

    public final ObservableBoolean mMarked = new ObservableBoolean();

    private final SingleLiveEvent<Void> mEditNoteCommand = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mDeleteNoteCommand = new SingleLiveEvent<>();

    private SnackbarMessage mSnackbarMessage = new SnackbarMessage();

    private boolean mIsDataLoading;

    private final NotesRepository mNotesRepository;

    public DetailNoteViewModel(@NonNull Application application, NotesRepository notesRepository) {
        super(application);
        mNotesRepository = notesRepository;
    }

    @Override
    public void onNoteLoaded(Note note) {
        setNote(note);
        mIsDataLoading = false;
    }

    @Override
    public void onDataNotAvailable() {
        mNote.set(null);
        mIsDataLoading = false;
    }

    public SingleLiveEvent<Void> getEditNoteCommand(){
        return mEditNoteCommand;
    }

    public SingleLiveEvent<Void> getDeleteNoteCommand(){
        return mDeleteNoteCommand;
    }

    public SnackbarMessage getSnackbarMessage() {
        return mSnackbarMessage;
    }

    public boolean isDataLoading() {
        return mIsDataLoading;
    }

    public boolean isDataAvailable(){
        return mNote.get() != null;
    }

    public void onRefresh(){

        if (mNote.get() != null){
            start(mNote.get().getId());
        }

    }

    public void editNote(){
        mEditNoteCommand.call();
    }

    public void start(String noteId){

        if (noteId != null){
            mIsDataLoading = true;
            mNotesRepository.getNote(noteId, this);
        }

    }

    public void deleteNote(){

        if (mNote.get() != null){
            mNotesRepository.deleteNote(mNote.get().getId());
            mDeleteNoteCommand.call();
        }

    }

    public void setNote(Note note){

        this.mNote.set(note);

        if (note != null){
            mMarked.set(note.isMarked());
        }

    }

    public void setMarked(boolean marked){

        if (isDataLoading()){
            return;
        }

        Note note = mNote.get();

        if (marked){
            mNotesRepository.markNote(note);
            showSnackbarMessage(R.string.note_marked);
        } else {
            mNotesRepository.unMarkNote(note);
            showSnackbarMessage(R.string.note_unmarked);
        }

    }

    private void showSnackbarMessage(@StringRes Integer message){
        mSnackbarMessage.setValue(message);
    }

}
