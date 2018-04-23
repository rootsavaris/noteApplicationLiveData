package com.example.rafaelsavaris.noteapplicationlivedata.notes.list;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.content.res.Resources;

import com.example.rafaelsavaris.noteapplicationlivedata.R;
import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesDatasource;
import com.example.rafaelsavaris.noteapplicationlivedata.data.source.NotesRepository;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.TestUtils;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.add.AddEditNoteActivity;
import com.example.rafaelsavaris.noteapplicationlivedata.notes.detail.DetailNoteActivity;
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

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rafael.savaris on 11/01/2018.
 */

public class NotesViewModelTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private static List<Note> NOTES;

    @Mock
    private NotesRepository mNotesRepository;

    @Mock
    private Application mApplication;

    @Captor
    private ArgumentCaptor<NotesDatasource.LoadNotesCallBack> mLoadNotesCallBackArgumentCaptor;

    private NotesViewModel mNotesViewModel;

    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);

        when(mApplication.getApplicationContext()).thenReturn(mApplication);

        when(mApplication.getString(R.string.successfully_saved_note_message)).thenReturn("EDIT_RESULT_OK");

        when(mApplication.getString(R.string.successfully_added_note_message)).thenReturn("ADD_RESULT_OK");

        when(mApplication.getString(R.string.successfully_deleted_note_message)).thenReturn("DELETE_RESULT_OK");

        when(mApplication.getResources()).thenReturn(mock(Resources.class));

        mNotesViewModel = new NotesViewModel(mApplication, mNotesRepository);

        NOTES = Lists.newArrayList(new Note("title1", "text1"), new Note("title2", "text2", true), new Note("title3", "text3", true));

    }

    @Test
    public void loadAllNotesFromRepositoryAndLoadIntoView(){

        mNotesViewModel.setFilter(NotesFilterType.ALL_NOTES);

        mNotesViewModel.loadNotes(true);

        verify(mNotesRepository).getNotes(mLoadNotesCallBackArgumentCaptor.capture());

        assertTrue(mNotesViewModel.dataLoading.get());

        mLoadNotesCallBackArgumentCaptor.getValue().onNotesLoaded(NOTES);

        assertFalse(mNotesViewModel.dataLoading.get());

        assertFalse(mNotesViewModel.mItems.isEmpty());

        assertTrue(mNotesViewModel.mItems.size() == 3);

    }

    @Test
    public void loadMarkedNotesFromRepositoryAndLoadIntoView(){

        mNotesViewModel.setFilter(NotesFilterType.MARKED_NOTES);

        mNotesViewModel.loadNotes(true);

        verify(mNotesRepository).getNotes(mLoadNotesCallBackArgumentCaptor.capture());

        assertTrue(mNotesViewModel.dataLoading.get());

        mLoadNotesCallBackArgumentCaptor.getValue().onNotesLoaded(NOTES);

        assertFalse(mNotesViewModel.dataLoading.get());

        assertFalse(mNotesViewModel.mItems.isEmpty());

        assertTrue(mNotesViewModel.mItems.size() == 2);

    }

    @Test
    public void clickOnFab_ShowAddNoteUi(){

        Observer<Void> observer = mock(Observer.class);

        mNotesViewModel.getNewNoteEvent().observe(TestUtils.TEST_OBSERVER, observer);

        mNotesViewModel.addNewNote();

        verify(observer).onChanged(null);

    }

    @Test
    public void clearMarkedNotes_ClearNotes(){

        mNotesViewModel.clearMarkedNotes();

        verify(mNotesRepository).clearMarkedNotes();

        verify(mNotesRepository).getNotes(any(NotesDatasource.LoadNotesCallBack.class));

    }

    @Test
    public void handleActivityResult_editOk(){

        Observer<Integer> observer = mock(Observer.class);

        mNotesViewModel.getSnackbarMessage().observe(TestUtils.TEST_OBSERVER, observer);

        mNotesViewModel.handleActivityResult(AddEditNoteActivity.REQUEST_CODE, DetailNoteActivity.EDIT_RESULT_OK);

        verify(observer).onChanged(R.string.successfully_saved_note_message);

    }

    @Test
    public void handleActivityResult_addOk(){

        Observer<Integer> observer = mock(Observer.class);

        mNotesViewModel.getSnackbarMessage().observe(TestUtils.TEST_OBSERVER, observer);

        mNotesViewModel.handleActivityResult(AddEditNoteActivity.REQUEST_CODE, AddEditNoteActivity.ADD_EDIT_RESULT_OK);

        verify(observer).onChanged(R.string.successfully_added_note_message);

    }

    @Test
    public void handleActivityResult_deleteOk(){

        Observer<Integer> observer = mock(Observer.class);

        mNotesViewModel.getSnackbarMessage().observe(TestUtils.TEST_OBSERVER, observer);

        mNotesViewModel.handleActivityResult(AddEditNoteActivity.REQUEST_CODE, DetailNoteActivity.DELETE_RESULT_OK);

        verify(observer).onChanged(R.string.successfully_deleted_note_message);

    }

    @Test
    public void getNotesAddViewVisible(){

        mNotesViewModel.setFilter(NotesFilterType.ALL_NOTES);

        assertThat(mNotesViewModel.mNotesAddViewVisible.get(), is(true));

    }

}
