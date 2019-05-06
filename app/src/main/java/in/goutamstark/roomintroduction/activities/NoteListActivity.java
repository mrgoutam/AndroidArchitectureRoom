package in.goutamstark.roomintroduction.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.goutamstark.roomintroduction.R;
import in.goutamstark.roomintroduction.adapter.NotesAdapter;
import in.goutamstark.roomintroduction.database.NoteDatabase;
import in.goutamstark.roomintroduction.entity.NoteEntity;

public class NoteListActivity extends AppCompatActivity implements NotesAdapter.OnNoteItemClick{

    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private NoteDatabase noteDatabase;
    private List<NoteEntity> noteEntities;
    private NotesAdapter notesAdapter;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        displayList();
    }

    private void displayList(){
        noteDatabase = NoteDatabase.getInstance(NoteListActivity.this);
        new RetrieveTask(this).execute();
    }

     private static class RetrieveTask extends AsyncTask<Void,Void,List<NoteEntity>>{

        private WeakReference<NoteListActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(NoteListActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<NoteEntity> doInBackground(Void... voids) {
            if (activityReference.get()!=null)
                return activityReference.get().noteDatabase.getNoteDao().getNotes();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<NoteEntity> noteEntities) {
            if (noteEntities !=null && noteEntities.size()>0 ){
                activityReference.get().noteEntities.clear();
                activityReference.get().noteEntities.addAll(noteEntities);
                // hides empty text view
                activityReference.get().textViewMsg.setVisibility(View.GONE);
                activityReference.get().notesAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initialize(){
        textViewMsg = findViewById(R.id.tv__empty);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(listener);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(NoteListActivity.this));
        noteEntities = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteEntities,NoteListActivity.this);
        recyclerView.setAdapter(notesAdapter);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(NoteListActivity.this,AddNoteActivity.class),100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0 ){
            if( resultCode == 1){
                noteEntities.add((NoteEntity) data.getSerializableExtra("note"));
            }else if( resultCode == 2){
                noteEntities.set(pos,(NoteEntity) data.getSerializableExtra("note"));
            }
            listVisibility();
        }
    }

    @Override
    public void onNoteClick(final int pos) {
            new AlertDialog.Builder(NoteListActivity.this)
            .setTitle("Select Options")
            .setItems(new String[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0:
                            noteDatabase.getNoteDao().deleteNote(noteEntities.get(pos));
                            noteEntities.remove(pos);
                            listVisibility();
                            break;
                        case 1:
                            NoteListActivity.this.pos = pos;
                            startActivityForResult(
                                    new Intent(NoteListActivity.this,
                                            AddNoteActivity.class).putExtra("note", noteEntities.get(pos)),
                                    100);

                            break;
                    }
                }
            }).show();

    }

    private void listVisibility(){
        int emptyMsgVisibility = View.GONE;
        if (noteEntities.size() == 0){ // no item to display
            if (textViewMsg.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        textViewMsg.setVisibility(emptyMsgVisibility);
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        noteDatabase.cleanUp();
        super.onDestroy();
    }
}
