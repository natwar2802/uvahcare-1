package com.innovation.socialmedia.Database;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "myTable")
public class EntityClass {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "eventname")
    String eventname;
    @ColumnInfo(name = "eventStartdate")
    String eventStartdate;
   // @ColumnInfo(name = "eventEnddate")
    //String eventEnddate;
    @ColumnInfo(name = "eventtime1")

    String eventtime1;
   // @ColumnInfo(name = "eventtime2")

    //String eventtime2;


    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

   /* public String getEventEnddate() {
        return eventEnddate;
    }

    public void setEventEnddate(String eventEnddate) {
        this.eventEnddate = eventEnddate;
    }*/

    public String getEventStartdate() {
        return eventStartdate;
    }

    public void setEventStartdate(String eventStartdate) {
        this.eventStartdate = eventStartdate;
    }

   /* public String getEventtime2() {
        return eventtime2;
    }

    public void setEventtime2(String eventtime2) {
        this.eventtime2 = eventtime2;
    }*/

    public String getEventtime1() {
        return eventtime1;
    }

    public void setEventtime1(String eventtime1) {
        this.eventtime1 = eventtime1;
    }
}
