package com.example.rafaelsavaris.noteapplicationlivedata.notes.list;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.rafaelsavaris.noteapplicationlivedata.data.model.Note;
import com.example.rafaelsavaris.noteapplicationlivedata.databinding.NoteItemBinding;

import java.util.List;

public class NotesAdapter extends BaseAdapter {

    private final NotesViewModel mNotesViewModel;

    private List<Note> mNotes;

    public NotesAdapter(List<Note> notes, NotesViewModel notesViewModel){
        mNotesViewModel = notesViewModel;
        setList(notes);
    }

    public void replaceData(List<Note> notes){
        setList(notes);
    }

    @Override
    public int getCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NoteItemBinding noteItemBinding;

        if (convertView == null){

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            noteItemBinding = NoteItemBinding.inflate(layoutInflater, parent, false);

        } else {

            noteItemBinding = DataBindingUtil.getBinding(convertView);

        }

        NoteItemUserActionsListener noteItemUserActionsListener = new NoteItemUserActionsListener() {

            @Override
            public void onMarkChanged(Note note, View view) {

                boolean marked = ((CheckBox) view).isChecked();

                if (marked){
                    mNotesViewModel.markNote(note);
                } else {
                    mNotesViewModel.unMarkNote(note);
                }

            }

            @Override
            public void onNoteClicked(Note note) {
                mNotesViewModel.getOpenNoteEvent().setValue(note.getId());
            }
        };

        noteItemBinding.setNote(mNotes.get(position));

        noteItemBinding.setListener(noteItemUserActionsListener);

        noteItemBinding.executePendingBindings();

        return noteItemBinding.getRoot();

    }

    private void setList(List<Note> notes){
        mNotes = notes;
        notifyDataSetChanged();
    }

}
