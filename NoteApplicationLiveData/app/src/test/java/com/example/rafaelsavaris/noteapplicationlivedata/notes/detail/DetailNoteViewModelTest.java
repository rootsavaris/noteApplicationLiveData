package com.example.rafaelsavaris.noteapplicationlivedata.notes.detail;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.res.Resources;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.SnackbarMessage;
import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesDatasource;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesRepository;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rafael.savaris on 11/01/2018.
 */

public class DetailNoteViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final String TITLE = "title";

    private static final String TEXT = "text";

    private static final String NO_DATA = "no data";

    private static final String NO_DATA_DESC = "no data desc";

    @Mock
    private NotesRepository mNotesRepository;

    @Mock
    private Application mApplication;

    @Mock
    private NotesDatasource.LoadNotesCallBack mLoadNotesCallBack;

    @Mock
    private NotesDatasource.GetNoteCallBack mGetNoteCallBack;

    @Captor
    private ArgumentCaptor<NotesDatasource.GetNoteCallBack> mGetNoteCallBackArgumentCaptor;

    private DetailNoteViewModel mDetailNoteViewModel;

    private Note mNote;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);

        when(mApplication.getApplicationContext()).thenReturn(mApplication);

        when(mApplication.getString(R.string.no_data)).thenReturn(NO_DATA);

//        when("").thenReturn(NO_DATA_DESC);

        when(mApplication.getResources()).thenReturn(mock(Resources.class));

        mNote = new Note(TITLE, TEXT);

        mDetailNoteViewModel = new DetailNoteViewModel(mApplication, mNotesRepository);

    }

    @Test
    public void getNoteFromRepositoryAndLoadIntoView(){

        setupViewModelRepositoryCallback();

        assertEquals(mDetailNoteViewModel.mNote.get().getTitle(), mNote.getTitle());

        assertEquals(mDetailNoteViewModel.mNote.get().getText(), mNote.getText());

    }

    @Test
    public void deleteNote(){

        setupViewModelRepositoryCallback();

        mDetailNoteViewModel.deleteNote();

        verify(mNotesRepository).deleteNote(mNote.getId());

    }

    @Test
    public void markNote(){

        setupViewModelRepositoryCallback();

        mDetailNoteViewModel.setMarked(true);

        verify(mNotesRepository).markNote(mNote);

        assertThat(mDetailNoteViewModel.getSnackbarMessage().getValue(), is(R.string.note_marked));

    }

    @Test
    public void unMarkNote(){

        setupViewModelRepositoryCallback();

        mDetailNoteViewModel.setMarked(false);

        verify(mNotesRepository).unMarkNote(mNote);

        assertThat(mDetailNoteViewModel.getSnackbarMessage().getValue(), is(R.string.note_unmarked));

    }

    @Test
    public void detailNoteViewModel_repositoryError(){

        mGetNoteCallBack = mock(NotesDatasource.GetNoteCallBack.class);

        mDetailNoteViewModel.start(mNote.getId());

        verify(mNotesRepository).getNote(eq(mNote.getId()), mGetNoteCallBackArgumentCaptor.capture());

        mGetNoteCallBackArgumentCaptor.getValue().onDataNotAvailable();

        assertFalse(mDetailNoteViewModel.isDataAvailable());

    }

    @Test
    public void detailNoteViewModel_repositoryNull(){

        setupViewModelRepositoryCallback();

        mGetNoteCallBackArgumentCaptor.getValue().onNoteLoaded(null);

        assertFalse(mDetailNoteViewModel.isDataAvailable());

        assertThat(mDetailNoteViewModel.mNote.get(), is(nullValue()));

    }

    private void setupViewModelRepositoryCallback(){

        mGetNoteCallBack = mock(NotesDatasource.GetNoteCallBack.class);

        mDetailNoteViewModel.start(mNote.getId());

        verify(mNotesRepository).getNote(eq(mNote.getId()), mGetNoteCallBackArgumentCaptor.capture());

        mGetNoteCallBackArgumentCaptor.getValue().onNoteLoaded(mNote);

    }

}
