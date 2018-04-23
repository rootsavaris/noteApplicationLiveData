package com.example.rafaelsavaris.noteapplicationlivedata.notes.add;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.SingleLiveEvent;
import com.example.rafaelsavaris.noteapplicationlivedata.SnackbarMessage;
import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesDatasource;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesRepository;

public class AddEditNoteViewModel extends AndroidViewModel implements NotesDatasource.GetNoteCallBack{

    public final ObservableBoolean mDataLoading = new ObservableBoolean(false);

    public final ObservableField<String> mTitle = new ObservableField<>();

    public final ObservableField<String> mText = new ObservableField<>();

    private final SnackbarMessage mSnackbarMessage = new SnackbarMessage();

    private final SingleLiveEvent<Void> mNoteUpdated = new SingleLiveEvent<>();

    private boolean mIsNewNote;

    private boolean mIsDataLoaded;

    private String mNoteId;

    private boolean mNoteMarked = false;

    private final NotesRepository mNotesRepository;

    public AddEditNoteViewModel(@NonNull Application application, NotesRepository notesRepository) {
        super(application);
        mNotesRepository = notesRepository;
    }

    public SingleLiveEvent<Void> getNoteUpdatedEvent(){
        return mNoteUpdated;
    }

    public SnackbarMessage getSnackbarMessage(){
        return mSnackbarMessage;
    }

    @Override
    public void onNoteLoaded(Note note) {
        mTitle.set(note.getTitle());
        mText.set(note.getText());
        mNoteMarked = note.isMarked();
        mDataLoading.set(false);
        mIsDataLoaded = true;
    }

    @Override
    public void onDataNotAvailable() {

    }

    public void saveNote(){

        Note note = new Note(mTitle.get(), mText.get());

        if (note.isEmpty()){
            mSnackbarMessage.setValue(R.string.empty_note_text);
            return;
        }

        if (isNewNote() || mNoteId == null){
            createNote(note);
        } else {
            note = new Note(mTitle.get(), mText.get(), mNoteId, mNoteMarked);
            updateNote(note);
        }

    }

    private boolean isNewNote(){
        return mIsNewNote;
    }

    private void createNote(Note note){
        mNotesRepository.saveNote(note);
        mNoteUpdated.call();
    }

    private void updateNote(Note note){
        mNotesRepository.saveNote(note);
        mNoteUpdated.call();
    }

    public void start(String noteId){

        if (mDataLoading.get()){
            return;
        }

        mNoteId = noteId;

        if (noteId == null){
            mIsNewNote = true;
            return;
        }

        if (mIsDataLoaded){
            return;
        }

        mIsNewNote = false;

        mDataLoading.set(true);

        mNotesRepository.getNote(noteId, this);

    }

}
