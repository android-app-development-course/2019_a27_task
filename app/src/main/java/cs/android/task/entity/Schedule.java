package cs.android.task.entity;

import java.util.Date;

public class Schedule {
    private String name;
    private Date date;
    private String location;
    private String mark;

    public Schedule(String sName, Date sDate, String sLocation, String sMark){
        name = sName;
        date = sDate;
        location = sLocation;
        mark = sMark;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
