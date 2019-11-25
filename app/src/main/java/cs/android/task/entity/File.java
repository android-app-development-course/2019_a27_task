package cs.android.task.entity;


import java.util.Date;

public class File {
    private String owner;
    private String name;
    private Date lastUpdate;
    private LogItem[] trace;


public String getOwner () {
    return owner;
}

public void setOwner (String owner) {
    this.owner = owner;
}

public String getName () {
    return name;
}

public void setName (String name) {
    this.name = name;
}

public Date getLastUpdate () {
    return lastUpdate;
}

public void setLastUpdate (Date lastUpdate) {
    this.lastUpdate = lastUpdate;
}

public LogItem[] getTrace () {
    return trace;
}

public void setTrace (LogItem[] trace) {
    this.trace = trace;
}
}
