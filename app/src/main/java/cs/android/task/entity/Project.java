package cs.android.task.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import androidx.annotation.NonNull;

public class Project {
private String name;
private Date createDate;
private String leaderName;
private String leaderPhone;
private String leaderEmail;
private long id;

private List<Member> members = new LinkedList<>();

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public void setLeaderEmail(String leaderEmail) {
        this.leaderEmail = leaderEmail;
    }

    public String getLeaderEmail() {
        return leaderEmail;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public long getId(){
    return id;
}

    public void setId(long id){
    this.id = id;
}

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

    public Optional<Member> getMember(@NonNull String phoneNum) {
    return members.stream()
            .filter(m -> m.getPhoneNum().equals(phoneNum))
            .findFirst();
}

public void addMember(Member member) {
    boolean exist = getMember(member.getPhoneNum()).isPresent();
    if (!exist){
        members.add(member);
    }

}
}
