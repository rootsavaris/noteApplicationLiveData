package com.example.rafaelsavaris.noteapplicationlivedata;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesRepository;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.add.AddEditNoteViewModel;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.detail.DetailNoteViewModel;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.list.NotesViewModel;

/**
 * Created by rafael.savaris on 28/03/2018.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private static volatile ViewModelFactory mInstance;

    private final Application mApplication;

    private final NotesRepository mNotesRepository;

    public static ViewModelFactory getInstance(Application application){

        if (mInstance == null){

            synchronized (ViewModelFactory.class){

                if (mInstance == null){

                    mInstance = new ViewModelFactory(application, Injection.providesNotesRepository(application.getApplicationContext()));

                }

            }

        }

        return mInstance;

    }

    public static void destroyInstance(){
        mInstance = null;
    }

    private ViewModelFactory(Application application, NotesRepository notesRepository){
        mApplication = application;
        mNotesRepository = notesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(NotesViewModel.class)){
            return (T) new NotesViewModel(mApplication, mNotesRepository);
        } else if (modelClass.isAssignableFrom(AddEditNoteViewModel.class)){
            return (T) new AddEditNoteViewModel(mApplication, mNotesRepository);
        }else if (modelClass.isAssignableFrom(DetailNoteViewModel.class)){
            return (T) new DetailNoteViewModel(mApplication, mNotesRepository);
        }

        return super.create(modelClass);

    }
}
