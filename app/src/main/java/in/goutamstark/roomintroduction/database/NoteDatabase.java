package in.goutamstark.roomintroduction.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import in.goutamstark.roomintroduction.dao.NoteDao;
import in.goutamstark.roomintroduction.entity.NoteEntity;
import in.goutamstark.roomintroduction.util.Constants;
import in.goutamstark.roomintroduction.util.DateRoomConverter;

@Database(entities = {NoteEntity.class}, version = 1)
@TypeConverters({DateRoomConverter.class})
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();
    private static NoteDatabase noteDB;

    // synchronized is use to avoid concurrent access in multithreaded environment
    public static /*synchronized*/ NoteDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }

    private static NoteDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                NoteDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public void cleanUp() {
        noteDB = null;
    }
}