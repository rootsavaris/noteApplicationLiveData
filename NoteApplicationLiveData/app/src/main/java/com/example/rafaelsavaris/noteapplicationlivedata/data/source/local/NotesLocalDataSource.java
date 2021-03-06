package com.example.rafaelsavaris.noteapplicationlivedata.data.source.local;

import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesDatasource;
import com.example.rafaelsavaris.noteapplicationlivedata.utils.AppExecutors;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by rafael.savaris on 18/10/2017.
 */

public class NotesLocalDataSource implements NotesDatasource {

    private static volatile NotesLocalDataSource mInstance;

    private NoteDao mNoteDao;

    private AppExecutors mAppExecutors;

    private NotesLocalDataSource(AppExecutors appExecutors, NoteDao noteDao) {
        mAppExecutors = appExecutors;
        mNoteDao = noteDao;
    }

    public static NotesLocalDataSource getInstance(AppExecutors appExecutors, NoteDao noteDao) {

        if (mInstance == null) {

            synchronized (NotesLocalDataSource.class){

                if (mInstance == null){
                    mInstance = new NotesLocalDataSource(appExecutors, noteDao);
                }

            }

        }

        return mInstance;

    }

    public static void clearInstance(){
        mInstance = null;
    }

    @Override
    public void getNotes(final LoadNotesCallBack loadNotesCallBack) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final List<Note> notes = mNoteDao.getNotes();

                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        if (notes.isEmpty()){
                            loadNotesCallBack.onDataNotAvailable();
                        } else{
                            loadNotesCallBack.onNotesLoaded(notes);
                        }

                    }
                });
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);

    }

    @Override
    public void getNote(final String noteId, final GetNoteCallBack getNoteCallBack) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final Note note = mNoteDao.getNoteById(noteId);

                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {

                        if (note != null){
                            getNoteCallBack.onNoteLoaded(note);
                        } else {
                            getNoteCallBack.onDataNotAvailable();
                        }

                    }
                });

            }
        };

        mAppExecutors.getDiskIO().execute(runnable);

    }

    @Override
    public void deleteAllNotes() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteNotes();
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);

    }

    @Override
    public void saveNote(final Note note) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mNoteDao.insertNote(note);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);

    }

    @Override
    public void refreshNotes() {
    }

    @Override
    public void markNote(final Note note) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mNoteDao.updateMarked(note.getId(), true);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);

    }

    @Override
    public void markNote(String noteId) {
    }

    @Override
    public void unMarkNote(final Note note) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mNoteDao.updateMarked(note.getId(), false);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);

    }

    @Override
    public void unMarkNote(String noteId) {

    }

    @Override
    public void clearMarkedNotes() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteMarkedNotes();
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);

    }

    @Override
    public void deleteNote(final String noteId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mNoteDao.deleteNoteById(noteId);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);

    }

}
