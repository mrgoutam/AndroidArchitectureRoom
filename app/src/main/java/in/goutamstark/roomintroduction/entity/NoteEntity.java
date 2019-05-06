package in.goutamstark.roomintroduction.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;



import java.io.Serializable;
import java.util.Date;

import in.goutamstark.roomintroduction.util.Constants;

@Entity(tableName = Constants.TABLE_NAME_NOTE)
public class NoteEntity implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private long note_id;

    @ColumnInfo(name = "note_content") // column name will be "note_content" instead of "content" in table
    private String content;

    private String title;

    private Date date;

//    public NoteEntity(int note_id, String content, String title, Date date) {
//        this.note_id = note_id;
//        this.content = content;
//        this.title = title;
//        this.date = date;
//    }

    public NoteEntity(String content, String title) {
        this.content = content;
        this.title = title;
        this.date = new Date(System.currentTimeMillis());
    }

    @Ignore
    public NoteEntity(){}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getNote_id() {
        return note_id;
    }

    public void setNote_id(long note_id) {
        this.note_id = note_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteEntity)) return false;

        NoteEntity noteEntity = (NoteEntity) o;

        if (note_id != noteEntity.note_id) return false;
        return title != null ? title.equals(noteEntity.title) : noteEntity.title == null;
    }



    @Override
    public int hashCode() {
        int result = (int)note_id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "note_id=" + note_id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
