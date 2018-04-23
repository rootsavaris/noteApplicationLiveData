package com.example.rafaelsavaris.noteapplicationlivedata;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.rafaelsavaris.noteapplicationlivedata.notes.list.NotesViewModel;

public class SwipeRefreshLayoutDataBinding {

    @BindingAdapter("android:onRefresh")
    public static void setSwipeRefreshLayoutOnRefreshListener(ScrollChildSwipeRefreshLayout view, final NotesViewModel notesViewModel){

        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notesViewModel.loadNotes(true);
            }
        });

    }

}
