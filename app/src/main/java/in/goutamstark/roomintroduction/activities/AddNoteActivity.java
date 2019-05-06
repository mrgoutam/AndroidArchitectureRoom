package in.goutamstark.roomintroduction.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.lang.ref.WeakReference;

import in.goutamstark.roomintroduction.R;
import in.goutamstark.roomintroduction.database.NoteDatabase;
import in.goutamstark.roomintroduction.entity.NoteEntity;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputEditText et_title,et_content;
    private NoteDatabase noteDatabase;
    private NoteEntity noteEntity;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        noteDatabase = NoteDatabase.getInstance(AddNoteActivity.this);
        Button button = findViewById(R.id.but_save);
        if ( (noteEntity = (NoteEntity) getIntent().getSerializableExtra("noteEntity"))!=null ){
            getSupportActionBar().setTitle("Update NoteEntity");
            update = true;
            button.setText("Update");
            et_title.setText(noteEntity.getTitle());
            et_content.setText(noteEntity.getContent());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update){
                    noteEntity.setContent(et_content.getText().toString());
                    noteEntity.setTitle(et_title.getText().toString());
                    noteDatabase.getNoteDao().updateNote(noteEntity);
                    setResult(noteEntity,2);
                }else {
                    noteEntity = new NoteEntity(et_content.getText().toString(), et_title.getText().toString());
                    new InsertTask(AddNoteActivity.this, noteEntity).execute();
                }
            }
        });
    }

    private void setResult(NoteEntity noteEntity, int flag){
        setResult(flag,new Intent().putExtra("noteEntity", noteEntity));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<AddNoteActivity> activityReference;
        private NoteEntity noteEntity;

        // only retain a weak reference to the activity
        InsertTask(AddNoteActivity context, NoteEntity noteEntity) {
            activityReference = new WeakReference<>(context);
            this.noteEntity = noteEntity;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented noteEntity id
            long j = activityReference.get().noteDatabase.getNoteDao().insertNote(noteEntity);
            noteEntity.setNote_id(j);
            Log.e("ID ", "doInBackground: "+j );
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool){
                activityReference.get().setResult(noteEntity,1);
                activityReference.get().finish();
            }
        }
    }


}
