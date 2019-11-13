package cs.android.task.entity;

import java.util.Date;

public class Project {
    private String name;
    private Date createDate;
    private String leaderName;

public String getName () {
    return name;
}

public void setName (String name) {
    this.name = name;
}

public Date getCreateDate () {
    return createDate;
}

public void setCreateDate (Date createDate) {
    this.createDate = createDate;
}

public String getLeaderName () {
    return leaderName;
}

public void setLeaderName (String leaderName) {
    this.leaderName = leaderName;
}
}
