package in.goutamstark.roomintroduction.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

import in.goutamstark.roomintroduction.entity.NoteEntity;
import in.goutamstark.roomintroduction.util.Constants;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM "+ Constants.TABLE_NAME_NOTE)
    List<NoteEntity> getNotes();

    /*
    * Insert the object in database
    * @param noteEntity, object to be inserted
    */
    @Insert
    long insertNote(NoteEntity noteEntity);

    /*
    * update the object in database
    * @param note, object to be updated
    */
    @Update
    void updateNote(NoteEntity repos);

    /*
    * delete the object from database
    * @param noteEntity, object to be deleted
    */
    @Delete
    void deleteNote(NoteEntity noteEntity);

    // NoteEntity... is varargs, here noteEntity is an array
    /*
    * delete list of objects from database
    * @param noteEntity, array of oject to be deleted
    */
    @Delete
    void deleteNotes(NoteEntity... noteEntity);

}
