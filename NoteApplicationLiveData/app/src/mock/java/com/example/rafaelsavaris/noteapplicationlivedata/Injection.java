package com.example.rafaelsavaris.noteapplicationlivedata;

import android.content.Context;

import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesRepository;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.local.NoteDatabase;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.local.NotesLocalDataSource;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.remote.MockRemoteDataSource;
import com.example.rafaelsavaris.noteapplicationlivedata.utils.AppExecutors;

/**
 * Created by rafael.savaris on 18/10/2017.
 */

public class Injection {

    public static NotesRepository providesNotesRepository(Context context){

        NoteDatabase database = NoteDatabase.getInstance(context);

        return NotesRepository.getInstance(MockRemoteDataSource.getInstance(), NotesLocalDataSource.getInstance(new AppExecutors(), database.noteDao()));

    }

}
