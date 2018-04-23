package com.example.rafaelsavaris.noteapplicationlivedata.notes.add;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesDatasource;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rafael.savaris on 11/01/2018.
 */

public class AddEditNoteViewModelTest {

    private static final String TITLE = "title";
    private static final String TEXT = "text";

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private NotesRepository mNotesRepository;

    @Captor
    private ArgumentCaptor<NotesDatasource.GetNoteCallBack> mGetNoteCallBackArgumentCaptor;

    private AddEditNoteViewModel mAddEditNoteViewModel;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);

        mAddEditNoteViewModel = new AddEditNoteViewModel(mock(Application.class), mNotesRepository);

    }

    @Test
    public void saveNewNoteToRepository_showsSuccessMessageUi(){

        mAddEditNoteViewModel.mText.set(TEXT);
        mAddEditNoteViewModel.mTitle.set(TITLE);
        mAddEditNoteViewModel.saveNote();

        verify(mNotesRepository).saveNote(any(Note.class));

    }

    @Test
    public void populateNote_callsRepoAndUpdatesView(){

        Note note = new Note(TITLE, TEXT);

        mAddEditNoteViewModel = new AddEditNoteViewModel(mock(Application.class), mNotesRepository);

        mAddEditNoteViewModel.start(note.getId());

        verify(mNotesRepository).getNote(eq(note.getId()), mGetNoteCallBackArgumentCaptor.capture());

        mGetNoteCallBackArgumentCaptor.getValue().onNoteLoaded(note);

        assertThat(mAddEditNoteViewModel.mTitle.get(), is(note.getTitle()));
        assertThat(mAddEditNoteViewModel.mText.get(), is(note.getText()));

    }

}
